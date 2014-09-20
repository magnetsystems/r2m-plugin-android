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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.magnet.plugin.helpers.HistoryHelper;
import com.magnet.plugin.helpers.QueryParser;
import com.magnet.plugin.helpers.VerifyHelper;
import com.magnet.plugin.listeners.URLFocusListener;
import com.magnet.plugin.models.ParsedUrl;
import com.magnet.plugin.models.Path;
import com.magnet.plugin.models.Query;
import com.magnet.plugin.constants.FormConfig;
import com.magnet.plugin.constants.PluginIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

public class MethodNameSection extends BasePanel implements URLFocusListener, PluginIcon {

    private JLabel jLabel1;
    private JTextField methodName;
    private JCheckBox advancedBox;
    private JLabel jLabel2;

    private ComboBox urlField;


    private URLSection urlSection;

    public void setProject(Project project) {
        urlField.setModel(new DefaultComboBoxModel(HistoryHelper.getSavedUrls(project)));
        urlField.setPrototypeDisplayValue("");
//        urlField.setMinimumAndPreferredWidth(15);
    }


    {

        jLabel1 = new JLabel();
        methodName = new JTextField();
        methodName.setColumns(1);
        advancedBox = new JCheckBox();
        jLabel2 = new JLabel();


        urlField = new ComboBox();
        urlField.setEditable(true);

        JTextField textField = getComboBoxEditor();
        textField.setColumns(1);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                ParsedUrl parsedUrl = QueryParser.parseQuerry((getComboBoxEditor()).getText());
                urlSection.setParsedQuery(parsedUrl);
                urlSection.revalidateSection();
            }
        });



        Border border = UIManager.getBorder("Button.border");
        advancedBox.setBorder(border);

        urlSection = new URLSection();
        urlSection.setVisible(false);
        urlSection.setFocusListener(this);

        jLabel1.setText("Method Name");

        jLabel2.setText("URL");

        methodName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                methodName.setText(VerifyHelper.verifyMethodName(methodName.getText()));
            }
        });

        jLabel1.setFont(baseFont);
        jLabel2.setFont(baseFont);
        methodName.setFont(baseFont);
        urlField.setFont(baseFont);


        advancedBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                showHideUrlSection(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(FormConfig.CUSTOM_GAP)
                        .addComponent(jLabel1, GroupLayout.Alignment.TRAILING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(advancedBox).addComponent(jLabel2)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(methodName, GroupLayout.DEFAULT_SIZE, FormConfig.DEFAULT_COMPONENT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(urlField, GroupLayout.DEFAULT_SIZE, FormConfig.DEFAULT_COMPONENT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(urlSection))));

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(methodName))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(advancedBox)
                                .addComponent(jLabel2)
                                .addComponent(urlField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(urlSection)));

        advancedBox.setIcon(closeIcon);
        advancedBox.setSelectedIcon(openIcon);
        advancedBox.setPressedIcon(getSelectedIcon(advancedBox));
    }

    private void showHideUrlSection(ActionEvent evt) {
        advancedBox.setPressedIcon(getSelectedIcon(advancedBox));
        if (advancedBox.isSelected()) {
//            advancedBox.setIcon(openIcon);
            ParsedUrl parsedUrl = QueryParser.parseQuerry(getComboBoxEditor().getText());
            urlSection.setParsedQuery(parsedUrl);
            urlSection.setVisible(true);
        } else {
//            advancedBox.setIcon(closeIcon);
            urlSection.setVisible(false);
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

    public String getUrlToFile() {
        if (advancedBox.isSelected()) {
            return urlSection.getAdvancedUrlToFile();
        }
        String text = "";
        if (!getComboBoxEditor().getText().trim().isEmpty()) {
            text = getComboBoxEditor().getText();
        }
        return text;
    }

    public List<Path> getPaths() {
        List<Path> pathList = new ArrayList<Path>();
        for (PathPanel panel : urlSection.getPathPanels()) {
            pathList.add(panel.getPath());
        }

        return pathList;
    }

    public List<Query> getQueries() {
        return urlSection.getQueryPanels();
    }

    public boolean checkRequirementFields() {
        return urlSection.checkRequirementFields();
    }

    @Override
    public void onFocusChange(String url) {
        url = url.trim();
        if (!url.isEmpty()) {
            getComboBoxEditor().setText(url);
        }
    }

    public JComboBox getUrlField() {
        return urlField;
    }

    private List<String> getURLs() {
        List<String> urls = new ArrayList<String>();
        for (int i = 0; i < urlField.getItemCount(); i++) {
            urls.add(urlField.getItemAt(i).toString());
        }
        return urls;
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

}
