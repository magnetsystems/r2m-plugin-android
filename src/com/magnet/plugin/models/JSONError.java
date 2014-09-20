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
