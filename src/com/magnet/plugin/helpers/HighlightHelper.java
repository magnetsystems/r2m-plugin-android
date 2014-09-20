package com.magnet.plugin.helpers;

import com.magnet.plugin.models.JSONError;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.List;

import static com.magnet.plugin.constants.Colors.PINK;


/**
 * Created by dlernatovich on 9/18/14.
 */
public class HighlightHelper {



    public static void highlightErrors(List<JSONError> errors,
                                       JTextArea responseField) {
        responseField.getHighlighter().removeAllHighlights();
        for (JSONError error : errors) {
            setHighlight(responseField, error);
        }
    }

    private static void setHighlight(JTextArea highlight, JSONError error) {
        Highlighter.HighlightPainter errorHighlighter =
                new DefaultHighlighter.DefaultHighlightPainter(PINK);
        try {
            highlight.getHighlighter().addHighlight(error.getStartIndex(),
                    error.getEndIndex(),
                    errorHighlighter);
        } catch (Exception ble) {
            System.out.println(ble);
        }
    }

}
