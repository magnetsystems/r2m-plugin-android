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

import com.intellij.util.net.HTTPMethod;
import com.magnet.plugin.constants.FormConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Andrew
 */
public class MethodTypeSection extends BasePanel {

    private JLabel jLabel1;
    private JSeparator jSeparator1;

    private JRadioButton buttonGETRequest;
    private JRadioButton buttonPOSTRequest;
    private JRadioButton buttonPUTRequest;
    private JRadioButton buttonDELETERequest;

    private HTTPMethod httpMethod = HTTPMethod.GET;

    private RequestPayloadSection requestPayloadSection;

    private ButtonGroup buttonGroup;

    public MethodTypeSection(RequestPayloadSection requestPayloadSection) {
        this.requestPayloadSection = requestPayloadSection;
        requestPayloadSection.setVisible(false);
    }

    {
        jLabel1 = new JLabel("Method Type");

        jSeparator1 = new JSeparator();
        jSeparator1.setOpaque(false);

        buttonGETRequest = new JRadioButton("GET");
        buttonPOSTRequest = new JRadioButton("POST");
        buttonPUTRequest = new JRadioButton("PUT");
        buttonDELETERequest = new JRadioButton("DELETE");

        buttonGETRequest.setSelected(true);


        jLabel1.setFont(baseFont);
        buttonGETRequest.setFont(baseFont);
        buttonPOSTRequest.setFont(baseFont);
        buttonPUTRequest.setFont(baseFont);
        buttonDELETERequest.setFont(baseFont);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(buttonGETRequest);
        buttonGroup.add(buttonPOSTRequest);
        buttonGroup.add(buttonPUTRequest);
        buttonGroup.add(buttonDELETERequest);

        buttonPOSTRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                requestPayloadSection.setVisible(true);
                httpMethod = HTTPMethod.POST;
            }
        });
        buttonPUTRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                requestPayloadSection.setVisible(true);
                httpMethod = HTTPMethod.PUT;
            }
        });
        buttonGETRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                requestPayloadSection.setVisible(false);
                requestPayloadSection.clearJson();
                httpMethod = HTTPMethod.GET;
            }
        });
        buttonDELETERequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                requestPayloadSection.setVisible(false);
                requestPayloadSection.clearJson();
                httpMethod = HTTPMethod.DELETE;
            }
        });

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
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(FormConfig.CUSTOM_METHOD_TYPE_GAP)
                                        .addComponent(buttonGETRequest, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonPOSTRequest, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonPUTRequest, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonDELETERequest, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                        )
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel1)
                                .addComponent(jSeparator1, FormConfig.SEPARATOR_CUSTOM_SIZE, FormConfig.SEPARATOR_CUSTOM_SIZE, FormConfig.SEPARATOR_CUSTOM_SIZE)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(buttonGETRequest)
                                .addComponent(buttonPOSTRequest)
                                .addComponent(buttonPUTRequest)
                                .addComponent(buttonDELETERequest)
                        )
        );
    }

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }


}
