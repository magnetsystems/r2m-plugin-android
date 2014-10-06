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
package com.magnet.plugin.helpers;

import com.magnet.langpack.builder.rest.parser.validation.DocLocation;
import com.magnet.langpack.builder.rest.parser.validation.ValidationResultEntry;
import com.magnet.plugin.constants.JSONErrorType;
import com.magnet.plugin.models.JSONError;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
* Convert raw errors in JSON payload into proper messages errors
*/
public class JsonErrorConverter {

  private int currentLine;
  private int currentIndex;
  private String text;
  private List<String> lines;
  private List<ValidationResultEntry> validationResultEntries;

  public JsonErrorConverter(String text, List<ValidationResultEntry> validationResultEntries) {
    this.text = text;
    try {
      lines = org.apache.commons.io.IOUtils.readLines(new StringReader(text));
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.validationResultEntries = validationResultEntries;
  }

  public List<JSONError> convert() {
    List<JSONError> result = new LinkedList<JSONError>();

    for(ValidationResultEntry errorEntry : validationResultEntries) {
      int startIndex = 0;
      int endIndex = 0;
      if (null != errorEntry.getDocLocation()) {
        if(errorEntry.getDocLocation().getLine() > lines.size()) { // line number out of bound
          int lastLineNum = lines.size() - 1;
          startIndex = getLineStartIndex(lines.size());
          endIndex = startIndex + lines.get(lastLineNum).length();
          // Reset location since it seems out of bound
          errorEntry = new ValidationResultEntry(errorEntry.getPropertyName(), errorEntry.getErrorType(),
                  new DocLocation(lines.size(), lines.get(lastLineNum).length()), errorEntry.getMessage());
        } else if(errorEntry.getDocLocation().getCol() > lines.get(errorEntry.getDocLocation().getLine() - 1).length()) { // column out of bound
          startIndex = getLineStartIndex(errorEntry.getDocLocation().getLine()) + lines.get(errorEntry.getDocLocation().getLine() - 1).length() -1;
          endIndex = startIndex + 1;

          // Reset location since it seems out of bound
          errorEntry = new ValidationResultEntry(errorEntry.getPropertyName(), errorEntry.getErrorType(),
                  new DocLocation(errorEntry.getDocLocation().getLine(), lines.get(errorEntry.getDocLocation().getLine() - 1).length()),
                  errorEntry.getMessage());
        } else {
            startIndex = getLineStartIndex(errorEntry.getDocLocation().getLine()) + errorEntry.getDocLocation().getCol() - 1;
            switch (errorEntry.getErrorType()) {
              case INVALID_FORMAT:
                startIndex = startIndex - 1;

                Integer nextQuota = text.indexOf("\"", startIndex) + 1;
                Integer nextComma = text.indexOf(",", startIndex) + 1;
                Integer endOfLine = currentIndex + lines.get(currentLine).length() + 1;
                if (nextQuota != 0 && nextQuota < nextComma) {
                  endIndex = nextQuota;
                  int possibleStartIndex = text.lastIndexOf("\"", startIndex);
                  if (possibleStartIndex > 0) {
                    startIndex = possibleStartIndex;
                  }
                } else if (nextComma != 0 && nextQuota < endOfLine) {
                  endIndex = nextComma;
                  int possibleStartIndex = startIndex = text.lastIndexOf(":", endIndex);
                  if (possibleStartIndex > 0) {
                    startIndex = possibleStartIndex + 1;
                  }
                } else if (endOfLine < text.length()) {
                  endIndex = endOfLine;
                  startIndex = currentIndex;
                } else {
                  endIndex = startIndex + 1;
                }
                break;
              case EMPTY_ARRAY:
                startIndex = startIndex - 1;
                endIndex = text.indexOf("]", startIndex) + 1;
                break;
              case EMPTY_OBJECT:
                startIndex = startIndex - 1;
                endIndex = text.indexOf("}", startIndex) + 1;
                break;
              default: //case NULL_PROPERTY:
                endIndex = startIndex + 4;
            }
          }
        }

        result.add(new JSONError(errorEntry, startIndex, endIndex));
      }
      return result;
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
