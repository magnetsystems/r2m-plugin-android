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

package com.magnet.plugin.messages;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Base class to retrieve L10N messages
 * @author etexier
 */
public class MessagesSupport {

    /**
     * Build the localized message given a resource bundle name, message key, and message arguments
     * @param bundleName bundleName
     * @param key the message key in the bundle
     * @param args the message arguments to inject in the message at key <code>key</code>
     * @return the localized message, if found, otherwise BUNDLE_NOT_FOUND, if bundle is not found, or MESSAGE_NOT_FOUND
     * if the message for key <code>key</code> is not found in bundle
     */
    static String getMessage(String bundleName, String key, Object... args) {
        ResourceBundle resourceBundle = getBundle(bundleName);
        if (null == resourceBundle) {
            return "BUNDLE_NOT_FOUND(" + bundleName + ")";
        }
        String message = null;
        try {
            message = resourceBundle.getString(key);
        } catch (Exception e) {
            // eat it
        }
        if (null == message) {
            return "MESSAGE_NOT_FOUND($bundleName, " + key + ", " + args + ")";
        }
        return MessageFormat.format(message, args);
    }

    /**
     * Utility method to retrieve the resource bundle
     * @param bundleName name of the resource bundle
     * @return bundle or null if none found
     */
    static ResourceBundle getBundle(String bundleName) {
        ResourceBundle res = null;
        try {
            res = ResourceBundle.getBundle(bundleName);
        } catch (Exception e) {
            // eat it
        }
        return res;
    }


}
