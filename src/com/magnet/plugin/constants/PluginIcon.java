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

package com.magnet.plugin.constants;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Icons
 */
public interface PluginIcon {
    Icon openIcon = IconLoader.getIcon("/minus_button.png");
    Icon closeIcon = IconLoader.getIcon("/plus_button.png");
    Icon closeIconPressed = IconLoader.getIcon("/plus_button_pressed.png");
    Icon openIconPressed = IconLoader.getIcon("/minus_button_pressed.png");
    Icon errorIcon = IconLoader.getIcon("/error_icon.png");
    Icon warningIcon = IconLoader.getIcon("/warning_icon.png");
    Icon validIcon = IconLoader.getIcon("/check_icon.png");
}
