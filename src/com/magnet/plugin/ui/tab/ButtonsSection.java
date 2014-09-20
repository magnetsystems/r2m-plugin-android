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

/**
 * @author Andrew
 */
public class ButtonsSection extends BasePanel {

    private JButton testApiButton;
    private JButton createMethodButton;
    private JButton deletePanelButton;


    {
        testApiButton = new JButton("Test API");
        createMethodButton = new JButton("Save Method");
        createMethodButton.setEnabled(false);
        deletePanelButton = new JButton("Remove Method");
        deletePanelButton.setEnabled(false);
        createMethodButton.setVisible(false);

        testApiButton.setFont(baseFont);
        createMethodButton.setFont(baseFont);
        deletePanelButton.setFont(baseFont);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addComponent(testApiButton, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_BUTTON_SIZE, Short.MAX_VALUE)
                        .addComponent(createMethodButton, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_BUTTON_SIZE, Short.MAX_VALUE)
                        .addComponent(deletePanelButton, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_BUTTON_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(testApiButton)
                        .addComponent(createMethodButton)
                        .addComponent(deletePanelButton)
        );
    }

    public JButton getCreateMethodButton() {
        return createMethodButton;
    }

    public void setCreateMethodButton(JButton createMethodButton) {
        this.createMethodButton = createMethodButton;
    }

    public JButton getTestApiButton() {
        return testApiButton;
    }

    public void setTestApiButton(JButton testApiButton) {
        this.testApiButton = testApiButton;
    }

    public JButton getDeletePanelButton() {
        return deletePanelButton;
    }

    public void setDeletePanelButton(JButton deletePanelButton) {
        this.deletePanelButton = deletePanelButton;
    }


    public void enableCreateMethodButton(boolean isEnabled){
        createMethodButton.setEnabled(isEnabled);
    }
}
