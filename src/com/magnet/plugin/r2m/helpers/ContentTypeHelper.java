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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Content-type utilities
 */
public class ContentTypeHelper {

    // Supported content types
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    /** pattern to discover a form url encoded content */
    public static final Pattern FORM_PARAM_PATTERN = Pattern.compile("(.+=.+)[&(.+=.+)]*");

    /** HTTP header for content type */
    public static final String CONTENT_TYPE_HEADER = "Content-Type";


    /**
     * Helper method to guess the content type of a body
     * @param body body to guess content-type from
     * @return content-type or null if there is no body.
     */
    public static String guessContentType(String body) {
        if (body == null || body.isEmpty()) {
            return null;
        }
        String result;
        String trimedBody = body.trim();
        if (trimedBody.startsWith("{") && trimedBody.endsWith("}") ||
                trimedBody.startsWith("[") && trimedBody.endsWith("]")) {
            result = CONTENT_TYPE_JSON;
        } else {
            Matcher m = FORM_PARAM_PATTERN.matcher(trimedBody);
            if (m.find()) {
                result = CONTENT_TYPE_FORM_URL_ENCODED;
            } else {
                result = CONTENT_TYPE_TEXT_PLAIN;
            }
        }

        return result;
    }



}
