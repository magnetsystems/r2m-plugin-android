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

public class PathPart {

    private String pathPart;
    private String variableName;
    private boolean templatized;

    public PathPart() {
        this("");
    }

    public PathPart(String pathPart) {
        this.pathPart = pathPart;
        this.variableName = "";
        this.templatized = false;
    }

    public String getPathPart() {
        return pathPart;
    }

    public String getTemplatizedPath() {
        if (templatized) {
            return getTemplateVariable();
        }
        return pathPart;
    }

    public void setPathPart(String pathPart) {
        this.pathPart = pathPart;
    }

    public String getTemplateVariable() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public boolean isTemplatized() {
        return templatized;
    }

    public void setTemplatized(boolean templatized) {
        this.templatized = templatized;
    }

    @Override
    public String toString() {
        return "PathPart{" +
                "pathPart='" + pathPart + '\'' +
                ", variableName='" + variableName + '\'' +
                ", templatized=" + templatized +
                '}';
    }
}
