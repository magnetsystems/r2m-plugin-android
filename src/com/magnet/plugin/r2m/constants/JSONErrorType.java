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

package com.magnet.plugin.r2m.constants;

import com.magnet.plugin.r2m.messages.Rest2MobileMessages;

/**
 * JSON errors types
 */
public enum JSONErrorType {
    ERROR_NULL_VALUE(Rest2MobileMessages.getMessage("ERROR_NULL_VALUE_TEXT")),
    ERROR_EMPTY_ARRAY(Rest2MobileMessages.getMessage("ERROR_EMPTY_ARRAY_TEXT")),
    ERROR_EMPTY_DICTIONARY(Rest2MobileMessages.getMessage("ERROR_EMPTY_DICTIONARY_TEXT")),
    ERROR_MISSING_COMMA(Rest2MobileMessages.getMessage("ERROR_MISSING_COMMA_TEXT")),
    ERROR_INVALID_FORMAT(Rest2MobileMessages.getMessage("ERROR_INVALID_FORMAT"));


  private String message;

    JSONErrorType(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
