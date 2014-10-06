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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnet.plugin.ui.tab;

import com.magnet.plugin.constants.FormConfig;

import javax.swing.*;

public class RequestPayloadSection extends PayloadPanel {

    {
        JLabel jLabel1 = new JLabel("Request Payload");
        jLabel1.setFont(baseFont);
        JSeparator jSeparator1 = new JSeparator();
        jSeparator1.setOpaque(false);
        JLabel jLabel2 = new JLabel("Raw");
        jLabel2.setFont(baseFont);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGap(FormConfig.CUSTOM_GAP)
                                .addComponent(jLabel1, GroupLayout.Alignment.TRAILING)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator1)
                                .addComponent(jLabel2)
                                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, FormConfig.DEFAULT_COMPONENT_SIZE, GroupLayout.DEFAULT_SIZE)
                                .addComponent(errorPanel, GroupLayout.DEFAULT_SIZE, FormConfig.DEFAULT_COMPONENT_SIZE, GroupLayout.DEFAULT_SIZE)
                        )
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(jSeparator1, FormConfig.SEPARATOR_CUSTOM_SIZE, FormConfig.SEPARATOR_CUSTOM_SIZE, FormConfig.SEPARATOR_CUSTOM_SIZE)
                                .addComponent(jLabel1)
                        )
                        .addComponent(jLabel2)
                        .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_TEXTAREA_SIZE, Short.MAX_VALUE)
                        .addComponent(errorPanel, GroupLayout.DEFAULT_SIZE, FormConfig.DEFAULT_COMPONENT_SIZE, GroupLayout.DEFAULT_SIZE)
        );
    }



}
