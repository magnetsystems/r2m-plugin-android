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

package com.magnet.plugin.constants;

import java.awt.*;

/**
 *
 * @author Andrew
 */
public interface FormConfig {
    int SEPARATOR_CUSTOM_SIZE = 6;
    int CUSTOM_GAP = 100;
    int CUSTOM_PREF_SIZE = 50;
    int PATH_CHECKBOX_PREF_SIZE = 20;
    int PATH_CHECKBOX_MAX_SIZE = 70;
    int CUSTOM_SCROLL_SIZE = 500;
    int CUSTOM_BUTTON_SIZE = 100;
    int CUSTOM_TEXTAREA_SIZE = 100;
    int MAX_TEXTAREA_SIZE = 300;
    int DEFAULT_COMPONENT_SIZE = 20;
    int CUSTOM_METHOD_TYPE_GAP = 50;

    Dimension PAYLOAD_TEXT_DIMENSION =new Dimension(500, 100);
    Dimension SCREEN_DIMENSION =new Dimension(700, 700);
    Dimension PAYLOAD_PANEL_DIMENSION =new Dimension(500, 250);
}
