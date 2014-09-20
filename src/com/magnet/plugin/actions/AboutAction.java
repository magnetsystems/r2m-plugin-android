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

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import com.magnet.plugin.helpers.Rest2MobileConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Action for "About" menu item
 */
public class AboutAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        showAboutMessage(project);
    }

    private static void showAboutMessage(Project project) {

        Window window = WindowManager.getInstance().suggestParentWindow(project);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 200));

        // image
        Icon image = IconLoader.getIcon("/icon_logo@4x.png");
        panel.add(new JLabel(image), BorderLayout.CENTER);

        // coordinate
        RelativePoint location;
        if (window != null) {
            Rectangle r = window.getBounds();
            location = new RelativePoint(window, new Point((r.width - image.getIconWidth()) / 2, (r.height - image.getIconHeight()) / 2));
        }
        else {
            Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            location = new RelativePoint(new Point((r.width - image.getIconWidth()) / 2, (r.height - image.getIconHeight()) / 2));
        }

        String version = PluginManager.getPlugin(PluginId.getId(Rest2MobileConstants.PUBLIC_TOOL_PACKAGE)).getVersion();
        String text = Rest2MobileConstants.PUBLIC_TOOL_NAME + " " + version + " " + "(" + Rest2MobileConstants.MAGNET_DEVELOPER_URL+ ")";

        // popup
        JBPopupFactory.getInstance().createComponentPopupBuilder(panel, panel)
                .setRequestFocus(true)
                .setFocusable(true)
                .setResizable(false)
                .setMovable(false)
                .setModalContext(false)
                .setShowShadow(true)
//                .setShowBorder(false)
                .setCancelKeyEnabled(true)
                .setCancelOnClickOutside(true)
                .setCancelOnOtherWindowOpen(true)
                .setAdText(text)
                .createPopup()
                .show(location);

    }
}
