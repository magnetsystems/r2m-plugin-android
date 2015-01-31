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
 * Constants
 */
public interface R2MConstants {
    /**
     * Public name fo the plugin
     */
    String PUBLIC_TOOL_NAME = "rest2mobile";
    /**
     * Developer landing page URL
     */
    String MAGNET_DEVELOPER_URL = "developer.magnet.com";
    /**
     * Public package for this plugin used to identify the plugin
     */
    String PUBLIC_TOOL_PACKAGE = "com.magnet.r2m";
    /**
     * Where to find the version information
     */
    String PUBLIC_VERSION_URL = "https://raw.githubusercontent.com/magnetsystems/r2m-plugin-android/master/version.properties";
    /**
     * connection timeout for http request
     */
    int CONNECTION_TIMEOUT = 10000;

    //
    // Key used in file pointed to by PUBLIC_VERSION_URL describing the version
    //
    String LATEST_VERSION_KEY = "version";
    String DOWNLOAD_URL_KEY = "downloadUrl";
    String DESCRIPTION_KEY="description";
    String COMMENTS_KEY="comments";

    /**
     * Default documentation
     */
    String DOCUMENTATION_URL = "https://github.com/magnetsystems/rest2mobile/wiki";

    //
    // Path variables
    //
    String END_TEMPLATE_VARIABLE = "}";
    String START_TEMPLATE_VARIABLE = "{";
    String END_TEMPLATE_VARIABLE_REGEX = "}";
    String START_TEMPLATE_VARIABLE_REGEX = "\\{";
    String TEMPLATE_VARIABLE_REGEX = "\\{(.*?)\\}";
}
