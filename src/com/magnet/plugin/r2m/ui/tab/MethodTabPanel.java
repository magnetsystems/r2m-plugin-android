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
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.net.HTTPMethod;
import com.magnet.langpack.builder.rest.parser.RestExampleModel;
import com.magnet.plugin.common.Logger;
import com.magnet.plugin.common.helpers.VerifyHelper;
import com.magnet.plugin.r2m.api.core.RequestFactory;
import com.magnet.plugin.r2m.api.mock.WorkerCallback;
import com.magnet.plugin.r2m.api.models.ApiMethodModel;
import com.magnet.plugin.r2m.api.models.RequestModel;
import com.magnet.plugin.r2m.api.models.ResponseModel;
import com.magnet.plugin.r2m.api.requests.abs.BaseRequest;
import com.magnet.plugin.r2m.helpers.*;
import com.magnet.plugin.r2m.listeners.CreateMethodCallback;
import com.magnet.plugin.r2m.listeners.TabRemoveListener;
import com.magnet.plugin.r2m.messages.R2MMessages;
import com.magnet.plugin.r2m.models.Method;
import com.magnet.plugin.r2m.constants.FormConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import static com.magnet.plugin.r2m.helpers.UIHelper.*;

public class MethodTabPanel extends BasePanel {

    private final MethodNameSection methodNameSection;
    private final MethodTypeSection type;
    private final HeaderSection header;
    private final RequestPayloadSection requestPayloadSection;
    private final ResponsePayloadSection responsePayloadSection;
    private final ButtonsSection buttons;

    private final CreateMethodCallback methodCallback;
    private final JTabbedPane tabPanel;

    private TabRemoveListener tabRemoveListener;
    private ApiMethodModel apiMethodModel = null;

    private int index = -1;


    {
        this.setOpaque(false);
        methodNameSection = new MethodNameSection();
        header = new HeaderSection();
        requestPayloadSection = new RequestPayloadSection();
        type = new MethodTypeSection(requestPayloadSection);
        responsePayloadSection = new ResponsePayloadSection();
        buttons = new ButtonsSection();

        JScrollPane jScrollPane = new JBScrollPane();
        JPanel jPanel1 = new JPanel();

        methodNameSection.getMethodNamePanel().getDocument().addDocumentListener(new DocumentListener() {

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
                        .addComponent(methodNameSection)
                        .addComponent(type)
                        .addComponent(header)
                        .addComponent(requestPayloadSection)
                        .addComponent(responsePayloadSection)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createSequentialGroup()
                        .addComponent(methodNameSection)
                        .addComponent(type)
                        .addComponent(header)
                        .addComponent(requestPayloadSection)
                        .addComponent(responsePayloadSection));
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

        Method method = getMethod();
        if (!VerifyHelper.isValidUrl(method.getTestUrl())) {
            showErrorMessage(R2MMessages.getMessage("PROVIDE_VALID_URL", method.getMethodName(), method.getTestUrl()));
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
        ResponseModel responseModel = new ResponseModel(responsePayloadSection.getRawPayload());
        apiMethodModel.setResponseModel(responseModel);

        // create method file
        methodCallback.createMethod(apiMethodModel);
        return true;
    }

    private void testApi() {
        if (methodNameSection.checkRequirementFields()) {
            Method method = makeMethod();
            if (!VerifyHelper.isValidUrlWithoutPerformance(method.getTestUrl())) {
                showErrorMessage(R2MMessages.getMessage("PROVIDE_VALID_URL", method.getMethodName(), method.getTestUrl()));
            }
            RequestModel requestModel = new RequestModel(method);
            BaseRequest request = RequestFactory.getRequestForMethod(callback, requestModel);
            request.execute();
            System.out.println(method.toString());
        } else {
            UIHelper.showErrorMessage(ERROR_FILL_REQUIRED_FIELD);
        }
    }

    public MethodTabPanel(Project project, CreateMethodCallback methodCallback, JTabbedPane tabPanel) {
        this.methodCallback = methodCallback;
        this.tabPanel = tabPanel;
        methodNameSection.setProject(project);
    }

    public String getRequestPayload() {
        return requestPayloadSection.getPayload();
    }

    public String getResponse() {
        return responsePayloadSection.getRawPayload();
    }

    private MethodTabPanel getCurrentPanel() {
        return this;
    }

    public String getMethodName() {
        return methodNameSection.getMethodName();
    }

    public String getMethodTabName() {
        return methodNameSection.getMethodNamePanel().getText();
    }

    public Method makeMethod() {

        Method method = new Method();
        method.setMethodName(methodNameSection.getMethodName());
        method.setTestUrl(methodNameSection.getUrl());
        method.setPathParts(methodNameSection.getPaths());
        method.setQueries(methodNameSection.getQueries());
        method.setHttpMethod(type.getHttpMethod());
        method.setHeaders(header.getHeaders());
        method.setResponse(responsePayloadSection.getRawPayload());

        String payload = getRequestPayload();
        method.setPayload(payload);

        return method;
    }

    public Method getMethod() {
        Method method = makeMethod();
        method.setTestUrl(methodNameSection.getUrl());
        method.setTemplateUrl(methodNameSection.getTemplateUrl());
        return method;
    }

    private WorkerCallback<ApiMethodModel> callback = new WorkerCallback<ApiMethodModel>() {
        @Override
        public void onSuccess(ApiMethodModel methodModel) {
            apiMethodModel = methodModel;
            String entity = ResponseHelper.processResponse(methodModel);
            MethodTabPanel.this.responsePayloadSection.setPayload(entity);
        }

        @Override
        public void onError(Exception e) {
            Logger.error(getClass(), e.toString());
        }

        @Override
        public void onError(com.magnet.plugin.r2m.api.models.Error error) {
            Logger.error(getClass(), "onError");
        }
    };

    public void setTabRemoveListener(TabRemoveListener tabRemoveListener) {
        this.tabRemoveListener = tabRemoveListener;
    }


    private void showErrorMessage(String message) {
        UIHelper.showErrorMessage(message);
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
                tabPanel.setTitleAt(getIndex(), R2MMessages.getMessage("METHOD_N", index + 1));
            }
        }
    }

    public void createMethodFromExample(RestExampleModel methodModel) {
        // method name
        String methodName = methodModel.getName();
        methodNameSection.getMethodNamePanel().setText(methodName);

        // set verb
        String urlWithVerb = methodModel.getRequestUrl();
        String[] parts = urlWithVerb.split(" ");
        HTTPMethod verb = HTTPMethod.GET;
        if (parts.length > 1) {
            verb = HTTPMethod.valueOf(parts[0]);
        }
        type.selectVerb(verb);

        // set url details (paths/queries
        String templatizedUrl = parts[parts.length - 1];
        methodNameSection.populateUrlDetails(templatizedUrl);

        // Request Headers
        Map<String, String> headers = methodModel.getRequestHeaders();
        if (headers != null && !headers.isEmpty()) {
            header.setEnabled(true);
            for (Map.Entry<String, String> e : headers.entrySet()) {
                header.addHeader(e.getKey(), e.getValue());
            }
        }
        // Request body
        setSectionBody(requestPayloadSection, methodModel.getRequestBody());
        // Response body
        setSectionBody(responsePayloadSection, methodModel.getResponseBody());

    }


    private static void setSectionBody(PayloadPanel payloadPanel, String body) {
        if (body != null && !body.isEmpty()) {
            payloadPanel.setEnabled(true);
            payloadPanel.setPayload(body);
        }

    }
}
