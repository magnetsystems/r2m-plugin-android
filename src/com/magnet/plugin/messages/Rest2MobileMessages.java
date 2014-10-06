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

/**
 * Placeholder for retrieving all L10N messages
 *
 */
public class Rest2MobileMessages {
    //
    // Constants pointing to keys in resource bundle
    //
    public static final String WINDOW_TITLE = "WINDOW_TITLE";
    public static final String TEST_API_BEFORE_SAVE_METHOD = "TEST_API_BEFORE_SAVE_METHOD";
    public static final String EMPTY_METHOD_NAME = "EMPTY_METHOD_NAME";
    public static final String PROVIDE_VALID_URL = "PROVIDE_VALID_URL";
    public static final String ERROR_REQUEST = "ERROR_REQUEST";
    public static final String EMPTY_CLASS_NAME = "EMPTY_CLASS_NAME";
    public static final String EMPTY_PACKAGE_NAME = "EMPTY_PACKAGE_NAME";
    public static final String REQUIRED_FIELD = "REQUIRED_FIELD";
    public static final String MUST_FILL_ALL_REQUIRED_FIELDS = "MUST_FILL_ALL_REQUIRED_FIELDS";
    public static final String PLUS_TAB = "PLUS_TAB";
    public static final String METHOD_N = "METHOD_N";
    public static final String SERVICE_WAS_GENERATED = "SERVICE_WAS_GENERATED";
    public static final String GENERATING_MESSAGE_SERVICE = "GENERATING_MESSAGE_SERVICE";
    public static final String SOURCE_AVAILABLE_CONTINUE_EDITING_QUESTION = "SOURCE_AVAILABLE_CONTINUE_EDITING_QUESTION";
    public static final String UPDATES_WINDOW_TITLE = "UPDATES_WINDOW_TITLE";
    public static final String UPDATES_AVAILABLE = "UPDATES_AVAILABLE";
    public static final String NO_UPDATES_AVAILABLE = "NO_UPDATES_AVAILABLE";
    public static final String INVALID_RESPONSE_MESSAGE = "INVALID_RESPONSE_MESSAGE";
    public static final String NAME_CONFLICTS_WITH_JAVA_KEYWORDS = "NAME_CONFLICTS_WITH_JAVA_KEYWORDS";

    public static final String ERROR_NULL_VALUE_TEXT = "ERROR_NULL_VALUE_TEXT";
    public static final String ERROR_EMPTY_ARRAY_TEXT = "ERROR_EMPTY_ARRAY_TEXT";
    public static final String ERROR_EMPTY_DICTIONARY_TEXT = "ERROR_EMPTY_DICTIONARY_TEXT";
    public static final String ERROR_MISSING_COMMA_TEXT = "ERROR_MISSING_COMMA_TEXT";
    public static final String ERROR_MISSING_QUOTES_TEXT = "ERROR_MISSING_QUOTES_TEXT";
    public static final String ERROR_INVALID_FORMAT = "ERROR_INVALID_FORMAT";

    public static final String VALIDATION_WARNING_MESSAGE = "VALIDATION_WARNING_MESSAGE";
    public static final String VALIDATION_WARNING_TITLE = "VALIDATION_WARNING_TITLE";
    public static final String VALIDATION_WARNING_QUESTION = "VALIDATION_WARNING_QUESTION";
    public static final String VALIDATION_WARNING_CONTINUE = "VALIDATION_WARNING_CONTINUE";
    public static final String VALIDATION_WARNING_CANCEL = "VALIDATION_WARNING_CANCEL";

    // Headers
    public static final String HEADER_SECTION_NAME = "HEADER_SECTION_NAME";
    public static final String HEADER_SECTION_ADD_NEW = "HEADER_SECTION_ADD_NEW";
    public static final String HEADER_SECTION_PLUS = "HEADER_SECTION_PLUS";
    public static final String HEADER_SECTION_DELETE = "HEADER_SECTION_DELETE";


  /**
     * @param key  constant identifying L10n message
     * @param args arguments to be expanded in L10n message
     * @return expanded L10n message given its key and arguments
     */
    public static String getMessage(String key, Object... args) {
        return MessagesSupport.getMessage("messages", key, args);
    }


}
