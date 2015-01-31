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

import com.intellij.openapi.project.Project;
import com.magnet.plugin.r2m.project.CacheManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cache holding all controllers used in this project
 */
public class ControllerHistory {
    private final Project project;
    private List<String> controllerNames;

    public ControllerHistory(Project project) {
        this.project = project;
        reset();
    }

    public void addController(String name) {
        if (controllerNames.contains(name)) {
            return;
        }
        controllerNames.add(name);
    }

    public void removeController(String name) {
        controllerNames.remove(name);
    }

    public void reset() {
        this.controllerNames = new ArrayList<String>();
        File[] dirs = CacheManager.getControllerFolders(project);
        if (null != dirs) {
            for (File d : dirs) {
                controllerNames.add(d.getName());
            }
        }
    }

    public List<String> getControllers() {
        return Collections.unmodifiableList(controllerNames);
    }


}
