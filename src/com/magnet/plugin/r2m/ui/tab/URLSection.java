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

import com.intellij.ui.JBColor;
import com.magnet.plugin.r2m.helpers.UrlParser;
import com.magnet.plugin.r2m.listeners.URLFocusListener;
import com.magnet.plugin.r2m.messages.Rest2MobileMessages;
import com.magnet.plugin.r2m.models.ParsedUrl;
import com.magnet.plugin.r2m.models.PathPart;
import com.magnet.plugin.r2m.models.Query;
import com.magnet.plugin.r2m.constants.FormConfig;
import com.magnet.plugin.r2m.ui.AbstractDocumentListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel implementing the URL section details with path and query params
 */
public class URLSection extends BasePanel implements FocusListener,
        QueryPanel.QueryParamCallBack, PathPartPanel.PathParamCallBack, BaseUrlCallBack {

    private final JTextField baseUrlField;
    private final BaseUrlCallBack baseUrlCallBack;

    private final PathPartLabel pathPartLabel;
    private final QueryPanelLabel queryPanelLabel;

    private final GroupLayout.ParallelGroup customPathGroupHorizontal;
    private final GroupLayout.SequentialGroup customPathGroupVertical;

    private final GroupLayout.ParallelGroup customQueryGroupHorizontal;
    private final GroupLayout.SequentialGroup customQueryGroupVertical;

    private static volatile int pathCount;
    private static volatile int queryCount;

    private URLFocusListener focusListener;

    private ParsedUrl parsedUrl;

    public List<PathPartPanel> getPathPanels() {
        return paths;
    }

    public ParsedUrl getParsedUrl() {
        return parsedUrl;
    }

    public List<Query> getQueryPanels() {
        List<Query> queries = new ArrayList<Query>();
        for (QueryPanel panel : this.queries) {
            queries.add(panel.getQuery());
        }
        return queries;
    }

    private volatile List<PathPartPanel> paths = new ArrayList<PathPartPanel>();
    private volatile List<QueryPanel> queries = new ArrayList<QueryPanel>();


    {
        JLabel jLabel3 = new JLabel();
        baseUrlField = new JTextField();

        JLabel jLabel4 = new JLabel();
        JSeparator jSeparator1 = new JSeparator();
        jSeparator1.setOpaque(false);
        JLabel jLabel5 = new JLabel();
        JButton pathButton = new JButton();

        pathPartLabel = new PathPartLabel();
        queryPanelLabel = new QueryPanelLabel();

        pathPartLabel.setVisible(false);
        queryPanelLabel.setVisible(false);

        JLabel jLabel6 = new JLabel();
        JSeparator jSeparator2 = new JSeparator();
        jSeparator2.setOpaque(false);
        JLabel jLabel7 = new JLabel();
        JButton queryButton = new JButton();

        jSeparator1.setForeground(JBColor.LIGHT_GRAY);
        jSeparator2.setForeground(JBColor.LIGHT_GRAY);


        jLabel3.setText(Rest2MobileMessages.getMessage("BASE_URL_SECTION_NAME"));

        jLabel4.setText(Rest2MobileMessages.getMessage("PATH_SECTION_NAME"));
        jLabel5.setText(Rest2MobileMessages.getMessage("SECTION_ADD_NEW"));
        pathButton.setText(Rest2MobileMessages.getMessage("SECTION_PLUS"));

        jLabel6.setText(Rest2MobileMessages.getMessage("QUERY_SECTION_NAME"));
        jLabel7.setText(Rest2MobileMessages.getMessage("SECTION_ADD_NEW"));
        queryButton.setText(Rest2MobileMessages.getMessage("SECTION_PLUS"));

        pathButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                PathPart pathPart = new PathPart();
                addPath(pathPart);
                if (null != parsedUrl) {
                    parsedUrl.addPathParam(pathPart);
                }
            }
        });

        queryButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                Query query = new Query();
                addQuery(query);
                if (null != parsedUrl) {
                    parsedUrl.addQueryParam(query);
                }
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
        baseUrlCallBack = this;
        baseUrlField.getDocument().addDocumentListener(new AbstractDocumentListener() {
            @Override
            protected void doUpdate() {
//                baseUrlField.setText(baseUrlField.getText());
                baseUrlCallBack.updated(baseUrlField);

            }
        });

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
                        .addComponent(pathPartLabel, GroupLayout.Alignment.CENTER)
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
                                                        .addComponent(pathPartLabel)
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

    public void removePath(PathPartPanel path) {
        pathCount--;
        if (parsedUrl != null) {
            parsedUrl.removePathParam(paths.indexOf(path));
        }
        paths.remove(path);
        if (pathCount == 0) {
            pathPartLabel.setVisible(false);
        }
    }

    public void removeQuery(QueryPanel query) {
        queryCount--;
        if (parsedUrl != null) {
            parsedUrl.removeQueryParam(queries.indexOf(query));
        }
        queries.remove(query);
        if (queryCount == 0) {
            queryPanelLabel.setVisible(false);
        }
    }


    private void addPath(PathPart pathPart) {
        PathPartPanel panel = new PathPartPanel(this, pathPart);
        paths.add(panel);
        pathPartLabel.setVisible(true);
        pathCount++;

        customPathGroupHorizontal.addComponent(panel);
        customPathGroupVertical.addComponent(panel);

        repaintPanel();
    }

    private void addQuery(Query query) {
        QueryPanel panel = new QueryPanel(this, query);
        queries.add(panel);
        queryPanelLabel.setVisible(true);
        queryCount++;

        customQueryGroupHorizontal.addComponent(panel);
        customQueryGroupVertical.addComponent(panel);

        repaintPanel();
    }

    private void removeAllPaths() {
        for (PathPartPanel pp : new ArrayList<PathPartPanel>(paths)/* avoid concurrent modification exception */) {
            deleted(pp);
        }
        paths.clear(); // should be cleared already
    }

    private void removeAllQueries() {
        for (QueryPanel qp : new ArrayList<QueryPanel>(queries)/* avoid concurrent modification exception */) {
            deleted(qp);
        }
        queries.clear();
    }

    public void setUrl(final String url) {
        if (url == null) {
            return;
        }

        if (null == parsedUrl || !parsedUrl.buildUrl(false).equals(url)) {
            parsedUrl = UrlParser.parseUrl(url);

            clearFields();

            // You need to reparse the URL since clearing the field has removed all its parts.
            parsedUrl = UrlParser.parseUrl(url);

            if (null != parsedUrl) {
                baseUrlField.setText(parsedUrl.getBaseUrl());
                List<PathPart> pathPartList = parsedUrl.getPathParts();
                List<Query> queries = parsedUrl.getQueries();
                for (PathPart aPathPart : pathPartList) {
                    addPath(aPathPart);
                }
                for (Query query : queries) {
                    addQuery(query);
                }
            }
        }
    }

    private void clearFields() {
        baseUrlField.setText("");
        removeAllPaths();
        removeAllQueries();
    }

    public String getExpandedUrl() {
        return parsedUrl == null ? null : parsedUrl.buildUrl(false);
    }

    public String getTemplateUrl() {
        return parsedUrl == null ? null : parsedUrl.buildUrl(true);
    }

    public boolean checkRequirementFields() {
        for (PathPartPanel pathPartPanel : paths) {
            if (!pathPartPanel.checkRequirementField()) {
                return false;
            }
        }

        for (QueryPanel pathPanel : queries) {
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
        for (PathPartPanel pathPartPanel : paths) {
            pathPartPanel.validateField();
        }
        focusListener.onFocusChange(getExpandedUrl());
    }

    public void setFocusListener(URLFocusListener focusListener) {
        this.focusListener = focusListener;
    }

    public void revalidateSection() {
        for (PathPartPanel panel : paths) {
            panel.invalidate();
        }

        for (QueryPanel queryPanel : queries) {
            queryPanel.invalidate();
        }

        this.invalidate();

    }

    private void repaintPanel() {
        revalidate();
        validate();
        repaint();
    }

    ////////////Methods from QueryPanel.QueryParamCallBack////////////
    @Override
    public void deleted(QueryPanel queryPanel) {
        remove(queryPanel);
        removeQuery(queryPanel);
        repaintPanel();

        focusListener.onFocusChange(getExpandedUrl());
    }

    @Override
    public void updated(QueryPanel queryPanel) {
        focusListener.onFocusChange(getExpandedUrl());
    }

    ////////////Methods from PathPartPanel.PathParamCallBack////////////
    @Override
    public void deleted(PathPartPanel pathPanel) {
        remove(pathPanel);
        removePath(pathPanel);

        repaintPanel();

        focusListener.onFocusChange(getExpandedUrl());
    }

    @Override
    public void updated(PathPartPanel pathPanel) {
        focusListener.onFocusChange(getExpandedUrl());
    }


    @Override
    public void updated(JTextField baseUrlField) {
        String baseUrl = baseUrlField.getText();
        if (null == parsedUrl) {
            parsedUrl = UrlParser.parseUrl(baseUrl);
        }
        if (null == parsedUrl) {
            return;
        }
        parsedUrl.setBaseUrl(baseUrl);
        focusListener.onFocusChange(getExpandedUrl());
    }
}
