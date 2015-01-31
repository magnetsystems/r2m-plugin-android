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

package com.magnet.plugin.r2m.helpers;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.magnet.plugin.r2m.messages.Rest2MobileMessages;
import com.magnet.plugin.r2m.singletons.Application;

import java.awt.*;

/**
 * Main UI hub where windows are launched.
 */
public class UIHelper {

    public static final String ERROR_TEST_API = Rest2MobileMessages.getMessage(Rest2MobileMessages.TEST_API_BEFORE_SAVE_METHOD);
    public static final String ERROR_METHOD_NAME = Rest2MobileMessages.getMessage(Rest2MobileMessages.EMPTY_METHOD_NAME);
    public static final String ERROR_REQUEST = Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_REQUEST);
    public static final String ERROR_SERVICE_NAME = Rest2MobileMessages.getMessage(Rest2MobileMessages.EMPTY_CLASS_NAME);
    public static final String ERROR_PACKAGE_NAME = Rest2MobileMessages.getMessage(Rest2MobileMessages.EMPTY_PACKAGE_NAME);
    public static final String ERROR_REQUIRED_FIELD = Rest2MobileMessages.getMessage(Rest2MobileMessages.REQUIRED_FIELD);
    public static final String ERROR_FILL_REQUIRED_FIELD = Rest2MobileMessages.getMessage(Rest2MobileMessages.MUST_FILL_ALL_REQUIRED_FIELDS);
    public static final String MESSAGE_GENERATING_SERVICE = Rest2MobileMessages.getMessage(Rest2MobileMessages.GENERATING_MESSAGE_SERVICE);


    public static void openAPIDialog(Project project, AnActionEvent anAction) {
        Application.getApplication().getCurrentForm(project, anAction, false);
    }

    public static void showErrorMessage(String message) {
        Messages.showInfoMessage(message, Rest2MobileMessages.getMessage(Rest2MobileMessages.WINDOW_TITLE));
    }

    public static Font getFont() {
        return new Font("Lucinda Grande", Font.PLAIN, 12);
    }
}
