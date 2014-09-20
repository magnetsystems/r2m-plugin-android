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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magnet.plugin.ui.tab;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Andrew
 */
public class ExtendedJPanel extends BasePanel{
    
    private List<JPanel> panels = new CopyOnWriteArrayList<JPanel>();
    private JPanel labelPanel;

    public List<JPanel> getPanels() {
        return panels;
    }

    public void addPanel(JPanel panel) {
        this.panels.add(panel);
    }
    
    public void removePanel(JPanel panel) {
        this.panels.remove(panel);
    }
    
    public void visibleLabelPanel(){
        labelPanel.setVisible(!panels.isEmpty());
    }

    public JPanel getLabelPanel() {
        return labelPanel;
    }

    public void setLabelPanel(JPanel labelPanel) {
        this.labelPanel = labelPanel;
        this.labelPanel.setVisible(false);
    }
}
