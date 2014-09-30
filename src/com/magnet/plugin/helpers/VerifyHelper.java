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

import com.magnet.langpack.builder.rest.parser.validation.BodyValidationResult;
import com.magnet.langpack.builder.rest.parser.validation.BodyValidator;
import com.magnet.langpack.builder.rest.parser.validation.ValidationResultEntry;
import com.magnet.plugin.messages.Rest2MobileMessages;
import org.apache.commons.validator.routines.UrlValidator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * validation helpers
 */
public class VerifyHelper {
    /**
     * The supported schemes when validating the url
     */
    private static final String[] SUPPORTED_PROTOCOL_SCHEMES = {"http", "https"};
    private static final long URL_VALIDATION_OPTIONS = UrlValidator.ALLOW_LOCAL_URLS;

    public static final List<String> KEYWORDS = new ArrayList<String>(
            Arrays.asList(new String[]{
                    "abstract", "assert", "boolean", "break", "byte",
                    "case", "catch", "char", "class", "const",
                    "continue", "default", "do", "double", "else",
                    "enum", "extends", "final", "finally", "float",
                    "for", "goto", "if", "implements", "import", "instanceof",
                    "int", "interface", "long", "native", "new", "package", "private",
                    "protected", "public", "return", "short", "static", "strictfp",
                    "super", "switch", "synchronized", "this", "throw", "throws",
                    "transient", "try", "void", "volatile", "while"
            })
    );


    public static String verifyPackageName(String name) {
        name = name.replaceAll(">", "");
        return name;
    }

    public static String verifyClassName(String name) {
        return verify(name, "[^A-Za-z0-9_]", true);
    }

    public static String verifyMethodName(String name) {
        return verify(name, "[^A-Za-z0-9_]", false);
    }

    public static String verifyVariableName(String name) {
        return verify(name, "[^A-Za-z0-9_]", false);
    }

//    public static boolean verifyResponse(String text, boolean isNeedLogger, JComponent errorMessageField) {
//        boolean result = false;
//        // Do not unformat! it will remove the line separator used to found out which line and column are invalid
//        //        String requestOrResponseBody = FormatHelper.unformatJSONCode(text);
//        //        BodyValidationResult resultValidator = BodyValidator.validateBody(requestOrResponseBody);
//        BodyValidationResult resultValidator = BodyValidator.validateBody(text);
//        if (resultValidator.isValid()) {
//            result = true;
//        } else {
//            String errorMessage = "";
//            for (ValidationResultEntry entry :
//                    resultValidator.getErrors()) {
//                errorMessage = entry.getMessage();
//                if (isNeedLogger) {
//                    Logger.error(errorMessage);
//                }
//            }
//            setErrorMessageField(errorMessage, errorMessageField);
//        }
//        return result;
//    }

    public static BodyValidationResult verifyResponse(String text, boolean isNeedLogger, JComponent errorMessageField) {
        boolean result = false;
        // Do not unformat! it will remove the line separator used to found out which line and column are invalid
        // String requestOrResponseBody = FormatHelper.unformatJSONCode(text);
        // BodyValidationResult resultValidator = BodyValidator.validateBody(requestOrResponseBody);
        BodyValidationResult resultValidator = BodyValidator.validateBody(text);
        if (resultValidator.isValid()) {
            result = true;
        } else {
            String errorMessage = "";
            for (ValidationResultEntry entry :
                    resultValidator.getErrors()) {
                errorMessage = entry.getMessage();

                if (isNeedLogger) {
                    Logger.error(errorMessage);
                }
            }
            setErrorMessageField(errorMessage, errorMessageField);
        }
        return resultValidator;
    }

    private static void setErrorMessageField(String message, JComponent errorMessageField) {
        if (errorMessageField != null) {
            if (errorMessageField instanceof JLabel) {
                ((JLabel) errorMessageField).setText(message);
            } else if (errorMessageField instanceof JTextField) {
                ((JTextField) errorMessageField).setText(message);
            }
        }
    }

    private static String verify(String name, String regExp, boolean isUpper) {
        name = name.replaceAll(regExp, "");
        if (!name.equalsIgnoreCase("")) {
            if (isUpper) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            } else {
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
            }
        }
        name = verifyByKeyWords(name);
        return name;
    }

    public static boolean isValidUrl(String url) {
        String templateURL = url;
        templateURL = templateURL.replaceAll("\\{", "");
        templateURL = templateURL.replaceAll("}", "");
        UrlValidator urlValidator = new UrlValidator(SUPPORTED_PROTOCOL_SCHEMES, URL_VALIDATION_OPTIONS);
        return urlValidator.isValid(templateURL);
    }

    public static boolean isValidUrlWithoutPerformance(String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    private static String verifyByKeyWords(String text) {
        if (KEYWORDS.contains(text)) {
            UIHelper.showErrorMessage(Rest2MobileMessages.getMessage(Rest2MobileMessages.NAME_CONFLICTS_WITH_JAVA_KEYWORDS));
            text = "";
        }
        return text;
    }
}
