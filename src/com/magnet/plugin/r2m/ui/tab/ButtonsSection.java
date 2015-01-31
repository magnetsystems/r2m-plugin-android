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
package com.magnet.plugin.r2m.ui.tab;

import com.magnet.plugin.r2m.constants.FormConfig;
import com.magnet.plugin.r2m.messages.R2MMessages;

import javax.swing.*;

public class ButtonsSection extends BasePanel {

    private JButton testApiButton;
    private JButton deletePanelButton;


    {
        testApiButton = new JButton(R2MMessages.getMessage("TEST_API_TEXT"));
        testApiButton.setToolTipText(R2MMessages.getMessage("TEST_API_TOOL_TIP"));
        deletePanelButton = new JButton(R2MMessages.getMessage("REMOVE_METHOD_TEXT"));
        deletePanelButton.setToolTipText(R2MMessages.getMessage("REMOVE_METHOD_TOOL_TIP"));
        deletePanelButton.setEnabled(true);

        testApiButton.setFont(baseFont);
        deletePanelButton.setFont(baseFont);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addComponent(testApiButton, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_BUTTON_SIZE, Short.MAX_VALUE)
                        .addComponent(deletePanelButton, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_BUTTON_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(testApiButton)
                        .addComponent(deletePanelButton)
        );
    }


    public JButton getTestApiButton() {
        return testApiButton;
    }

    public JButton getDeletePanelButton() {
        return deletePanelButton;
    }
}
