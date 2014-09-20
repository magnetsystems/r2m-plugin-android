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

package com.magnet.plugin.ui.tab;

import com.magnet.plugin.constants.Colors;
import com.magnet.plugin.constants.FormConfig;
import com.magnet.plugin.constants.PluginIcon;
import com.magnet.plugin.helpers.FormatHelper;
import com.magnet.plugin.helpers.JSONValidator;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static com.magnet.plugin.constants.Colors.BLACK;
import static com.magnet.plugin.constants.Colors.GREEN;

public class JSONPanel extends BasePanel {

    protected JXLabel errorMessageField;
    protected JTextArea jsonField;
    protected JScrollPane jScrollPane;

    {


        errorMessageField = new JXLabel("");
        errorMessageField.setFont(getFont());
        errorMessageField.setForeground(Colors.RED);
        errorMessageField.setIcon(PluginIcon.validIcon);
        errorMessageField.setVisible(true);
        errorMessageField.setLineWrap(true);

        jsonField = new JTextArea();
        jsonField.setMinimumSize(FormConfig.PAYLOAD_DIMENSION);
        jsonField.setLineWrap(true);
        jsonField.setFont(baseFont);



        jsonField.getDocument().addDocumentListener(new DocumentListener() {



            private void setFieldColor() {
                String text = jsonField.getText();
                resetFields();
                if (JSONValidator.isJSON(text)) {
                    setFieldTextColor(BLACK);
                    JSONValidator.validateJSON(text, errorMessageField, jsonField);
                } else {
                    setFieldTextColor(GREEN);
                }
            }

            private void setFieldTextColor(Color color) {
                jsonField.setForeground(color);
            }

            private void resetFields() {
                jsonField.getHighlighter().removeAllHighlights();
                errorMessageField.setIcon(PluginIcon.validIcon);
                errorMessageField.setText("");
            }

            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                setFieldColor();

            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                setFieldColor();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                setFieldColor();
            }
        });

        jScrollPane = new JScrollPane();
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setViewportView(jsonField);

    }

    public String getUnformattedJson() {
        return FormatHelper.unformatJSONCode(getJson());
    }

    public void setJson(String response) {
        jsonField.setText(FormatHelper.formatJSONCode(response));
    }


    public JTextArea getJsonField() {
        return jsonField;
    }

    public String getJson() {
        return jsonField.getText();
    }

    public void clearJson() {
        jsonField.setText("");
    }
}
