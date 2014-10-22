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
import com.magnet.plugin.messages.Rest2MobileMessages;
import com.magnet.plugin.models.Query;
import com.magnet.plugin.constants.FormConfig;
import com.magnet.plugin.ui.AbstractDocumentListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.magnet.plugin.helpers.UIHelper.ERROR_REQUIRED_FIELD;

public class QueryPanel extends BasePanel {

    private JTextField key;
    private JTextField value;
    private JButton delete;

    private QueryParamCallBack callBack;

    private Query query;

    public QueryPanel(QueryParamCallBack callBack, Query query) {
        this.callBack = callBack;
        this.query = query;
        key.setText(query.getKey());
        value.setText(query.getValue());
    }

    {
        key = new JTextField();
        key.setFont(baseFont);
        key.getDocument().addDocumentListener(new AbstractDocumentListener() {

            @Override
            protected void doUpdate() {
              query.setKey(key.getText());
              callBack.updated(QueryPanel.this);
            }
        });

        value = new JTextField();
        value.setFont(baseFont);
        value.getDocument().addDocumentListener(new AbstractDocumentListener() {

            @Override
            protected void doUpdate() {
              query.setValue(value.getText());
              callBack.updated(QueryPanel.this);
            }
        });

        delete = new JButton(Rest2MobileMessages.getMessage(Rest2MobileMessages.SECTION_DELETE));
        delete.setFont(baseFont);

        HintHelper.setHintToTextField(ERROR_REQUIRED_FIELD, key);
        HintHelper.setHintToTextField(ERROR_REQUIRED_FIELD, value);

        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                callBack.deleted(QueryPanel.this);
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

    public Query getQuery() {
        query.setKey(key.getText());
        query.setValue(value.getText());
        return query;
    }

    public boolean checkRequirementField() {
        if (key.getText().trim().equalsIgnoreCase("")) {
            return false;
        }

        if (value.getText().trim().equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

  /**
   * Callback interface to handle events in this panel
   */
    public interface QueryParamCallBack {
        void deleted(QueryPanel queryPanel);
        void updated(QueryPanel queryPanel);
    }
}
