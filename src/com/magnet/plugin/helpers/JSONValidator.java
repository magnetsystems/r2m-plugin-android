package com.magnet.plugin.helpers;

import com.magnet.langpack.builder.rest.parser.validation.BodyValidationResult;
import com.magnet.langpack.builder.rest.parser.validation.BodyValidator;
import com.magnet.langpack.builder.rest.parser.validation.ValidationResultEntry;
import com.magnet.plugin.constants.JSONErrorType;
import com.magnet.plugin.models.JSONError;
import com.magnet.plugin.constants.PluginIcon;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jim on 9/18/14.
 */
public class JSONValidator {

    private static final String UNUSED_SYMBOLS = "\n|,|:|\"|\t";
    private static final String IS_JSON_VERIFY = "^\\{|^\\[|]$|}$";

    public static boolean isJSON(String text) {
        Pattern pattern = Pattern.compile(IS_JSON_VERIFY);
        Matcher matcher = pattern.matcher(removeUnusedSymbols(text.trim()));
        int matchCount = 0;
        while (matcher.find()) {
            matchCount++;
        }
        return matchCount >= 2;
    }

    public static List<JSONError> validateJSON(String text, JComponent field, JTextArea jsonField) {
        BodyValidationResult validationResult = BodyValidator.validateBody(text);

        StringBuilder errorMessage = getErrorMessage(validationResult.getErrors());
        setMessageToField(field, errorMessage.toString().trim(), !validationResult.isValid());

        JsonErrorConverter jsonErrorConverter = new JsonErrorConverter(text);
        List<JSONError> errors = new ArrayList<JSONError>();
        for(ValidationResultEntry entry : validationResult.getErrors()) {
          errors.add(jsonErrorConverter.convert(entry));
        }

        HighlightHelper.highlightErrors(errors, jsonField);

        return errors;
    }

    public static BodyValidationResult validateBody(String text) {
        return BodyValidator.validateBody(text);
    }

    private static String removeUnusedSymbols(String text) {
        return text.replaceAll(UNUSED_SYMBOLS, " ");
    }

    public static StringBuilder getErrorMessage(List<ValidationResultEntry> errors) {
        StringBuilder errorMessage = new StringBuilder();
        for (ValidationResultEntry error : errors) {
            errorMessage.append(error.getMessage()).append("\n");
        }
        return errorMessage;
    }

    private static void setMessageToField(JComponent field, String text, boolean isNeedVisible) {
        if (field instanceof JXLabel) {
            ((JXLabel) field).setText(text);
            if (isNeedVisible) {
                ((JXLabel) field).setIcon(PluginIcon.errorIcon);
            } else {
                ((JXLabel) field).setIcon(PluginIcon.validIcon);
            }
        } else if (field instanceof JTextField) {
            ((JTextField) field).setText(text);
            ((JTextField) field).setVisible(isNeedVisible);
        } else if (field instanceof JTextArea) {
            ((JTextArea) field).setText(text);
            ((JTextArea) field).setVisible(isNeedVisible);
        }
    }

    private static class JsonErrorConverter {
      private int currentLine;
      private int currentIndex;
      private String text;
      private List<String> lines;

      public JsonErrorConverter(String text) {
        this.text = text;
        try {
          lines = org.apache.commons.io.IOUtils.readLines(new StringReader(text));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      public JSONError convert(ValidationResultEntry errorEntry) {
        int startIndex = 0;
        int endIndex = 0;
        if(null != errorEntry.getDocLocation()) {
          startIndex = getLineStartIndex(errorEntry.getDocLocation().getLine()) + errorEntry.getDocLocation().getCol() - 1;
          switch (errorEntry.getErrorType()) {
            case INVALID_FORMAT:
              startIndex = startIndex - 1;

              Integer nextQuota = text.indexOf("\"", startIndex) + 1;
              Integer nextComma = text.indexOf(",", startIndex) + 1;
              Integer endOfLine = currentIndex + lines.get(currentLine).length() + 1;
              if(nextQuota != 0 && nextQuota < nextComma) {
                endIndex = nextQuota;
                int possibleStartIndex = text.lastIndexOf("\"", startIndex);
                if(possibleStartIndex > 0) {
                  startIndex = possibleStartIndex;
                }
              } else if(nextComma != 0 && nextQuota < endOfLine) {
                endIndex = nextComma;
                int possibleStartIndex = startIndex = text.lastIndexOf(":", endIndex);
                if(possibleStartIndex > 0) {
                  startIndex = possibleStartIndex + 1;
                }
              } else if(endOfLine < text.length()) {
                endIndex = endOfLine;
                startIndex = currentIndex;
              } else {
                endIndex = startIndex + 1;
              }
              break;
            case EMPTY_ARRAY:
              startIndex = startIndex - 1;
              endIndex =  text.indexOf("]", startIndex) + 1;
              break;
            case EMPTY_OBJECT:
              startIndex = startIndex - 1;
              endIndex =  text.indexOf("}", startIndex) + 1;
              break;
            default: //case NULL_PROPERTY:
              endIndex =  startIndex + 4;
          }
        }

        return new JSONError(convertErrorType(errorEntry.getErrorType()), startIndex, endIndex);
      }

      private int getLineStartIndex(int lineRequested) {
        if(currentLine < lineRequested - 1) {
          for(int i = 0; i < (lineRequested - currentLine - 1); i++) {
            currentIndex += lines.get(currentLine + i).length() + 1;
          }

          currentLine = lineRequested - 1;
        }

        return currentIndex;
      }

      private JSONErrorType convertErrorType(ValidationResultEntry.ErrorType validationErrorType) {
        switch (validationErrorType) {
          case INVALID_FORMAT:
            return JSONErrorType.ERROR_MISSING_COMMA;
          case EMPTY_ARRAY:
            return JSONErrorType.ERROR_EMPTY_ARRAY;
          case EMPTY_OBJECT:
            return JSONErrorType.ERROR_EMPTY_DICTIONARY;
          default: //case NULL_PROPERTY:
            return JSONErrorType.ERROR_NULL_VALUE;
        }
      }
    }
}
