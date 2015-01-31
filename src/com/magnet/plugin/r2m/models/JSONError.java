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

package com.magnet.plugin.r2m.models;

import com.magnet.langpack.builder.rest.parser.validation.DocLocation;
import com.magnet.langpack.builder.rest.parser.validation.ValidationResultEntry;
import com.magnet.plugin.r2m.constants.JSONErrorType;

public class JSONError {
    protected JSONErrorType errorType;
    private int startIndex;
    private int endIndex;
    private ValidationResultEntry validationResultEntry;

    public JSONError(ValidationResultEntry validationResultEntry, int startIndex, int endIndex) {
      this.validationResultEntry = validationResultEntry;
      this.startIndex = startIndex;
      this.endIndex = endIndex;

      this.errorType = convertErrorType(validationResultEntry.getErrorType());
    }

    public JSONErrorType getErrorType() {
        return errorType;
    }

    public String getErrorTypeAsString() {
      return errorType2String(errorType);
    }


  public void setErrorType(JSONErrorType errorType) {
        this.errorType = errorType;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public ValidationResultEntry.SEVERITY getSeverity() {
      return validationResultEntry.getSeverity();
    }

    public DocLocation getDocLocation() {
      return validationResultEntry.getDocLocation();
    }

    public String getPropertyName() {
      return validationResultEntry.getPropertyName();
    }

    public String getMessage() {
      return validationResultEntry.getMessage();
    }

    @Override
    public String toString() {
        String error = "";
        if ((endIndex == -1)) {
            error = errorType + " (line: " + startIndex + ")\n";
        } else {
            error = errorType.toString() + "\n";
        }
        return error;
    }

    private JSONErrorType convertErrorType(ValidationResultEntry.ErrorType validationErrorType) {
      switch (validationErrorType) {
        case INVALID_FORMAT:
          return JSONErrorType.ERROR_INVALID_FORMAT;
        case EMPTY_ARRAY:
          return JSONErrorType.ERROR_EMPTY_ARRAY;
        case EMPTY_OBJECT:
          return JSONErrorType.ERROR_EMPTY_DICTIONARY;
        default: //case NULL_PROPERTY:
          return JSONErrorType.ERROR_NULL_VALUE;
      }
    }

    private String errorType2String(JSONErrorType errorType) {
      switch (errorType) {
        case ERROR_NULL_VALUE :
          return "value is null";
        case ERROR_EMPTY_ARRAY :
          return "empty array []";
        case ERROR_EMPTY_DICTIONARY :
          return "empty object {}";
        case ERROR_INVALID_FORMAT:
          return "invalid format";
        default:
          return "unknown";
      }
    }
}
