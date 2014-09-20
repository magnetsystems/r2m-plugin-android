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

package com.magnet.plugin.helpers;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * Logging utilities
 */
public class Logger {

    public static void info(Class cls, String message) {
        sendNotification(message, cls.getSimpleName(), NotificationType.INFORMATION);
    }

    public static void error(Class cls, String message) {
        sendNotification(message, cls.getSimpleName(), NotificationType.ERROR);
    }

    public static void info(String message) {
        sendNotification(message, "Information", NotificationType.INFORMATION);
    }

    public static void error(String message) {
        sendNotification(message, "Error", NotificationType.ERROR);
    }

    public static void sendNotification(String message, String tag, NotificationType notificationType) {
        if (null == message || message.isEmpty()) { // to prevent Assertion error
            return;
        }
        Notification notification = new Notification("MagnetPlugin", tag, espaceString(message), notificationType);
        Notifications.Bus.notify(notification);
    }


    private static String espaceString(String string) {
        // replace with both so that it returns are preserved in the notification ballon and in the event log
        return string.replaceAll("\n", "\n<br />");
    }
}
