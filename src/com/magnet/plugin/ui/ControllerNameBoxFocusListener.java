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
import com.magnet.langpack.builder.rest.parser.RestExampleModel;
import com.magnet.plugin.helpers.ControllerHistoryManager;
import com.magnet.plugin.helpers.VerifyHelper;
import com.magnet.plugin.project.CacheManager;
import com.magnet.plugin.ui.tab.MethodTabPanel;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;

/**
 * Focus listener for Controller name box
 * When losing focus, the form is populated with cached data, if the controller matches one of the previously
 * created controllers
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

        String packageName;
        String controllerName;
        if (entry.lastIndexOf('.') <= 0) {
            controllerName = entry;
            form.populateMethods(controllerName, null, null);
        } else {
            packageName = entry.substring(0, entry.lastIndexOf('.'));
            controllerName = VerifyHelper.verifyClassName(entry.substring(entry.lastIndexOf('.') + 1));
            form.populateMethods(controllerName, packageName, getMethodsFromCache(entry));
        }
    }

    private List<RestExampleModel> getMethodsFromCache(String entry) {
        int index = entry.lastIndexOf('.');
        if (index <= 0) {
            return null;
        }

        if (!isCacheController(entry)) {
            return null;
        }

        String packageName = entry.substring(0, index);
        String controllerName = entry.substring(index + 1);
        CacheManager cache = new CacheManager(project, packageName, controllerName);
        return cache.getControllerMethodsModel();
    }

    private boolean isCacheController(String entry) {
        List<String> cachedControllers = Arrays.asList(ControllerHistoryManager.getCachedControllers(project));
        return cachedControllers.contains(entry);
    }

}
