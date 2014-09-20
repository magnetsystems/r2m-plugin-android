package com.magnet.plugin.constants;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by dlernatovich on 9/12/14.
 */
public interface PluginIcon {
    Icon openIcon = IconLoader.getIcon("/minus_button.png");
    Icon closeIcon = IconLoader.getIcon("/plus_button.png");
    Icon closeIconPressed = IconLoader.getIcon("/plus_button_pressed.png");
    Icon openIconPressed = IconLoader.getIcon("/minus_button_pressed.png");
    Icon errorIcon = IconLoader.getIcon("/error_icon.png");
    Icon validIcon = IconLoader.getIcon("/check_icon.png");
}
