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

import com.magnet.plugin.common.helpers.VerifyHelper;
import com.magnet.plugin.common.ui.HintTextField;
import com.magnet.plugin.r2m.constants.FormConfig;
import com.magnet.plugin.r2m.helpers.R2MVerifyHelper;
import com.magnet.plugin.r2m.helpers.UIHelper;
import com.magnet.plugin.r2m.messages.R2MMessages;
import com.magnet.plugin.r2m.models.PathPart;
import com.magnet.plugin.r2m.ui.AbstractDocumentListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.magnet.plugin.r2m.helpers.UIHelper.ERROR_REQUIRED_FIELD;

public class PathPartPanel extends JPanel {

    private final HintTextField pathPartField;
    private final JCheckBox isVariableCheckBox;
    private final HintTextField variableNameField;
    private final JButton deleteButton;

    private final PathParamCallBack callBack;

    private final PathPart pathPart;

    public PathPartPanel(PathParamCallBack callBack, PathPart pathPart) {
        this.callBack = callBack;
        this.pathPart = pathPart;
        variableNameField.setText(pathPart.getVariableName());
        isVariableCheckBox.setSelected(pathPart.isTemplatized());
        this.pathPartField.setText(pathPart.getValue());
    }


    {
        Font font = UIHelper.getFont();
        pathPartField = new HintTextField("");
        pathPartField.setFont(font);
        pathPartField.getDocument().addDocumentListener(new AbstractDocumentListener() {

            @Override
            protected void doUpdate() {
                pathPart.setValue(pathPartField.getText());
                callBack.updated(PathPartPanel.this);
            }
        });

        isVariableCheckBox = new JCheckBox();
        isVariableCheckBox.setToolTipText(R2MMessages.getMessage("VARIABLE_CHECKBOX_TOOL_TIP"));
        isVariableCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                changeVariable(evt);
            }
        });

        variableNameField = new HintTextField("");
        variableNameField.setToolTipText(R2MMessages.getMessage("VARIABLE_NAME_TEXT_FIELD_TOOL_TIP"));
        variableNameField.getDocument().addDocumentListener(new AbstractDocumentListener() {

            @Override
            protected void doUpdate() {
                pathPart.setVariableName(variableNameField.getText());
                callBack.updated(PathPartPanel.this);
            }
        });

        deleteButton = new JButton(R2MMessages.getMessage("SECTION_DELETE"));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                callBack.deleted(PathPartPanel.this);
            }
        });

        isVariableCheckBox.setFont(font);
        variableNameField.setFont(font);

        deleteButton.setFont(font);

        pathPartField.setHint(ERROR_REQUIRED_FIELD);

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
            variableNameField.setHint(UIHelper.ERROR_REQUIRED_FIELD);
        } else {
            variableNameField.setHint("");
            variableNameField.setEditable(false);
            pathPart.setTemplatized(false);
            variableNameField.setText("");
        }
    }

    public PathPart getPathPartField() {
//        PathPart pathPart = new PathPart();
//        pathPart.setValue(this.pathPartField.getText());
//        pathPart.setTemplatized(isVariableCheckBox.isSelected());
//        pathPart.setVariableName(Rest2MobileConstants.START_TEMPLATE_VARIABLE + variableNameField.getText() + Rest2MobileConstants.END_TEMPLATE_VARIABLE);
        pathPart.setValue(this.pathPartField.getText());
        pathPart.setVariableName(variableNameField.getText());
        pathPart.setTemplatized(isVariableCheckBox.isSelected());
        return pathPart;
    }

    public void validateField() {
        variableNameField.setText(R2MVerifyHelper.verifyVariableName(variableNameField.getText()));
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
