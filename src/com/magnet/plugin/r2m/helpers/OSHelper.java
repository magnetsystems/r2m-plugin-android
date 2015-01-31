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

/**
 * OS utilities
 */
public class OSHelper {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static void testOS() {

        Logger.info(OSHelper.class, OS);

        if (isWindows()) {
            Logger.info(OSHelper.class, "This is Windows");
        } else if (isMac()) {
            Logger.info(OSHelper.class, "This is Mac");
        } else if (isUnix()) {
            Logger.info(OSHelper.class, "This is Unix or Linux");
        } else if (isSolaris()) {
            Logger.info(OSHelper.class, "This is Solaris");
        } else {
            Logger.info(OSHelper.class, "Your OS is not support!!");
        }
    }

    public static boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        return (OS.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );

    }

    public static boolean isSolaris() {

        return (OS.indexOf("sunos") >= 0);

    }
}
