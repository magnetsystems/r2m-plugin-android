/*
 * Copyright (c) 2014 Magnet Systems, Inc.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.magnet.plugin.actions;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.reporter.ConnectionException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.util.net.HttpConfigurable;
import com.intellij.util.text.VersionComparatorUtil;
import com.magnet.plugin.helpers.Logger;
import com.magnet.plugin.helpers.Rest2MobileConstants;
import com.magnet.plugin.helpers.UIHelper;
import com.magnet.plugin.helpers.URLHelper;
import com.magnet.plugin.messages.Rest2MobileMessages;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Action for "Check Updates" menu
 */
public class CheckUpdatesAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {

        Properties updatesInfo = new Properties();
        InputStream infoStream = null;

        String url = Rest2MobileConstants.PUBLIC_VERSION_URL;
        try {
            infoStream = URLHelper.loadUrl(url);
            updatesInfo.load(infoStream);
        } catch (Exception ex) {
            UIHelper.showErrorMessage("Couldn't access version URL: " + Rest2MobileConstants.PUBLIC_VERSION_URL);
            Logger.error(CheckUpdatesAction.class, ex.getMessage());
            return;
        } finally {
            if (infoStream != null) {
                try {
                    infoStream.close();
                } catch (IOException e1) {
                    // ignore
                }
            }
        }

        String latestVersion = updatesInfo.getProperty(Rest2MobileConstants.LATEST_VERSION_KEY);
        String installedVersion = getInstalledVersion();

        Project project = e.getData(CommonDataKeys.PROJECT);
        if (VersionComparatorUtil.compare(installedVersion, latestVersion) >= 0) {
            showNoUpdateDialog(project, installedVersion, updatesInfo);
            return;
        }

        showUpdatesAvailableDialog(project, installedVersion, updatesInfo);

    }

    private static String getInstalledVersion() {
        return PluginManager.getPlugin(PluginId.getId(Rest2MobileConstants.PUBLIC_TOOL_PACKAGE)).getVersion();
    }

    private static void showUpdatesAvailableDialog(Project project, String installedVersion, Properties info) {
        String newVersion = info.getProperty(Rest2MobileConstants.LATEST_VERSION_KEY);
        String url = info.getProperty(Rest2MobileConstants.DOWNLOAD_URL_KEY);
        String description = info.getProperty(Rest2MobileConstants.DESCRIPTION_KEY);
        String comments = info.getProperty(Rest2MobileConstants.COMMENTS_KEY);
        Messages.showInfoMessage(project,
                Rest2MobileMessages.getMessage(Rest2MobileMessages.UPDATES_AVAILABLE, installedVersion, newVersion, url, description, comments),
                Rest2MobileMessages.getMessage(Rest2MobileMessages.UPDATES_WINDOW_TITLE));
    }

    private static void showNoUpdateDialog(Project project, String installedVersion, Properties info) {
        String newVersion = info.getProperty(Rest2MobileConstants.LATEST_VERSION_KEY);
        Messages.showInfoMessage(project,
                Rest2MobileMessages.getMessage(Rest2MobileMessages.NO_UPDATES_AVAILABLE, installedVersion, newVersion),
                Rest2MobileMessages.getMessage(Rest2MobileMessages.UPDATES_WINDOW_TITLE));
    }

}
