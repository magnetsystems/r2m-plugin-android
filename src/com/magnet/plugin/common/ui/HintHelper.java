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
package com.magnet.plugin.common.ui;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

/**
 * Support for rendering hint in text field
 */
public class HintHelper {
    public static void paint(Graphics g, JTextField f, String hint) {
        if (f.getText().length() != 0) {
            return;
        }
        int h = f.getHeight();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Insets ins = f.getInsets();
        FontMetrics fm = g.getFontMetrics();
        int c0 = f.getBackground().getRGB();
        int c1 = f.getForeground().getRGB();
        int m = 0xfefefefe;
        int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
        g.setColor(new JBColor(new Color(c2, true), new Color(c2, true)));
        g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);

    }
}
