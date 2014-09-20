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

import com.magnet.plugin.helpers.Logger;
import com.magnet.plugin.listeners.URLFocusListener;
import com.magnet.plugin.models.ParsedUrl;
import com.magnet.plugin.models.Path;
import com.magnet.plugin.models.Query;
import com.magnet.plugin.constants.FormConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aderkach
 */
public class URLSection extends BasePanel implements FocusListener {

    private JButton pathButton;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JSeparator jSeparator1;
    private JTextField baseUrlField;

    private JLabel jLabel6;
    private JLabel jLabel7;
    private JSeparator jSeparator2;
    private JButton queryButton;

    private PathPanelLabel pathPanelLabel;
    private QueryPanelLabel queryPanelLabel;

    private GroupLayout.ParallelGroup customPathGroupHorizontal;
    private GroupLayout.SequentialGroup customPathGroupVertical;

    private GroupLayout.ParallelGroup customQueryGroupHorizontal;
    private GroupLayout.SequentialGroup customQueryGroupVertical;

    private static volatile int pathCount;
    private static volatile int queryCount;

    private URLFocusListener focusListener;

    public List<PathPanel> getPathPanels() {
        return paths;
    }

    public List<Query> getQueryPanels() {
        List<Query> queries = new ArrayList<Query>();
        for (QueryPanel panel : querys) {
            queries.add(panel.getQuery());
        }
        return queries;
    }

    private volatile List<PathPanel> paths = new ArrayList<PathPanel>();
    private volatile List<QueryPanel> querys = new ArrayList<QueryPanel>();


    {
        jLabel3 = new JLabel();
        baseUrlField = new JTextField();

        jLabel4 = new JLabel();
        jSeparator1 = new JSeparator();
        jSeparator1.setOpaque(false);
        jLabel5 = new JLabel();
        pathButton = new JButton();

        pathPanelLabel = new PathPanelLabel();
        queryPanelLabel = new QueryPanelLabel();

        pathPanelLabel.setVisible(false);
        queryPanelLabel.setVisible(false);

        jLabel6 = new JLabel();
        jSeparator2 = new JSeparator();
        jSeparator2.setOpaque(false);
        jLabel7 = new JLabel();
        queryButton = new JButton();

        jSeparator1.setForeground(Color.lightGray);
        jSeparator2.setForeground(Color.lightGray);


        jLabel3.setText("Base URL");

        jLabel4.setText("Path");
        jLabel5.setText("Add new");
        pathButton.setText("+");

        jLabel6.setText("Query");
        jLabel7.setText("Add new");
        queryButton.setText("+");

        pathButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                addPath(new Path());
            }
        });

        queryButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                addQuery(new Query());
            }
        });

        pathCount = 0;
        queryCount = 0;

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        pathButton.setFont(baseFont);
        jLabel3.setFont(baseFont);
        jLabel4.setFont(baseFont);
        jLabel5.setFont(baseFont);
        baseUrlField.setFont(baseFont);
        jLabel6.setFont(baseFont);
        jLabel7.setFont(baseFont);
        queryButton.setFont(baseFont);

        baseUrlField.addFocusListener(this);

        customPathGroupHorizontal = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        customPathGroupVertical = layout.createSequentialGroup();

        customQueryGroupHorizontal = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        customQueryGroupVertical = layout.createSequentialGroup();

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(jLabel6))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(baseUrlField, GroupLayout.DEFAULT_SIZE, FormConfig.DEFAULT_COMPONENT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jSeparator1)
                                .addComponent(jLabel5)
                                .addComponent(pathButton))
                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jSeparator2)
                                        .addComponent(jLabel7)
                                        .addComponent(queryButton)
                        )
                        .addComponent(pathPanelLabel, GroupLayout.Alignment.CENTER)
                        .addGroup(customPathGroupHorizontal)
                        .addComponent(queryPanelLabel, GroupLayout.Alignment.CENTER)
                        .addGroup(customQueryGroupHorizontal)));

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel3)
                                                        .addComponent(baseUrlField)
                                        )
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jSeparator1, FormConfig.SEPARATOR_CUSTOM_SIZE, FormConfig.SEPARATOR_CUSTOM_SIZE, FormConfig.SEPARATOR_CUSTOM_SIZE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(pathButton)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                        .addComponent(pathPanelLabel)
                                                        .addGroup(customPathGroupVertical)
                                        )
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                .addComponent(jLabel6)
                                                .addComponent(jSeparator2, FormConfig.SEPARATOR_CUSTOM_SIZE, FormConfig.SEPARATOR_CUSTOM_SIZE, FormConfig.SEPARATOR_CUSTOM_SIZE)
                                                .addComponent(jLabel7)
                                                .addComponent(queryButton))
                                        .addGroup(layout.createSequentialGroup()
                                                        .addComponent(queryPanelLabel)
                                                        .addGroup(customQueryGroupVertical)
                                        )
                        )
        );
    }

    public void removePath(PathPanel path) {
        pathCount--;
        paths.remove(path);
        if (pathCount == 0) {
            pathPanelLabel.setVisible(false);
        }
    }

    public void removeQuery(QueryPanel query) {
        queryCount--;
        querys.remove(query);
        if (queryCount == 0) {
            queryPanelLabel.setVisible(false);
        }
    }


    private void addPath(Path path) {
        PathPanel panel = new PathPanel(this, path);
        panel.setFocusListener(this);
        paths.add(panel);
        pathPanelLabel.setVisible(true);
        pathCount++;

        customPathGroupHorizontal.addComponent(panel);
        customPathGroupVertical.addComponent(panel);

        this.revalidate();
        this.validate();
        this.repaint();
    }

    private void addQuery(Query query) {
        QueryPanel panel = new QueryPanel(this, query);
        panel.setFocusListener(this);
        querys.add(panel);
        queryPanelLabel.setVisible(true);
        queryCount++;

        customQueryGroupHorizontal.addComponent(panel);
        customQueryGroupVertical.addComponent(panel);

        this.revalidate();
        this.validate();
        this.repaint();
    }

    private void removeAllPathes() {
        while (paths.size() > 0) {
            paths.get(0).deleteThisPanel();
        }
        paths.clear();
    }

    private void removeAllQueries() {
        while (querys.size() > 0) {
            querys.get(0).deleteThisPanel();
        }
        querys.clear();
    }

    public void setParsedQuery(final ParsedUrl parsedUrl) {
        if (parsedUrl == null) {
            return;
        }

        clearFields();

        baseUrlField.setText(parsedUrl.getBase());
        List<Path> pathList = parsedUrl.getPaths();
        List<Query> queries = parsedUrl.getQueries();
        for (int i = 0; i < pathList.size(); i++) {
            addPath(pathList.get(i));
        }
        for (int i = 0; i < queries.size(); i++) {
            addQuery(queries.get(i));
        }
    }

    private void clearFields() {
        baseUrlField.setText("");
        removeAllPathes();
        removeAllQueries();
    }

    public String getAdvancedUrl() {
        StringBuilder builder = new StringBuilder(baseUrlField.getText());
        for (int i = 0; i < paths.size(); i++) {
            builder.append("/" + paths.get(i).getPath().getPath());
        }
        if (querys.size() > 0) {
            builder.append("?");
        }
        for (int i = 0; i < querys.size(); i++) {
            Query query = querys.get(i).getQuery();
            String queryString = query.getKey() + "=" + query.getValue() + "&";
            builder.append(queryString);
        }
        if (querys.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        Logger.info(getClass(), builder.toString());
        return builder.toString();

    }

    public String getAdvancedUrlToFile() {
        StringBuilder builder = new StringBuilder(baseUrlField.getText());
        for (int i = 0; i < paths.size(); i++) {
            builder.append("/" + paths.get(i).getPath().getVariablePath());
        }
        if (querys.size() > 0) {
            builder.append("?");
        }
        for (int i = 0; i < querys.size(); i++) {
            Query query = querys.get(i).getQuery();
            String queryString = query.getKey() + "=" + query.getValue() + "&";
            builder.append(queryString);
        }
        if (querys.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        Logger.info(getClass(), builder.toString());
        return builder.toString();

    }

    public boolean checkRequirementFields() {
        for (PathPanel pathPanel : paths) {
            if (!pathPanel.checkRequirementField()) {
                return false;
            }
        }

        for (QueryPanel pathPanel : querys) {
            if (!pathPanel.checkRequirementField()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        for (PathPanel pathPanel : paths) {
            pathPanel.invalidateField();
        }
        focusListener.onFocusChange(getAdvancedUrl());
    }

    public void setFocusListener(URLFocusListener focusListener) {
        this.focusListener = focusListener;
    }

    public void revalidateSection() {
        for (PathPanel panel : paths) {
            panel.invalidate();
        }

        for (QueryPanel queryPanel : querys) {
            queryPanel.invalidate();
        }

        this.invalidate();

    }

}
