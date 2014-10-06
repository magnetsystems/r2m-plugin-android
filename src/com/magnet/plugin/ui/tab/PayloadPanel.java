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

package com.magnet.plugin.ui.tab;

import com.magnet.langpack.builder.rest.RestContentType;
import com.magnet.langpack.builder.rest.parser.ExampleParser;
import com.magnet.plugin.constants.FormConfig;
import com.magnet.plugin.constants.JSONErrorType;
import com.magnet.plugin.constants.PluginIcon;
import com.magnet.plugin.helpers.FormatHelper;
import com.magnet.plugin.helpers.JSONValidator;
import com.magnet.plugin.messages.Rest2MobileMessages;
import com.magnet.plugin.models.JSONError;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTable;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import java.util.*;
import java.util.List;

import static com.magnet.plugin.constants.Colors.*;

public class PayloadPanel extends BasePanel {

    protected JTextArea payloadField;
    protected JScrollPane jScrollPane;
    protected JXTable errorTable;
    protected JXLabel errorMessageLabel;
    protected JScrollPane errorTableScrollPane;
    protected JPanel errorPanel;

    protected List<JSONError> errors;

  {
        payloadField = new JTextArea();
        payloadField.setMinimumSize(FormConfig.PAYLOAD_DIMENSION);
        payloadField.setLineWrap(true);
        payloadField.setFont(baseFont);

        errorTable = new JXTable();
        errorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

          @Override
          public void valueChanged(ListSelectionEvent listSelectionEvent) {
            int viewRow = errorTable.getSelectedRow();
            if (viewRow >= 0) {
              if(null != errors && viewRow < errors.size()) {
                JSONError error = errors.get(viewRow);
                if(null != error.getDocLocation() && 0 != error.getDocLocation().getLine()) {
                  try {
                    int lineNum = error.getDocLocation().getLine() - 1;
                    int startIndex = payloadField.getLineStartOffset(lineNum);
                    int endIndex = payloadField.getLineEndOffset(lineNum);
                    //Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
                    //payloadField.getHighlighter().addHighlight(startIndex, endIndex, painter);

                    payloadField.requestFocusInWindow();
                    Rectangle viewRect = payloadField.modelToView(startIndex);
                    payloadField.scrollRectToVisible(viewRect);
                    // Highlight the text
                    payloadField.setCaretPosition(endIndex);
                    payloadField.moveCaretPosition(startIndex);
                  } catch (BadLocationException e) {
                    e.printStackTrace();
                  }
                }
              }

            } else {

            }
          }
        });

        errorMessageLabel = new JXLabel();
        errorMessageLabel.setLineWrap(true);
        errorMessageLabel.setFont(getFont());

        errorTableScrollPane = new JScrollPane();
        errorTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        errorTableScrollPane.setViewportView(errorTable);

        errorPanel = new JPanel();
        GroupLayout jPanel1Layout = new GroupLayout(errorPanel);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(errorMessageLabel)
                        .addComponent(errorTableScrollPane)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createSequentialGroup()
                        .addComponent(errorMessageLabel)
                        .addComponent(errorTableScrollPane));
        errorPanel.setLayout(jPanel1Layout);

        errorPanel.setVisible(false);
        errorTableScrollPane.setVisible(false);

        payloadField.getDocument().addDocumentListener(new DocumentListener() {
            private void setFieldColor() {
                String text = payloadField.getText();
                resetFields();
                if (JSONValidator.isJSON(text)) {
                  setFieldTextColor(BLACK);
                  errors = JSONValidator.validateJSON(text, null /* errorMessageField */, payloadField);
                  if(!errors.isEmpty()) {
                    errorTable.setModel(new ErrorTableModel(errors));

                    errorPanel.setVisible(true);
                    errorTableScrollPane.setVisible(true);

                    errorTable.setModel(new ErrorTableModel(errors));
                    errorTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                    Dimension tableSize =  errorTableScrollPane.getPreferredSize();
                    errorTable.getColumnModel().getColumn(0).setPreferredWidth(200);
                    errorTable.getColumnModel().getColumn(1).setPreferredWidth(100);
                    errorTable.getColumnModel().getColumn(2).setCellRenderer(new TableCellLongTextRenderer());
                    errorTable.getColumnModel().getColumn(2).setMinWidth(300);

                    // is it fatal error ?
                    if(errors.size() == 1 && errors.get(0).getErrorType() == JSONErrorType.ERROR_INVALID_FORMAT) {
                      errorMessageLabel.setText(Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_INVALID_FORMAT));
                      errorMessageLabel.setIcon(PluginIcon.errorIcon);
                    } else {
                      errorMessageLabel.setText(Rest2MobileMessages.getMessage(Rest2MobileMessages.VALIDATION_WARNING_MESSAGE));
                      errorMessageLabel.setIcon(PluginIcon.warningIcon);
                    }
                  } else {
                    //errorMessageLabel.setText(Rest2MobileMessages.getMessage(Rest2MobileMessages.ERROR_INVALID_FORMAT));
                    errorMessageLabel.setIcon(PluginIcon.validIcon);
                    errorPanel.setVisible(true);
                  }
                } else {
                  setFieldTextColor(GREEN);
                }
            }

            private void setFieldTextColor(Color color) {
                payloadField.setForeground(color);
            }

            private void resetFields() {
                payloadField.getHighlighter().removeAllHighlights();

                errorMessageLabel.setIcon(PluginIcon.validIcon);
                errorMessageLabel.setText("");

                errorTable.setModel(new ErrorTableModel(Collections.EMPTY_LIST));

                errorPanel.setVisible(false);
                errorTableScrollPane.setVisible(false);
            }

            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                setFieldColor();

            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                setFieldColor();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                setFieldColor();
            }
        });

        jScrollPane = new JScrollPane();
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setViewportView(payloadField);

    }

    public String getRawPayload() {
        String payload = getPayload();
        RestContentType type = ExampleParser.guessContentType(payload);
        if (type == RestContentType.JSON) {
            payload = FormatHelper.unformatJSONCode(getPayload());
        }
        return payload;
    }

    public void setPayload(String payload) {
        if (payload == null || payload.isEmpty()) {
            return;
        }
        RestContentType type = ExampleParser.guessContentType(payload);
        if (type == RestContentType.JSON) {
            payload = FormatHelper.formatJSONCode(payload);
        }
        payloadField.setText(payload);

    }


    public JTextArea getPayloadField() {
        return payloadField;
    }

    public String getPayload() {
        return payloadField.getText();
    }

    public void clearPayload() {
        payloadField.setText("");
    }

    public static class ErrorTableModel extends AbstractTableModel {
        public static final int PROPERTY_INDEX = 0;
        public static final int LOCATION_INDEX = 1;
        public static final int ERROR_INDEX = 2;

        public static final String[] columnNames = {"Property", "Location", "Error"};

        private final java.util.List<JSONError> errors;

        public ErrorTableModel(java.util.List<JSONError> errors) {
          this.errors = errors;
        }

        @Override
        public int getRowCount() {
          return errors.size();
        }

        @Override
        public int getColumnCount() {
          return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
          return columnNames[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
          JSONError record = errors.get(row);
          switch (column) {
            case PROPERTY_INDEX:
              return record.getPropertyName();
            case LOCATION_INDEX:
              return null != record.getDocLocation() ? record.getDocLocation().toString() : "";
            case ERROR_INDEX:
              return JSONErrorType.ERROR_INVALID_FORMAT != record.getErrorType() ? record.getErrorTypeAsString() : record.getMessage();
            default:
              return new Object();
          }
        }

        public boolean isCellEditable(int row, int column) {
          return false;
        }
    }

  /**
   * Customized TableCellRenderer for possible long error message
   */
    public class TableCellLongTextRenderer extends JTextArea implements TableCellRenderer {

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setText((String)value);
        this.setWrapStyleWord(true);
        this.setLineWrap(true);

        //set the JTextArea to the width of the table column
        setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
        if (table.getRowHeight(row) != getPreferredSize().height) {
          //set the height of the table row to the calculated height of the JTextArea
          table.setRowHeight(row, getPreferredSize().height);
        }

        if(isSelected){
          this.setBackground((Color)UIManager.get("Table.selectionBackground"));
          this.setForeground((Color)UIManager.get("Table.selectionForeground"));
          this.selectAll();
        }

        return this;
      }

    }

}
