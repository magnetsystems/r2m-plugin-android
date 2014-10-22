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
import com.magnet.plugin.helpers.HintHelper;
import com.magnet.plugin.helpers.UIHelper;
import com.magnet.plugin.helpers.VerifyHelper;
import com.magnet.plugin.messages.Rest2MobileMessages;
import com.magnet.plugin.models.PathPart;
import com.magnet.plugin.ui.AbstractDocumentListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.magnet.plugin.helpers.UIHelper.ERROR_REQUIRED_FIELD;

public class PathPartPanel extends JPanel {

    private final JTextField pathPartField;
    private final JCheckBox isVariableCheckBox;
    private final JTextField variableNameField;
    private final JButton deleteButton;

    private final PathParamCallBack callBack;

    private final PathPart pathPart;

    public PathPartPanel(PathParamCallBack callBack, PathPart pathPart) {
        this.callBack = callBack;
        this.pathPart = pathPart;
        variableNameField.setText(pathPart.getVariableName());
        isVariableCheckBox.setSelected(pathPart.isTemplatized());
        this.pathPartField.setText(pathPart.getPathValue());
    }


    {
        Font font = UIHelper.getFont();
        pathPartField = new JTextField();
        pathPartField.setFont(font);
        pathPartField.getDocument().addDocumentListener(new AbstractDocumentListener() {

          @Override
          protected void doUpdate() {
            pathPart.setPathValue(pathPartField.getText());
            callBack.updated(PathPartPanel.this);
          }
        });

        isVariableCheckBox = new JCheckBox();
        isVariableCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                changeVariable(evt);
            }
        });

        variableNameField = new JTextField();
        variableNameField.getDocument().addDocumentListener(new AbstractDocumentListener() {

          @Override
          protected void doUpdate() {
                pathPart.setVariableName(variableNameField.getText());
                callBack.updated(PathPartPanel.this);
            }
        });

        deleteButton = new JButton(Rest2MobileMessages.getMessage(Rest2MobileMessages.SECTION_DELETE));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                callBack.deleted(PathPartPanel.this);
            }
        });

        isVariableCheckBox.setFont(font);
        variableNameField.setFont(font);
        deleteButton.setFont(font);

        HintHelper.setHintToTextField(ERROR_REQUIRED_FIELD, pathPartField);

        isVariableCheckBox.setSelected(false);
        variableNameField.setEditable(false);

        GroupLayout jPanel2Layout = new GroupLayout(this);
        this.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(pathPartField, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE)
                                .addComponent(isVariableCheckBox, GroupLayout.DEFAULT_SIZE, FormConfig.PATH_CHECKBOX_PREF_SIZE, FormConfig.PATH_CHECKBOX_MAX_SIZE)
                                .addComponent(variableNameField, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_PREF_SIZE, Short.MAX_VALUE)
                                .addComponent(deleteButton)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pathPartField)
                                .addComponent(isVariableCheckBox)
                                .addComponent(variableNameField)
                                .addComponent(deleteButton)));
    }

    private void changeVariable(ActionEvent evt) {
        if (isVariableCheckBox.isSelected()) {
            variableNameField.setEditable(true);
            variableNameField.setText(pathPart.getVariableName());
            pathPart.setTemplatized(true);
            HintHelper.setHintToTextField(UIHelper.ERROR_REQUIRED_FIELD, variableNameField);
        } else {
            HintHelper.removeHintFromField(variableNameField);
            variableNameField.setEditable(false);
            pathPart.setTemplatized(false);
            variableNameField.setText("");
        }
    }

    public PathPart getPathPartField() {
//        PathPart pathPart = new PathPart();
//        pathPart.setPathValue(this.pathPartField.getText());
//        pathPart.setTemplatized(isVariableCheckBox.isSelected());
//        pathPart.setVariableName(Rest2MobileConstants.START_TEMPLATE_VARIABLE + variableNameField.getText() + Rest2MobileConstants.END_TEMPLATE_VARIABLE);
        pathPart.setPathValue(this.pathPartField.getText());
        pathPart.setVariableName(variableNameField.getText());
        pathPart.setTemplatized(isVariableCheckBox.isSelected());
        return pathPart;
    }

    public void validateField() {
        variableNameField.setText(VerifyHelper.verifyVariableName(variableNameField.getText()));
    }

    public boolean checkRequirementField() {
        if (pathPartField.getText().trim().equalsIgnoreCase("")) {
            return false;
        }

        if (isVariableCheckBox.isSelected()) {
            if (variableNameField.getText().trim().equalsIgnoreCase("")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Callback interface to handle events in this panel
     */
    public interface PathParamCallBack {
        void deleted(PathPartPanel pathPanel);
        void updated(PathPartPanel pathPanel);
    }
}
