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
package com.magnet.plugin.r2m.ui;

import com.intellij.openapi.project.Project;
import com.magnet.langpack.builder.rest.parser.RestExampleModel;
import com.magnet.plugin.r2m.helpers.ControllerHistoryManager;
import com.magnet.plugin.r2m.helpers.VerifyHelper;
import com.magnet.plugin.r2m.project.CacheManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;

/**
 * item listener for Controller name box
 * when an item is selection, the form is populated with cached data, if the controller matches one of the previously
 * created controllers
 */
public class ControllerNameBoxItemListener implements ActionListener, FocusListener{

    private final Project project;
    private final AddControllerForm form;

    public ControllerNameBoxItemListener(Project project, AddControllerForm form) {
        this.project = project;
        this.form = form;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String entry;
        String controllerName;
        entry = form.getControllerName();

        if (null == entry || entry.isEmpty()) {
            return;
        }

        String packageName;

        if (entry.lastIndexOf('.') <= 0) {
            controllerName = entry;
            form.populateMethods(controllerName, null, null);
        } else {
            packageName = entry.substring(0, entry.lastIndexOf('.'));
            controllerName = getControllerName();
            if (null == controllerName) {
                return;
            }
            form.populateMethods(controllerName, packageName, getMethodsFromCache(entry));
        }
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        return;
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        String controllerName = getControllerName();
        if (controllerName == null) {
            return;
        }
        form.getControllerNameBox().getChildComponent().getEditor().setItem(controllerName);

    }

    private String getControllerName() {
        String entry = form.getControllerName();

        if (null == entry || entry.isEmpty()) {
            return null;
        }

        String className = null;
        if (entry.lastIndexOf('.') <= 0) {
            className = entry;
        } else {
            className = VerifyHelper.verifyClassName(entry.substring(entry.lastIndexOf('.') + 1));
        }

        if(Character.isUpperCase(className.charAt(0))) {
          return className;
        } else {
          return (new StringBuilder()).append(Character.toUpperCase(className.charAt(0))).append(className.substring(1)).toString();
        }
    }


}
