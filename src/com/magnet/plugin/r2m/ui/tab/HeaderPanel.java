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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnet.plugin.r2m.ui.tab;

import com.magnet.plugin.r2m.api.models.RequestHeaderModel;
import com.magnet.plugin.r2m.constants.FormConfig;
import com.magnet.plugin.r2m.messages.Rest2MobileMessages;

import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Swing component representing the header panel
 */
public class HeaderPanel extends BasePanel {

    private JTextField key;
    private JTextField value;
    private JButton delete;

    public ExtendedJPanel getParentPanel() {
        return parentPanel;
    }

    private ExtendedJPanel parentPanel;

    private final RequestHeaderModel header;

    public ActionListener getListener() {
        return listener;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
        delete.addActionListener(listener);

    }

    private ActionListener listener;

    public HeaderPanel(ExtendedJPanel parentPanel, RequestHeaderModel header) {
        this.parentPanel = parentPanel;
        this.parentPanel.addPanel(this);
        this.header = header;
    }


    {
        key = new JTextField();
        value = new JTextField();
        delete = new JButton(Rest2MobileMessages.getMessage("SECTION_DELETE"));

        delete.addActionListener(listener);

        key.setFont(baseFont);
        value.setFont(baseFont);
        delete.setFont(baseFont);


        GroupLayout jPanel2Layout = new GroupLayout(this);
        this.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(key, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE)
                                .addComponent(value, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE)
                                .addComponent(delete)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(key)
                                .addComponent(value)
                                .addComponent(delete)));
    }



    public RequestHeaderModel getHeader() {
        header.setValue(value.getText());
        header.setName(key.getText());
        return header;
    }

    public void setHeader(String key, String value) {
        this.value.setText(value);
        this.key.setText(key);
    }
}
