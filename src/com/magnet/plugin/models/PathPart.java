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

import com.magnet.plugin.helpers.Rest2MobileConstants;

public class PathPart {

    private String pathValue;
    private String variableName;
    private boolean templatized;

    public PathPart() {
        this("");
    }

    public PathPart(String pathValue) {
        this(pathValue, "", false);
    }

    public PathPart(String pathValue, String variableName) {
        this(pathValue, variableName, true);
    }

    private PathPart(String pathValue, String variableName, boolean templatized) {
        this.pathValue = pathValue;
        this.variableName = variableName;
        this.templatized = templatized;
    }

    public String getPathValue() {
        return pathValue;
    }

    public String getTemplatizedPath() {
        if (templatized) {
            String s;
            if(null != pathValue && !pathValue.isEmpty()) {
                s = variableName + ":" + pathValue;
            } else {
                s = variableName;
            }

            return Rest2MobileConstants.START_TEMPLATE_VARIABLE + s + Rest2MobileConstants.END_TEMPLATE_VARIABLE;
        } else {
            return pathValue;
        }
    }

    public void setPathValue(String pathValue) {
        this.pathValue = pathValue;
    }

    public String getVariableName() {
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
                "pathValue='" + pathValue + '\'' +
                ", variableName='" + variableName + '\'' +
                ", templatized=" + templatized +
                '}';
    }
}
