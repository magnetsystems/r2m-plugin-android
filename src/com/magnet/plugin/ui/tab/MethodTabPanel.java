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
import com.intellij.util.net.HTTPMethod;
import com.magnet.langpack.builder.rest.parser.RestExampleModel;
import com.magnet.plugin.api.core.RequestFactory;
import com.magnet.plugin.api.mock.WorkerCallback;
import com.magnet.plugin.api.models.ApiMethodModel;
import com.magnet.plugin.api.models.RequestModel;
import com.magnet.plugin.api.models.ResponseModel;
import com.magnet.plugin.api.requests.abs.BaseRequest;
import com.magnet.plugin.helpers.*;
import com.magnet.plugin.listeners.CreateMethodCallback;
import com.magnet.plugin.listeners.TabRemoveListener;
import com.magnet.plugin.messages.Rest2MobileMessages;
import com.magnet.plugin.models.Method;
import com.magnet.plugin.constants.FormConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.magnet.plugin.helpers.UIHelper.*;

public class MethodTabPanel extends BasePanel {

    private final MethodNameSection panel;
    private final MethodTypeSection type;
    private final HeaderSection header;
    private final RequestPayloadSection requestPayloadSection;
    private final ResponseSection responseSection;
    private final ButtonsSection buttons;

    private final CreateMethodCallback methodCallback;
    private final JTabbedPane tabPanel;

    private TabRemoveListener tabRemoveListener;
    private ApiMethodModel apiMethodModel = null;

    private int index = -1;


    {
        this.setOpaque(false);
        panel = new MethodNameSection();
        header = new HeaderSection();
        requestPayloadSection = new RequestPayloadSection();
        type = new MethodTypeSection(requestPayloadSection);
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

        responseSection.getPayloadField().getDocument().addDocumentListener(createMethodButtonUpdater);
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
                        .addComponent(requestPayloadSection)
                        .addComponent(responseSection)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createSequentialGroup()
                        .addComponent(panel)
                        .addComponent(type)
                        .addComponent(header)
                        .addComponent(requestPayloadSection)
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

        Method method = getMethod();
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
        ResponseModel responseModel = new ResponseModel(responseSection.getRawPayload());
        apiMethodModel.setResponseModel(responseModel);

        // create method file
        methodCallback.createMethod(apiMethodModel);
        return true;
    }

    private void testApi() {
        if (panel.checkRequirementFields()) {
            Method method = makeMethod();
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

    public MethodTabPanel(Project project, CreateMethodCallback methodCallback, JTabbedPane tabPanel) {
        this.methodCallback = methodCallback;
        this.tabPanel = tabPanel;
        panel.setProject(project);
    }

    public String getRequestPayload() {
        return requestPayloadSection.getPayload();
    }

    public String getResponse() {
        return responseSection.getRawPayload();
    }

    private MethodTabPanel getCurrentPanel() {
        return this;
    }

    public String getMethodName() {
        return panel.getMethodName();
    }

    public String getMethodTabName() {
        return panel.getMethodNamePanel().getText();
    }

    /**
     * @param url url where path param are expanded (removed "{""}")
     * @return expanded url
     */
    private static String expandUrl(String url) {
        url = url.replaceAll(Rest2MobileConstants.START_TEMPLATE_VARIABLE_REGEX, "");
        url = url.replaceAll(Rest2MobileConstants.END_TEMPLATE_VARIABLE_REGEX, "");
        return url;
    }

    public Method makeMethod() {

        Method method = new Method();
        method.setMethodName(panel.getMethodName());
        method.setUrl(panel.getUrl());
        method.setPathParts(panel.getPaths());
        method.setQueries(panel.getQueries());
        method.setHttpMethod(type.getHttpMethod());
        method.setHeaders(header.getHeaders());
        method.setResponse(responseSection.getRawPayload());

        String payload = getRequestPayload();
        method.setPayload(payload);

        return method;
    }

    public Method getMethod() {
        Method method = makeMethod();
        method.setUrl(panel.getTemplateUrl());
        return method;
    }

    private WorkerCallback<ApiMethodModel> callback = new WorkerCallback<ApiMethodModel>() {
        @Override
        public void onSuccess(ApiMethodModel methodModel) {
            apiMethodModel = methodModel;
            String entity = ResponseHelper.processResponse(methodModel);
            MethodTabPanel.this.responseSection.setPayload(entity);
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
        boolean isResponseEmpty = responseSection.getRawPayload().isEmpty();
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
                tabPanel.setTitleAt(getIndex(), Rest2MobileMessages.getMessage(Rest2MobileMessages.METHOD_N, index + 1));
            }
        }
    }

    public void createMethodFromExample(RestExampleModel methodModel) {
        // method name
        String methodName = methodModel.getName();
        panel.getMethodNamePanel().setText(methodName);

        // verb
        String urlWithVerb = methodModel.getRequestUrl();
        String[] parts = urlWithVerb.split(" ");
        HTTPMethod verb = HTTPMethod.GET;
        if (parts.length > 1) {
            verb = HTTPMethod.valueOf(parts[0]);
        }
        type.selectVerb(verb);

        // URL
        String url = parts[parts.length - 1];
        panel.getUrlField().getEditor().setItem(expandUrl(url));

        // set paths
        List<String> pathParams = findPathVariables(url);
        if (pathParams.size() > 0) {

            // TODO: populate the paths
        }

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
        setSectionBody(responseSection, methodModel.getResponseBody());

    }

    /**
     * Find path variables in template url. Variables are identified by <code>{var}</code>
     * @param templateUrl template url
     * @return list of path param variable name in url
     */
    private static List<String> findPathVariables(String templateUrl) {
        Pattern p = Pattern.compile(Rest2MobileConstants.TEMPLATE_VARIABLE_REGEX);
        Matcher m = p.matcher(templateUrl);
        List<String> l = new ArrayList<String>();
        while (m.find()) {
            l.add(m.group().substring(1, m.group().length() - 1));
        }
        return l;
    }


    private static void setSectionBody(PayloadPanel payloadPanel, String body) {
        if (body != null && !body.isEmpty()) {
            payloadPanel.setEnabled(true);
            payloadPanel.setPayload(body);
        }

    }
}
