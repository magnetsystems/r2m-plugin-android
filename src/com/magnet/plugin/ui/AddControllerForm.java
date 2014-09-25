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
import com.intellij.openapi.vfs.VirtualFileManager;
import com.magnet.langpack.builder.rest.parser.validation.BodyValidationResult;
import com.magnet.plugin.api.models.ApiMethodModel;
import com.magnet.plugin.generator.Generator;
import com.magnet.plugin.helpers.*;
import com.magnet.plugin.listeners.ControllerActionCallback;
import com.magnet.plugin.listeners.CreateMethodCallback;
import com.magnet.plugin.listeners.TabRemoveListener;
import com.magnet.plugin.listeners.generator.PostGenerateCallback;
import com.magnet.plugin.messages.Rest2MobileMessages;
import com.magnet.plugin.project.CacheManager;
import com.magnet.plugin.project.ProjectManager;
import com.magnet.plugin.constants.FormConfig;
import com.magnet.plugin.ui.tab.MainPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.magnet.plugin.helpers.UIHelper.*;

public class AddControllerForm extends FrameWrapper implements CreateMethodCallback, PostGenerateCallback {
    private JPanel contentPane;
    private JTextField controllerName;
    private JButton generateServiceButton;
    private JTabbedPane tabPanel;
    private JTextField packageNameField;
    private ControllerActionCallback actionCallback;

    private List<MainPanel> tabs = new ArrayList<MainPanel>();

    private final Project project;

    private final AnActionEvent anAction;

    public AddControllerForm(Project project, AnActionEvent anAction, boolean canBeParent, ControllerActionCallback actionCallback) {
//        super(project, canBeParent);
        super(project);

        this.actionCallback = actionCallback;
        this.anAction = anAction;
        this.project = project;

        Font font = UIHelper.getFont();
        controllerName.setFont(font);
        generateServiceButton.setFont(font);
        packageNameField.setFont(font);

//        setResizable(true);
        setTitle(Rest2MobileMessages.getMessage(Rest2MobileMessages.WINDOW_TITLE));
        packageNameField.setText(ProjectManager.getPackageName(project));
        MainPanel customTab = new MainPanel(project, this, tabPanel);
        customTab.setTabRemoveListener(tabRemoveListener);
        customTab.setIndex(0);


        generateServiceButton.addActionListener(generateListener);
        generateServiceButton.setEnabled(true);
        tabs.add(customTab);
        tabPanel.addTab(Rest2MobileMessages.getMessage(Rest2MobileMessages.METHOD_N, 1), customTab);
        tabPanel.addTab(Rest2MobileMessages.getMessage(Rest2MobileMessages.PLUS_TAB), new JLabel(""));
        tabPanel.addChangeListener(tabListener);
        controllerName.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                controllerName.setText(VerifyHelper.verifyClassName(controllerName.getText()));
            }
        });
//        init();
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

    private void addNewTab(int index) {
        MainPanel customTab = new MainPanel(project, this, tabPanel);
        customTab.setTabRemoveListener(tabRemoveListener);
        customTab.setIndex(index);

//        scrollPane.setViewportView(customTab);
        tabs.add(customTab);
        tabPanel.removeChangeListener(tabListener);
        tabPanel.remove(index);
        tabPanel.addTab(Rest2MobileMessages.getMessage(Rest2MobileMessages.METHOD_N, index + 1), customTab);
        tabPanel.addTab(Rest2MobileMessages.getMessage(Rest2MobileMessages.PLUS_TAB), new JLabel(""));
        tabPanel.setSelectedIndex(0);
        tabPanel.addChangeListener(tabListener);
        updateRemoveButtons();
    }

    private void removeTab(MainPanel mainPanel) {
        int tabCount = tabPanel.getTabCount();
        tabs.remove(mainPanel);
        tabPanel.setSelectedIndex(0);
        tabPanel.remove(mainPanel);
        tabCount = tabPanel.getTabCount();
        tabPanel.invalidate();
    }

//    @Nullable
//    @Override
//    protected JComponent createCenterPanel() {
//        contentPane.setSize(FormConfig.SCREEN_DIMENSION);
//        contentPane.setMinimumSize(FormConfig.SCREEN_DIMENSION);
////        contentPane.setMaximumSize(FormConfig.SCREEN_DIMENSION);
////        contentPane.setPreferredSize(FormConfig.SCREEN_DIMENSION);
//        return contentPane;
//    }

//    @NotNull
//     @Override
//     protected Action[] createActions() {
//        return new Action[0];
//    }

//    @NotNull
//    @Override
//    protected Action[] createLeftSideActions() {
//        return super.createLeftSideActions();
//    }

    private ChangeListener tabListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            int index = tabPanel.getSelectedIndex();
            Logger.info(getClass(), "" + index);
            if (index == tabPanel.getTabCount() - 1) {
                addNewTab(index);
                updateRemoveButtons();
                updateSelectedIndex();
            }

        }
    };


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
            HistoryHelper.saveUrl(project, methodModel.getRequestModel().getUrl());
        }
    }

    @Override
    public void canGenerate(boolean canGenerate) {
        generateServiceButton.setEnabled(true);
    }


    private String getControllerName() {
        return this.controllerName.getText().trim();
    }

    private String getPackageName() {
        return this.packageNameField.getText().trim();
    }


    private void showErrorMessage(String message) {
        UIHelper.showErrorMessage(message);
    }

    private void updateRemoveButtons() {
        for (int i = 0; i < tabs.size(); i++) {
            tabs.get(i).setIndex(i);
        }
        for (MainPanel panel : tabs) {
            panel.enableRemoveButton(tabs.size() > 1);
        }
    }

    private void updateSelectedIndex() {
        tabPanel.setSelectedIndex(tabPanel.getTabCount() - 2);
        tabPanel.revalidate();
    }

    /*
    =======================LISTENERS SECTION=========================
     */
    private TabRemoveListener tabRemoveListener = new TabRemoveListener() {
        @Override
        public void removeCurrentPanel(MainPanel mainPanel) {
            if (tabPanel.getTabCount() > 2) {
                removeTab(mainPanel);
                // do not delete the method example file.
                // Users will be asked whether they want to keep it on or not at generation time.
                updateRemoveButtons();
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
            return checkAllMethodNames() && checkControllerName() && checkMainPanels() && checkNoRemainingCachedMethods() && checkResponse();
        }

        private boolean checkResponse() {
            boolean result = true;
            for (MainPanel mainPanel : tabs) {
                BodyValidationResult validationResult = JSONValidator.validateBody(mainPanel.getResponse());
                if (!validationResult.isValid()) {
                    UIHelper.showErrorMessage(Rest2MobileMessages.getMessage(Rest2MobileMessages.INVALID_RESPONSE_MESSAGE) + "\n" + JSONValidator.getErrorMessage(validationResult.getErrors(), null));
                    result = false;
                    break;
                }
            }
            return result;
        }

        /**
         * Check that the methods being created do not conflicts with existing methods already configured in the
         * cache director returned byt {@link com.magnet.plugin.project.CacheManager#getControllerExamplesFolder()}
         * @return true if no conflicts, false otherwise
         */
        private boolean checkNoRemainingCachedMethods() {
            boolean result = true;

            // methods from tabs
            Set<String> methodsToGenerate = new HashSet<String>();
            for (MainPanel mainPanel : tabs) {
                methodsToGenerate.add(mainPanel.getMethodTabName());
            }

            // methods from cache
            List<String> cachedMethods = new ArrayList<String>(getCacheManager().getControllerMethodNames());


            cachedMethods.removeAll(methodsToGenerate);

            if (cachedMethods.size() == 0) {
                return true;
            }

            for (String method : cachedMethods) {

                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to keep the following method that was previously defined for this controller?" +
                                "\n" + method + "(..)\n",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (option != 0) {
                    getCacheManager().clearControllerMethodCache(method);
                }
            }

            return true;
        }

        /**
         * Check that there is no duplicate name in the method tabs.
         * @return true if no duplicate, false otherwise
         */
        private boolean checkAllMethodNames() {
            boolean result = true;
            Set<String> strings = new HashSet<String>();
            for (MainPanel mainPanel : tabs) {
                strings.add(mainPanel.getMethodTabName());
            }
            result = (strings.size() == tabs.size());
            if (!result) {
                UIHelper.showErrorMessage("Name of methods must be different!");
            }
            return result;
        }

        private boolean checkControllerName() {
            if (controllerName.getText().trim().isEmpty()) {
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
            for (MainPanel tab : tabs) {
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
                generateServiceButton.setEnabled(true);
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

}
