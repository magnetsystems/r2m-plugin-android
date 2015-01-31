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

package com.magnet.plugin.r2m.constants;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Icons
 */
public interface PluginIcon {
    Icon openIcon = IconLoader.getIcon("/r2m/minus_button.png");
    Icon closeIcon = IconLoader.getIcon("/r2m/plus_button.png");
    Icon closeIconPressed = IconLoader.getIcon("/r2m/plus_button_pressed.png");
    Icon openIconPressed = IconLoader.getIcon("/r2m/minus_button_pressed.png");
    Icon errorIcon = IconLoader.getIcon("/r2m/error_icon.png");
    Icon warningIcon = IconLoader.getIcon("/r2m/warning_icon.png");
    Icon validIcon = IconLoader.getIcon("/r2m/check_icon.png");
}
