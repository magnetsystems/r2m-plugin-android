package com.magnet.plugin.common.ui;


import com.magnet.plugin.common.ui.HintHelper;

import javax.swing.*;
import java.awt.*;

/**
 * password with hint
 */
public class HintPasswordTextField extends JPasswordField {
    private final String hint;

    public HintPasswordTextField(String hint) {
        this.hint = hint;
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        HintHelper.paint(g, this, hint);
    }

}

