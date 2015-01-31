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

package com.magnet.plugin.common.helpers;

import com.magnet.plugin.common.Logger;

/**
 * Logger for debugging
 */
public class FormattedLogger {
    private StringBuilder message;
    private Class aClass;
    private static final String CLASS_DIVIDER = " : ";
    private static final String ACTION_DIVIDER = " -> ";

    public FormattedLogger(Class aClass) {
        message = new StringBuilder();
        this.aClass = aClass;
        append(this.aClass.getSimpleName(), CLASS_DIVIDER);
    }

    public void append(String... strings) {
        for (String s : strings) {
            message.append(s);
        }
    }

    public void appendAction(String strings) {
        append(strings, ACTION_DIVIDER);
    }

    public void appendAction(String... strings) {
        for (String s : strings) {
            append(s, ACTION_DIVIDER);
        }
    }

    public void clearMessage() {
        message = new StringBuilder();
        append(this.aClass.getSimpleName(), CLASS_DIVIDER);
    }

    public void showInfoLog() {
        Logger.showLogInfo(message.toString());
    }

    public void showErrorLog() {
        Logger.showLogError(message.toString());
    }

}
