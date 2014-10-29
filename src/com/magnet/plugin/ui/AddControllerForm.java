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

package com.magnet.plugin.ui;

import com.intellij.ide.SaveAndSyncHandlerImpl;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ex.ProjectManagerEx;
import com.intellij.openapi.ui.FrameWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.TextFieldWithHistoryWithBrowseButton;
import com.magnet.langpack.builder.rest.parser.RestExampleModel;
import com.magnet.langpack.builder.rest.parser.validation.BodyValidationResult;
import com.magnet.plugin.api.models.ApiMethodModel;
import com.magnet.plugin.constants.FormConfig;
import com.magnet.plugin.generator.Generator;
import com.magnet.plugin.helpers.*;
import com.magnet.plugin.listeners.ControllerActionCallback;
import com.magnet.plugin.listeners.CreateMethodCallback;
import com.magnet.plugin.listeners.generator.PostGenerateCallback;
import com.magnet.plugin.messages.Rest2MobileMessages;
import com.magnet.plugin.project.CacheManager;
import com.magnet.plugin.project.ProjectManager;
import com.magnet.plugin.ui.chooser.ExampleChooserHelper;
import com.magnet.plugin.ui.tab.MethodTabPanel;
import com.magnet.plugin.ui.tab.TabManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.magnet.plugin.helpers.UIHelper.*;

public class AddControllerForm extends FrameWrapper implements CreateMethodCallback, PostGenerateCallback {
    private JPanel contentPane;
    private TextFieldWithHistoryWithBrowseButton controllerNameBox;
    private JButton generateButton;
    private JTabbedPane tabPanel;
    private JTextField packageNameField;
    private ControllerActionCallback actionCallback;
    private final TabManager tabManager;


    private final Project project;

    private final AnActionEvent anAction;

    public AddControllerForm(final Project project, AnActionEvent anAction, boolean canBeParent, ControllerActionCallback actionCallback) {
//        super(project, canBeParent);
        super(project);

        this.actionCallback = actionCallback;
        this.anAction = anAction;
        this.project = project;

        Font font = UIHelper.getFont();
        controllerNameBox.getChildComponent().setModel(new DefaultComboBoxModel(ControllerHistoryManager.getCachedControllers(project)));
        controllerNameBox.setFocusable(true);
        controllerNameBox.setFont(font);
        generateButton.setFont(font);
        packageNameField.setFont(font);


//        setResizable(true);
        setTitle(Rest2MobileMessages.getMessage(Rest2MobileMessages.WINDOW_TITLE));
        packageNameField.setText(ProjectManager.getPackageName(project));

        generateButton.addActionListener(generateListener);
        generateButton.setEnabled(true);


        tabManager = new TabManager(project, this, tabPanel);
        ControllerNameBoxItemListener controllerNameBoxListener = new ControllerNameBoxItemListener(project, this);
        controllerNameBox.getChildComponent().addActionListener(controllerNameBoxListener);
        controllerNameBox.getChildComponent().getEditor().getEditorComponent().addFocusListener(controllerNameBoxListener);
        controllerNameBox.addActionListener(browseListener);
        setDefaultParameters();
    }

    private void setDefaultParameters() {
        contentPane.setSize(FormConfig.SCREEN_DIMENSION);
        contentPane.setMinimumSize(FormConfig.SCREEN_DIMENSION);
        setComponent(contentPane);
        getThis().setSize(FormConfig.SCREEN_DIMENSION);
        getThis().setMinimumSize(FormConfig.SCREEN_DIMENSION);
        getThis().setPreferredSize(FormConfig.SCREEN_DIMENSION);
        getThis().setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
    }

    public Window getThis() {
        return this.getFrame();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    @Override
    public void createMethod(ApiMethodModel methodModel) {
        String controllerName = getControllerName();
        if (methodModel == null) {
            showErrorMessage(ERROR_TEST_API);
        } else if (controllerName.isEmpty()) {
            showErrorMessage(ERROR_SERVICE_NAME);
        } else {
            getGenerator().createMethodFile(methodModel);
            HistoryHelper.saveUrl(project, methodModel.getRequestModel().getTestUrl());
        }
    }

    @Override
    public void canGenerate(boolean canGenerate) {
        generateButton.setEnabled(true);
    }


    public TextFieldWithHistoryWithBrowseButton getControllerNameBox() {
        return controllerNameBox;
    }

    public JTextField getPackageNameField() {
        return packageNameField;
    }

    public String getControllerName() {
        Object item = this.controllerNameBox.getChildComponent().getEditor().getItem();
        if (item == null) {
            return null;
        }
        return item.toString().trim();
    }

    private String getPackageName() {
        return this.packageNameField.getText().trim();
    }


    private void showErrorMessage(String message) {
        UIHelper.showErrorMessage(message);
    }

    /*
    =======================LISTENERS SECTION=========================
     */

    /**
     * Listener triggered upon clicking on browse button
     */
    private final ActionListener browseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (ExampleChooserHelper.isExamplesDialogUp()) {
                return;
            }
            String controllerName = ExampleChooserHelper.showExamplesDialog();
            if (null == controllerName) {
                return;
            }

            controllerName = controllerName.trim();

            List<RestExampleModel> methods = ExampleChooserHelper.getControllersMethodsByName(controllerName);
            if (methods != null && !methods.isEmpty()) {
                populateMethods(VerifyHelper.verifyClassName(controllerName), "com.magnetapi.examples", methods);
                return;
            }
            // check for file
            try {
                File file = URLHelper.getFileFromURL(controllerName);
                methods = ExampleChooserHelper.getControllersMethodsByFile(file);
                populateMethods("", "", methods);

            } catch (Exception e) {
                Messages.showErrorDialog("Resource " + controllerName + " cannot be found", "Error");
            }

        }
    };

    /**
     * Listener triggered upon "Generate"
     */
    private final ActionListener generateListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (checkAllRequirements()) {
                performGeneration();
            }
        }

        /**
         * Validate the rest controller wizard before generation
         * @return true if validated, false otherwise
         */
        private boolean checkAllRequirements() {
            return checkAllMethodNames() && checkControllerName() && checkMainPanels() && checkNoRemainingCachedMethods() && checkPayload();
        }

        private boolean checkPayload() {
            boolean result = true;
            List<MethodTabPanel> tabs = tabManager.getTabs();
            for (int i = 0; i < tabs.size(); i++) {
                // Revalidate the payload before generation
                MethodTabPanel methodTabPanel = tabs.get(i);

                BodyValidationResult requestValidationResult = JSONValidator.validateBody(methodTabPanel.getRequestPayload());
                BodyValidationResult responseValidationResult = JSONValidator.validateBody(methodTabPanel.getResponse());
                if (!requestValidationResult.isValid() || !responseValidationResult.isValid()) {
                    if (ifContinue(methodTabPanel.getMethodName(), !requestValidationResult.isValid() ? requestValidationResult : responseValidationResult)) {
                        continue;
                    } else {
                        result = false;
                        tabManager.selectTab(i);
                        break;
                    }
                }
            }
            return result;
        }

        private boolean ifContinue(String methodName, BodyValidationResult validationResult) {
            int okCancelResult = Messages.showOkCancelDialog(contentPane, Rest2MobileMessages.getMessage(Rest2MobileMessages.VALIDATION_WARNING_QUESTION, methodName) + "\n" + JSONValidator.getErrorMessage(validationResult.getErrors()),
                    Rest2MobileMessages.getMessage(Rest2MobileMessages.VALIDATION_WARNING_TITLE, methodName),
                    Rest2MobileMessages.getMessage(Rest2MobileMessages.VALIDATION_WARNING_CONTINUE),
                    Rest2MobileMessages.getMessage(Rest2MobileMessages.VALIDATION_WARNING_CANCEL),
                    null);
            boolean result = okCancelResult == 0;

            return result;
        }

        /**
         * Check that the methods being created do not conflicts with existing methods already configured in the
         * cache director returned byt {@link com.magnet.plugin.project.CacheManager#getControllerExamplesFolder()}
         * @return true if no conflicts, false otherwise
         */
        private boolean checkNoRemainingCachedMethods() {

            // methods from tabs
            Set<String> methodsToGenerate = new HashSet<String>();
            for (MethodTabPanel methodTabPanel : tabManager.getTabs()) {
                methodsToGenerate.add(methodTabPanel.getMethodTabName());
            }

            // methods from cache
            List<String> cachedMethods = new ArrayList<String>(getCacheManager().getControllerMethodNames());


            cachedMethods.removeAll(methodsToGenerate);

            if (cachedMethods.size() == 0) {
                return true;
            }

            for (String method : cachedMethods) {
                getCacheManager().clearControllerMethodCache(method);
            }

            return true;
        }

        /**
         * Check that there is no duplicate name in the method tabs.
         * @return true if no duplicate, false otherwise
         */
        private boolean checkAllMethodNames() {
            boolean result;
            Set<String> strings = new HashSet<String>();
            List<MethodTabPanel> tabs = tabManager.getTabs();
            for (MethodTabPanel methodTabPanel : tabs) {
                strings.add(methodTabPanel.getMethodTabName());
            }
            result = (strings.size() == tabs.size());
            if (!result) {
                UIHelper.showErrorMessage("Name of methods must be different!");
            }
            return result;
        }

        private boolean checkControllerName() {
            String name = getControllerName();

            if (name == null || name.isEmpty()) {
                showErrorMessage(ERROR_SERVICE_NAME);
                return false;
            } else if (packageNameField.getText().trim().isEmpty()) {
                showErrorMessage(ERROR_PACKAGE_NAME);
                return false;
            }
            return true;
        }

        private boolean checkMainPanels() {
            boolean result = true;
            for (MethodTabPanel tab : tabManager.getTabs()) {
                result = tab.createMethod();
                if (!result) {
                    break;
                }
            }
            return result;
        }

        private void performGeneration() {

            String controllerName = getControllerName();
            if (controllerName.isEmpty()) {
                showErrorMessage(ERROR_SERVICE_NAME);
            } else {
                generateButton.setEnabled(true);
                getAsyncHelper().runGenerateTask();
            }
        }
    };

    private AsyncHelper getAsyncHelper() {
        return new AsyncHelper(project, getPackageName(), getControllerName(), this);
    }

    private Generator getGenerator() {
        return new Generator(project, getPackageName(), getControllerName());
    }

    private CacheManager getCacheManager() {
        return new CacheManager(project, getPackageName(), getControllerName());
    }

    @Override
    public void onGenerateFinished(boolean result, File file) {
        SaveAndSyncHandlerImpl.refreshOpenFiles();
        VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);
        ProjectManagerEx.getInstanceEx().unblockReloadingProjectOnExternalChanges();
        project.getBaseDir().refresh(false, true);
        if (!result) {
            showCloseDialog(file);
        } else {
            getThis().show();
        }
    }


    /**
     * Lifecycle method which make some actions when form is show
     */
    @Override
    public void show() {
        setShortCutState(this.anAction, false);
        super.show();
    }

    /**
     * Lifecycle method which make some actions when form is dispose
     */
    @Override
    public void dispose() {
        setShortCutState(this.anAction, true);
        actionCallback.isDispose();
        super.dispose();
    }

    /**
     * Method which provide enable/disable shortcuts inside IDEA menu
     *
     * @param e
     * @param state
     */
    private void setShortCutState(AnActionEvent e, boolean state) {
        e.getActionManager().getAction("MagnetPlugin.MainMenuAddRestApi")
                .getTemplatePresentation().setEnabled(state);
        e.getActionManager().getAction("MagnetPlugin.ContextMenuRestController")
                .getTemplatePresentation().setEnabled(state);
    }

    private void showCloseDialog(File file) {
        String path = file.getAbsolutePath();
        String folder = path.substring(0, path.lastIndexOf(File.separator));
        String className = path.substring(path.lastIndexOf(File.separator) + 1);
        int option = JOptionPane.showConfirmDialog(
                null,
                Rest2MobileMessages.getMessage(Rest2MobileMessages.SOURCE_AVAILABLE_CONTINUE_EDITING_QUESTION, className, folder),
                "Success",
                JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            getThis().show();
        } else {
            dispose();
        }
    }

    public void populateMethods(String controllerName, String packageName, List<RestExampleModel> methodModels) {
        if (null != controllerName) {
            getControllerNameBox().getChildComponent().getEditor().setItem(controllerName);
        }

        if (null != packageName) {
            getPackageNameField().setText(packageName);
        }

        // first remove all tabs

        if (null != methodModels) {
            tabManager.removeAllTabs();
            for (int i = 0; i < methodModels.size(); i++) {
                MethodTabPanel panel = tabManager.addNewTab(i);
                panel.createMethodFromExample(methodModels.get(i));
            }
            tabManager.updateRemoveButtons();
        }
    }


}
