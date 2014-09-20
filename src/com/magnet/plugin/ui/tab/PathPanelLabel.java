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

import com.magnet.plugin.constants.FormConfig;

import javax.swing.*;

/**
 *
 * @author aderkach
 */
public class PathPanelLabel extends BasePanel {

    private JLabel path;
    private JLabel variable;
    private JLabel variableName;

    {
        path = new JLabel();
        variable = new JLabel();
        variableName = new JLabel();

        path.setText("Path");
        variable.setText("Variable?");
        variableName.setText("Variable Name");

        path.setFont(baseFont);
        variable.setFont(baseFont);
        variableName.setFont(baseFont);

        GroupLayout jPanel2Layout = new GroupLayout(this);
        this.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createSequentialGroup()
                .addComponent(path, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE)
                .addComponent(variable, GroupLayout.DEFAULT_SIZE, FormConfig.PATH_CHECKBOX_PREF_SIZE, FormConfig.PATH_CHECKBOX_MAX_SIZE)
                .addComponent(variableName, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(path)
                .addComponent(variable)
                .addComponent(variableName));
    }
}
