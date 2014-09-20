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
package com.magnet.plugin.ui.tab;

import com.magnet.plugin.helpers.HintHelper;
import com.magnet.plugin.models.Query;
import com.magnet.plugin.constants.FormConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import static com.magnet.plugin.helpers.UIHelper.ERROR_REQUIRED_FIELD;

public class QueryPanel extends BasePanel {

    private JTextField key;
    private JTextField value;
    private JButton delete;

    private JPanel parrentPanel;

    private Query query;

    public QueryPanel(JPanel parrentPanel, Query query) {
        this.parrentPanel = parrentPanel;
        this.query = query;
        key.setText(query.getKey());
        value.setText(query.getValue());
    }

    {
        key = new JTextField();
        value = new JTextField();
        delete = new JButton("X");

        key.setFont(baseFont);
        value.setFont(baseFont);
        delete.setFont(baseFont);

        HintHelper.setHintToTextField(ERROR_REQUIRED_FIELD, key);
        HintHelper.setHintToTextField(ERROR_REQUIRED_FIELD, value);

        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                deleteThisPanel();
            }
        });


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

    public void deleteThisPanel() {
        parrentPanel.remove(this);
        if (parrentPanel instanceof URLSection) {
            ((URLSection) parrentPanel).removeQuery(this);
        }
        parrentPanel.revalidate();
        parrentPanel.validate();
        parrentPanel.repaint();
    }


    public Query getQuery() {
        query.setKey(key.getText());
        query.setValue(value.getText());
        return query;
    }

    public void setFocusListener(FocusListener focusListener) {
        key.addFocusListener(focusListener);
        value.addFocusListener(focusListener);
        delete.addFocusListener(focusListener);
    }

    public boolean checkRequirementField() {
        if (key.getText().toString().trim().equalsIgnoreCase("")) {
            return false;
        }

        if (value.getText().toString().trim().equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }
}
