package com.magnet.plugin.constants;

import com.magnet.plugin.messages.Rest2MobileMessages;

/**
 * Created by dlernatovich on 9/18/14.
 */
public enum JSONErrorType {
    ERROR_NULL_VALUE(Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_NULL_VALUE_TEXT)),
    ERROR_EMPTY_ARRAY(Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_EMPTY_ARRAY_TEXT)),
    ERROR_EMPTY_DICTIONARY(Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_EMPTY_DICTIONARY_TEXT)),
    ERROR_MISSING_COMMA(Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_MISSING_COMMA_TEXT)),
    ERROR_MISSING_QUOTES(Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_MISSING_QUOTES_TEXT)),
    ERROR_MISSING_CLOSING(Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_MISSING_QUOTES_TEXT));
    private String message;

    JSONErrorType(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
