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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.magnet.plugin.r2m.constants.FormConfig;
import com.magnet.plugin.r2m.constants.PluginIcon;
import com.magnet.plugin.r2m.helpers.HistoryHelper;
import com.magnet.plugin.r2m.helpers.R2MVerifyHelper;
import com.magnet.plugin.r2m.listeners.URLFocusListener;
import com.magnet.plugin.r2m.messages.R2MMessages;
import com.magnet.plugin.r2m.models.PathPart;
import com.magnet.plugin.r2m.models.Query;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

public class MethodNameSection extends BasePanel implements URLFocusListener, PluginIcon {

    private final JTextField methodName;
    private final JCheckBox urlDetailsBox;

    private final ComboBox urlField;
    private final URLSection urlSection;

    {

        JLabel methodNameLabel = new JLabel();
        methodName = new JTextField();
        methodName.setToolTipText(R2MMessages.getMessage("METHOD_NAME_TOOL_TIP"));
        methodName.setColumns(1);
        urlDetailsBox = new JCheckBox();
        urlDetailsBox.setToolTipText(R2MMessages.getMessage("EXPAND_URL_CHECKBOX_TOOL_TIP"));
        JLabel urlLabel = new JLabel();


        urlField = new ComboBox();
        urlField.setEditable(true);
        urlField.setToolTipText(R2MMessages.getMessage("URL_FIELD_TOOL_TIP"));

        JTextField textField = getComboBoxEditor();
        textField.setColumns(1);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                urlSection.setUrl((getComboBoxEditor()).getText());
                urlSection.revalidateSection();
            }
        });


        Border border = UIManager.getBorder("Button.border");
        urlDetailsBox.setBorder(border);

        urlSection = new URLSection();
        urlSection.setVisible(false);
        urlSection.setFocusListener(this);

        methodNameLabel.setText(R2MMessages.getMessage("METHOD_NAME_LABEL_TEXT"));

        urlLabel.setText(R2MMessages.getMessage("URL_LABEL_TEXT"));

        methodName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                methodName.setText(R2MVerifyHelper.verifyMethodName(methodName.getText()));
            }
        });

        methodNameLabel.setFont(baseFont);
        urlLabel.setFont(baseFont);
        methodName.setFont(baseFont);
        urlField.setFont(baseFont);


        urlDetailsBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                showOrHideUrlSection(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(FormConfig.CUSTOM_GAP)
                        .addComponent(methodNameLabel, GroupLayout.Alignment.TRAILING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(urlDetailsBox).addComponent(urlLabel)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(methodName, GroupLayout.DEFAULT_SIZE, FormConfig.DEFAULT_COMPONENT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(urlField, GroupLayout.DEFAULT_SIZE, FormConfig.DEFAULT_COMPONENT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(urlSection))));

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(methodNameLabel)
                                .addComponent(methodName))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(urlDetailsBox)
                                .addComponent(urlLabel)
                                .addComponent(urlField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(urlSection)));

        urlDetailsBox.setIcon(closeIcon);
        urlDetailsBox.setSelectedIcon(openIcon);
        urlDetailsBox.setPressedIcon(getSelectedIcon(urlDetailsBox));
    }

    public void setProject(Project project) {
        urlField.setModel(new DefaultComboBoxModel(HistoryHelper.getSavedUrls(project)));
        urlField.setPrototypeDisplayValue("");
//        urlField.setMinimumAndPreferredWidth(15);
    }


    /**
     * Triggered when clicking on the expand url check box
     *
     * @param evt unused
     */
    private void showOrHideUrlSection(@SuppressWarnings("unused") ActionEvent evt) {
        String u = getComboBoxEditor().getText();
//        if (urlDetailsBox.isSelected()) {
//            if (!VerifyHelper.isValidUrl(u)) {
//                UIHelper.showErrorMessage(Rest2MobileMessages.getMessage(Rest2MobileMessages.PROVIDE_VALID_URL, getMethodName(), u));
//                return;
//            }
//        }
        urlDetailsBox.setPressedIcon(getSelectedIcon(urlDetailsBox));
        if (urlDetailsBox.isSelected()) {
            getComboBoxEditor().setEditable(false);
            urlSection.setUrl(u);
            urlSection.setVisible(true);
        } else {
            urlSection.setVisible(false);
            getComboBoxEditor().setEditable(true);
        }
    }

    public String getMethodName() {
        return methodName.getText();
    }

    public JTextField getMethodNamePanel() {
        return methodName;
    }

    public String getUrl() {
        String text = "";
        if (!getComboBoxEditor().getText().trim().isEmpty()) {
            text = getComboBoxEditor().getText();
        }
        return text;
    }

    public String getTemplateUrl() {
        if (urlDetailsBox.isSelected()) {
            return urlSection.getTemplateUrl();
        }
        String text = "";
        if (!getComboBoxEditor().getText().trim().isEmpty()) {
            text = getComboBoxEditor().getText();
        }
        return text;
    }

    public List<PathPart> getPaths() {
        List<PathPart> pathPartList = new ArrayList<PathPart>();
        for (PathPartPanel panel : urlSection.getPathPanels()) {
            pathPartList.add(panel.getPathPartField());
        }

        return pathPartList;
    }

    public List<Query> getQueries() {
        return urlSection.getQueryPanels();
    }

    public boolean checkRequirementFields() {
        return urlSection.checkRequirementFields();
    }

    @Override
    public void onFocusChange(String url) {
        if (null == url) {
            url = "";
        }
        url = url.trim();
        if (!url.isEmpty()) {
            getComboBoxEditor().setText(url);
        }
    }

    public JComboBox getUrlField() {
        return urlField;
    }

    private JTextField getComboBoxEditor() {
        return (JTextField) urlField.getEditor().getEditorComponent();
    }

    private Icon getSelectedIcon(JCheckBox checkBox) {
        if (checkBox.isSelected()) {
            return openIconPressed;
        } else {
            return closeIconPressed;
        }
    }

    /**
     * Populate the url section with url details given a templatized urls
     *
     * @param templatizedUrl templatized url ,with path param variables (if any), e.g http://host.com/a/b/c/{id}/{id2}
     */
    public void populateUrlDetails(String templatizedUrl) {
        urlSection.setUrl(templatizedUrl);

        urlField.getEditor().setItem(urlSection.getParsedUrl().buildUrl(false));

        // expand url details if there are path params
        if (urlSection.getParsedUrl().hasPathParams()) {
            urlDetailsBox.setSelected(true);
            urlDetailsBox.setPressedIcon(getSelectedIcon(urlDetailsBox));
            urlSection.setVisible(true);

        }
    }

}
