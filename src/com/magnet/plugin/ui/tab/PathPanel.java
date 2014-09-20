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
import com.magnet.plugin.helpers.UIHelper;
import com.magnet.plugin.helpers.VerifyHelper;
import com.magnet.plugin.models.Path;
import com.magnet.plugin.constants.FormConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import static com.magnet.plugin.helpers.UIHelper.ERROR_REQUIRED_FIELD;

public class PathPanel extends JPanel {

    private JTextField path;
    private JCheckBox variable;
    private JTextField variableName;
    private JButton delete;

    private String variableNameValue;

    private JPanel parrentPanel;

    private Path pathModel;
    private boolean isRequiredField = true;

    public PathPanel(JPanel parrentPanel, Path path) {
        this.parrentPanel = parrentPanel;
        this.pathModel = path;
        variableName.setText(pathModel.getVariableName());
        variable.setSelected(pathModel.isVariable());
        this.path.setText(path.getPath());
    }


    {
        path = new JTextField();
        variable = new JCheckBox();
        variableName = new JTextField();
        delete = new JButton("X");

        variableNameValue = "";

        Font font = UIHelper.getFont();
        path.setFont(font);
        variable.setFont(font);
        variableName.setFont(font);
        delete.setFont(font);

        HintHelper.setHintToTextField(ERROR_REQUIRED_FIELD, path);

        variable.setSelected(false);
        variableName.setEditable(false);

        variable.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                changeVariable(evt);
            }
        });

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
                                .addComponent(path, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE)
                                .addComponent(variable, GroupLayout.DEFAULT_SIZE, FormConfig.PATH_CHECKBOX_PREF_SIZE, FormConfig.PATH_CHECKBOX_MAX_SIZE)
                                .addComponent(variableName, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE)
                                .addComponent(delete)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(path)
                                .addComponent(variable)
                                .addComponent(variableName)
                                .addComponent(delete)));
    }

    private void changeVariable(ActionEvent evt) {
        if (variable.isSelected()) {
            variableName.setEditable(true);
            variableName.setText(variableNameValue);
            HintHelper.setHintToTextField(UIHelper.ERROR_REQUIRED_FIELD, variableName);
        } else {
            HintHelper.removeHintFromField(variableName);
            variableName.setEditable(false);
            variableNameValue = variableName.getText().toString().trim();
            variableName.setText("");
        }
    }

    public void deleteThisPanel() {
        parrentPanel.remove(this);
        if (parrentPanel instanceof URLSection) {
            ((URLSection) parrentPanel).removePath(this);
        }
        parrentPanel.revalidate();
        parrentPanel.validate();
        parrentPanel.repaint();
    }

    public Path getPath() {
        Path path = new Path();
        path.setPath(this.path.getText().toString());
        path.setVariable(variable.isSelected());
        path.setVariableName("{" + variableName.getText().toString() + "}");

        return path;
    }

    public void setFocusListener(FocusListener focusListener) {
        path.addFocusListener(focusListener);
        variableName.addFocusListener(focusListener);
        delete.addFocusListener(focusListener);
    }

    public void invalidateField() {
        variableName.setText(VerifyHelper.verifyVariableName(variableName.getText()));
    }

    public boolean checkRequirementField() {
        if (path.getText().toString().trim().equalsIgnoreCase("")) {
            return false;
        }

        if (variable.isSelected()) {
            if (variableName.getText().toString().trim().equalsIgnoreCase("")) {
                return false;
            }
        }

        return true;
    }
}
