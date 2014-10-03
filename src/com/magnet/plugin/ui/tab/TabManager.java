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
package com.magnet.plugin.ui.tab;

import com.intellij.openapi.project.Project;
import com.magnet.plugin.helpers.Logger;
import com.magnet.plugin.listeners.TabRemoveListener;
import com.magnet.plugin.messages.Rest2MobileMessages;
import com.magnet.plugin.ui.AddControllerForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Manage the method tabs in form
 */
public class TabManager {
    private final Project project;
    private final AddControllerForm form;
    private final JTabbedPane tabPanel;

    public List<MainPanel> getTabs() {
        return Collections.unmodifiableList(tabs);
    }

    private final List<MainPanel> tabs;

    public TabManager(Project project, AddControllerForm form, JTabbedPane tabPanel) {
        this.project = project;
        this.form = form;
        this.tabPanel = tabPanel;
        this.tabs = new ArrayList<MainPanel>();

        init();

    }

    private void init() {
        MainPanel customTab = new MainPanel(project, form, tabPanel);
        customTab.setTabRemoveListener(tabRemoveListener);
        customTab.setIndex(0);
        tabs.add(customTab);
        tabPanel.addTab(Rest2MobileMessages.getMessage(Rest2MobileMessages.METHOD_N, 1), customTab);
        tabPanel.addTab(Rest2MobileMessages.getMessage(Rest2MobileMessages.PLUS_TAB), new JLabel(""));
        tabPanel.addChangeListener(tabListener);
    }

    public void addNewTab(int index) {
        MainPanel customTab = new MainPanel(project, form, tabPanel);
        customTab.setTabRemoveListener(tabRemoveListener);
        customTab.setIndex(index);

        tabs.add(customTab);
        tabPanel.removeChangeListener(tabListener);
        tabPanel.remove(index);
        tabPanel.addTab(Rest2MobileMessages.getMessage(Rest2MobileMessages.METHOD_N, index + 1), customTab);
        tabPanel.addTab(Rest2MobileMessages.getMessage(Rest2MobileMessages.PLUS_TAB), new JLabel(""));
        tabPanel.setSelectedIndex(0);
        tabPanel.addChangeListener(tabListener);
        updateRemoveButtons();
    }

    public void removeTab(MainPanel mainPanel) {
        tabs.remove(mainPanel);
        tabPanel.setSelectedIndex(0);
        tabPanel.remove(mainPanel);
        tabPanel.invalidate();
    }

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




}
