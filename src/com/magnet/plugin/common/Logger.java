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

package com.magnet.plugin.common;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class Logger {


    public static void info(String message) {
        sendNotification(message, "Information", NotificationType.INFORMATION);
    }

    public static void error(String message) {
        sendNotification(message, "Error", NotificationType.ERROR);
    }

    public static void info(Class cls, String message) {
        sendNotification(message, cls.getSimpleName(), NotificationType.INFORMATION);
    }

    public static void error(Class cls, String message) {
        sendNotification(message, cls.getSimpleName(), NotificationType.ERROR);
    }

    private static void sendNotification(String message, String tag, NotificationType notificationType) {
        if (null == message || message.isEmpty()) { // to prevent Assertion error
            return;
        }
        Notification notification = new Notification(CommonConstants.LOGGER_TAG, tag, escapeString(message), notificationType);
        Notifications.Bus.notify(notification);
    }

    private static String escapeString(String string) {
        // replace with both so that it returns are preserved in the notification balloon and in the event log
        return string.replaceAll("\n", "\n<br />");
    }

    /**
     * Method which provide to show info log only in debug
     *
     * @param message message
     */
    public static void showLogInfo(String message) {
        System.out.println("=====> " + message);
    }

    /**
     * Method which provide to show error log only in debug
     *
     * @param message message
     */
    public static void showLogError(String message) {
        System.err.println("=====> " + message);
    }

}
