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
import com.intellij.ui.components.JBScrollPane;
import com.magnet.plugin.api.core.RequestFactory;
import com.magnet.plugin.api.mock.WorkerCallback;
import com.magnet.plugin.api.models.ApiMethodModel;
import com.magnet.plugin.api.models.RequestModel;
import com.magnet.plugin.api.models.ResponseModel;
import com.magnet.plugin.api.requests.abs.BaseRequest;
import com.magnet.plugin.helpers.Logger;
import com.magnet.plugin.helpers.ResponseHelper;
import com.magnet.plugin.helpers.UIHelper;
import com.magnet.plugin.helpers.VerifyHelper;
import com.magnet.plugin.listeners.CreateMethodCallback;
import com.magnet.plugin.listeners.TabRemoveListener;
import com.magnet.plugin.models.Method;
import com.magnet.plugin.constants.FormConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.magnet.plugin.helpers.UIHelper.*;

public class MainPanel extends BasePanel {

    private MethodNameSection panel;
    private MethodTypeSection type;
    private HeaderSection header;
    private RequestPayloadSection payload;
    private ResponseSection responseSection;
    private ButtonsSection buttons;

    private CreateMethodCallback methodCallback;
    private TabRemoveListener tabRemoveListener;
    private JTabbedPane tabPanel;

    private ApiMethodModel apiMethodModel = null;

    private int index = -1;


    {
        this.setOpaque(false);
        panel = new MethodNameSection();
        header = new HeaderSection();
        payload = new RequestPayloadSection();
        type = new MethodTypeSection(payload);
        responseSection = new ResponseSection();
        buttons = new ButtonsSection();

        JScrollPane jScrollPane = new JBScrollPane();
        JPanel jPanel1 = new JPanel();


        DocumentListener createMethodButtonUpdater = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCreateMethodButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCreateMethodButton();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCreateMethodButton();
            }
        };

        responseSection.getJsonField().getDocument().addDocumentListener(createMethodButtonUpdater);
        ((JTextField) (panel.getUrlField().getEditor().getEditorComponent())).getDocument().addDocumentListener(createMethodButtonUpdater);

        panel.getMethodNamePanel().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                setMethodTitle();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setMethodTitle();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setMethodTitle();
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panel)
                        .addComponent(type)
                        .addComponent(header)
                        .addComponent(payload)
                        .addComponent(responseSection)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createSequentialGroup()
                        .addComponent(panel)
                        .addComponent(type)
                        .addComponent(header)
                        .addComponent(payload)
                        .addComponent(responseSection));
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setViewportView(jPanel1);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, FormConfig.CUSTOM_SCROLL_SIZE, Short.MAX_VALUE)
                        .addComponent(buttons, GroupLayout.Alignment.CENTER)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane)
                        .addComponent(buttons)
                        .addContainerGap()
        );

        buttons.getTestApiButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                testApi();
            }
        });
        buttons.getCreateMethodButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMethod();
//                buttons.getCreateMethodButton().setEnabled(false);
            }
        });

        buttons.getDeletePanelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (tabRemoveListener != null) {
                    tabRemoveListener.removeCurrentPanel(getCurrentPanel());
                }
            }
        });
    }

    public boolean createMethod() {

        Method method = getMethodToFile();
        if (!VerifyHelper.isValidUrl(method.getUrl())) {
            showErrorMessage(ERROR_INVALID_URL);
            return false;
        }
        if (getMethodName().isEmpty()) {
            showErrorMessage(ERROR_METHOD_NAME);
            return false;
        }
        if (apiMethodModel == null) {
            apiMethodModel = new ApiMethodModel();
            RequestModel requestModel = new RequestModel(method);
            apiMethodModel.setRequestModel(requestModel);
        } else {
            RequestModel requestModel = new RequestModel(method);
            apiMethodModel.setRequestModel(requestModel);
        }
        ResponseModel responseModel = new ResponseModel(responseSection.getUnformattedJson());
        apiMethodModel.setResponseModel(responseModel);
        methodCallback.createMethod(apiMethodModel);
        return true;
    }

    private void testApi() {
        if (panel.checkRequirementFields()) {
            Method method = getMethod();
            if (!VerifyHelper.isValidUrlWithoutPerformance(method.getUrl())) {
                showErrorMessage(ERROR_INVALID_URL);
            }
            RequestModel requestModel = new RequestModel(method);
            BaseRequest request = RequestFactory.getRequestForMethod(callback, requestModel);
            request.execute();
            System.out.println(method.toString());
        } else {
            UIHelper.showErrorMessage(ERROR_FILL_REQUIRED_FIELD);
        }
    }

    public MainPanel(Project project, CreateMethodCallback methodCallback, JTabbedPane tabPanel) {
        this.methodCallback = methodCallback;
        this.tabPanel = tabPanel;
        panel.setProject(project);
    }

    public String getPayload() {
        return payload.getJson();
    }

    public String getResponse() {
        return responseSection.getUnformattedJson();
    }

    private MainPanel getCurrentPanel() {
        return this;
    }

    public String getMethodName() {
        return panel.getMethodName();
    }

    public String getMethodTabName() {
        return panel.getMethodNamePanel().getText();
    }

    public Method getMethod() {

        Method method = new Method();
        method.setMethodName(panel.getMethodName());
        method.setUrl(panel.getUrl());
        method.setPaths(panel.getPaths());
        method.setQueries(panel.getQueries());
        method.setHttpMethod(type.getHttpMethod());
        method.setHeaders(header.getHeaders());
        method.setResponse(responseSection.getUnformattedJson());

        String payload = getPayload();
        method.setPayload(payload);

        return method;
    }

    public Method getMethodToFile() {
        Method method = getMethod();
        method.setUrl(panel.getUrlToFile());
        return method;
    }

    private WorkerCallback<ApiMethodModel> callback = new WorkerCallback<ApiMethodModel>() {
        @Override
        public void onSuccess(ApiMethodModel methodModel) {
            apiMethodModel = methodModel;
            String entity = ResponseHelper.processResponse(methodModel);
            MainPanel.this.responseSection.setJson(entity);
            buttons.getCreateMethodButton().setEnabled(true);
        }

        @Override
        public void onError(Exception e) {
            Logger.error(getClass(), e.toString());
        }

        @Override
        public void onError(com.magnet.plugin.api.models.Error error) {
            Logger.error(getClass(), "onError");
        }
    };

    public void setTabRemoveListener(TabRemoveListener tabRemoveListener) {
        this.tabRemoveListener = tabRemoveListener;
    }


    private void showErrorMessage(String message) {
        UIHelper.showErrorMessage(message);
    }

    private void updateCreateMethodButton() {
        boolean isResponseEmpty = responseSection.getUnformattedJson().isEmpty();
        boolean isUrlEmpty = panel.getUrl().isEmpty();
        boolean needEnable = !isResponseEmpty && !isUrlEmpty;
        buttons.enableCreateMethodButton(needEnable);
    }

    public void enableRemoveButton(boolean needEnable) {
        buttons.getDeletePanelButton().setEnabled(needEnable);
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        setMethodTitle();
    }

    private void setMethodTitle() {
        if (tabPanel.getTabCount() > 0) {
            if (!getMethodTabName().isEmpty()) {
                tabPanel.setTitleAt(getIndex(), getMethodTabName());
            } else {
                tabPanel.setTitleAt(getIndex(), "Method " + (index + 1));
            }
        }
    }
    public MethodNameSection getPanel() {
        return panel;
    }


}
