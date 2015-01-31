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

import com.magnet.plugin.r2m.models.JSONError;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.List;

import static com.magnet.plugin.r2m.constants.Colors.PINK;


public class HighlightHelper {



    public static void highlightErrors(List<JSONError> errors,
                                       JTextArea responseField) {
        responseField.getHighlighter().removeAllHighlights();
        for (JSONError error : errors) {
            setHighlight(responseField, error);
        }
    }

    private static void setHighlight(JTextArea highlight, JSONError error) {
        Highlighter.HighlightPainter errorHighlighter =
                new DefaultHighlighter.DefaultHighlightPainter(PINK);
        try {
            highlight.getHighlighter().addHighlight(error.getStartIndex(),
                    error.getEndIndex(),
                    errorHighlighter);
        } catch (Exception ble) {
            ble.printStackTrace();
        }
    }

}
