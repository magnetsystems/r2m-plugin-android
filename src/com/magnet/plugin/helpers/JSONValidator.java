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
import com.magnet.plugin.constants.JSONErrorType;
import com.magnet.plugin.models.JSONError;
import com.magnet.plugin.constants.PluginIcon;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON validator according to Rest-By-Example specification
 */
public class JSONValidator {

    private static final String UNUSED_SYMBOLS = "\n|,|:|\"|\t";
    private static final String IS_JSON_VERIFY = "^\\{|^\\[|]$|}$";

    public static boolean isJSON(String text) {
        Pattern pattern = Pattern.compile(IS_JSON_VERIFY);
        Matcher matcher = pattern.matcher(removeUnusedSymbols(text.trim()));
        int matchCount = 0;
        while (matcher.find()) {
            matchCount++;
        }
        return matchCount >= 2;
    }

    public static void validateJSON(String text, JComponent field, JTextArea jsonField) {
        BodyValidationResult validationResult = BodyValidator.validateBody(text);

        if (validationResult.isValid()) {
            return;
        }
        JsonErrorConverter converter = new JsonErrorConverter(text);
        // Display error messages
        StringBuilder errorMessage = getErrorMessage(converter, validationResult.getErrors());
        setMessageToField(field, errorMessage.toString().trim(), !validationResult.isValid());
        // Highlight error in payload
        highlightErrors(converter, validationResult, jsonField);

    }

    private static void highlightErrors(JsonErrorConverter converter, BodyValidationResult validationResult, JTextArea jsonField) {
        List<JSONError> errors = new ArrayList<JSONError>();
        for (ValidationResultEntry entry : validationResult.getErrors()) {
            errors.add(converter.convert(entry));
        }
        HighlightHelper.highlightErrors(errors, jsonField);

    }

    public static BodyValidationResult validateBody(String text) {
        return BodyValidator.validateBody(text);
    }

    private static String removeUnusedSymbols(String text) {
        return text.replaceAll(UNUSED_SYMBOLS, " ");
    }

    public static StringBuilder getErrorMessage(JsonErrorConverter jsonErrorConverter, List<ValidationResultEntry> errors) {

        StringBuilder errorMessage = new StringBuilder();
        for (ValidationResultEntry error : errors) {
            errorMessage.append(jsonErrorConverter.convert(error));
        }
        return errorMessage;
    }

    private static void setMessageToField(JComponent field, String text, boolean isNeedVisible) {
        if (field instanceof JXLabel) {
            ((JXLabel) field).setText(text);
            if (isNeedVisible) {
                ((JXLabel) field).setIcon(PluginIcon.errorIcon);
            } else {
                ((JXLabel) field).setIcon(PluginIcon.validIcon);
            }
        } else if (field instanceof JTextField) {
            ((JTextField) field).setText(text);
            field.setVisible(isNeedVisible);
        } else if (field instanceof JTextArea) {
            ((JTextArea) field).setText(text);
            field.setVisible(isNeedVisible);
        }
    }

}
