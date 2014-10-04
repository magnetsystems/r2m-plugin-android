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

public class Path {

    private String path = "";
    private String variableName = "";
    private boolean isVariable;

    public Path() {
        this.path = "";
        variableName = "";
        isVariable = false;
    }

    public Path(String string) {
        path = string;
        isVariable = false;
    }

    public String getPath() {
//        if (isVariable) {
//            return getParameterizedVariable();
//        }
        return path;
    }

    public String getParameterizedPath() {
        if (isVariable) {
            return getParameterizedVariable();
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParameterizedVariable() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public boolean isVariable() {
        return isVariable;
    }

    public void setVariable(boolean isVariable) {
        this.isVariable = isVariable;
    }

    @Override
    public String toString() {
        return "Path{" +
                "path='" + path + '\'' +
                ", variableName='" + variableName + '\'' +
                ", isVariable=" + isVariable +
                '}';
    }
}
