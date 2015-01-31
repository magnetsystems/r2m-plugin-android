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

import com.magnet.plugin.r2m.constants.FormConfig;
import com.magnet.plugin.r2m.messages.R2MMessages;

import javax.swing.*;

public class PathPartLabel extends BasePanel {

    {
        JLabel pathLabel = new JLabel();
        JLabel variableLabel = new JLabel();
        JLabel variableNameLabel = new JLabel();

        pathLabel.setText(R2MMessages.getMessage("PATH_PART_NAME"));
        variableLabel.setText(R2MMessages.getMessage("PATH_VARIABLE_CHECKBOX_NAME"));
        variableNameLabel.setText(R2MMessages.getMessage("PATH_VARIABLE_NAME"));

        pathLabel.setFont(baseFont);
        variableLabel.setFont(baseFont);
        variableNameLabel.setFont(baseFont);

        GroupLayout pathParamLabelsLayout = new GroupLayout(this);
        this.setLayout(pathParamLabelsLayout);
        pathParamLabelsLayout.setHorizontalGroup(
                pathParamLabelsLayout.createSequentialGroup()
                        .addComponent(pathLabel, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE)
                        .addComponent(variableLabel, GroupLayout.DEFAULT_SIZE, FormConfig.PATH_CHECKBOX_PREF_SIZE, FormConfig.PATH_CHECKBOX_MAX_SIZE)
                        .addComponent(variableNameLabel, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE));
        pathParamLabelsLayout.setVerticalGroup(
                pathParamLabelsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(pathLabel)
                .addComponent(variableLabel)
                .addComponent(variableNameLabel));
    }
}
