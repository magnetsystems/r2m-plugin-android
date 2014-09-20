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

package com.magnet.plugin.models;

import com.magnet.plugin.constants.JSONErrorType;

/**
 * Created by dlernatovich on 9/18/14.
 */
public class JSONError {
    protected JSONErrorType errorType;
    private int startIndex;
    private int endIndex;

    public JSONError() {
    }

    public JSONError(JSONErrorType errorType, int startIndex, int endIndex) {
        this.errorType = errorType;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public JSONErrorType getErrorType() {
        return errorType;
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

    @Override
    public String toString() {
        String error = "";
        if ((endIndex == -1)) {
            error = errorType + " (line: " + startIndex + ")\n";
        } else {
            error = errorType
                    + "  (range "
                    + startIndex
                    + " : "
                    + endIndex
                    + " )\n";
        }
        return error;
    }
}
