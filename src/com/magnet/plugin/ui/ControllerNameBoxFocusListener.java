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
package com.magnet.plugin.ui;

import com.intellij.openapi.project.Project;
import com.magnet.plugin.helpers.ControllerCacheManager;
import com.magnet.plugin.helpers.VerifyHelper;
import com.magnet.plugin.project.CacheManager;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;

/**
 * Focus listener for Controller name box
 */
public class ControllerNameBoxFocusListener implements FocusListener {

    private final Project project;
    private final AddControllerForm form;

    public ControllerNameBoxFocusListener(Project project, AddControllerForm form) {
        this.project = project;
        this.form = form;
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {}

    @Override
    public void focusLost(FocusEvent focusEvent) {
        String entry = form.getControllerName();
        if (null == entry || entry.isEmpty()) {
            return;
        }

        String name = populateControllerPackageAndName(entry);
        //populatePayloads(entry);


        form.getControllerNameBox().getEditor().setItem(VerifyHelper.verifyClassName(name));
    }

    private void populatePayloads(String entry) {
        // populate the payloads TODO: warn if already set.
        int index = entry.lastIndexOf('.');
        if (index <= 0) {
            return;
        }

        List<String> cachedControllers = Arrays.asList(ControllerCacheManager.getCachedControllers(project));
        if (!cachedControllers.contains(entry)) {
            return;
        }

        String packageName = entry.substring(0, index);
        String controllerName = entry.substring(index + 1);
        CacheManager cache = new CacheManager(project, packageName, controllerName);
        List<String> methodNames = cache.getControllerMethodNames();
        JTabbedPane tabPanel = form.getTabPanel();
        tabPanel.removeAll();


        return;

    }

    private String populateControllerPackageAndName(String entry) {
        if (entry.lastIndexOf('.') <= 0) {
            return entry;
        }

        String packageName = entry.substring(0, entry.lastIndexOf('.'));
        // populate package name
        form.getPackageNameField().setText(packageName);
        return entry.substring(entry.lastIndexOf('.') + 1);
    }

}
