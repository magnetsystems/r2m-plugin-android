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

import com.intellij.openapi.project.Project;
import com.magnet.plugin.components.ControllerHistoryComponent;
import com.magnet.plugin.models.ControllerHistory;

import java.util.List;

/**
 * Manage access to controller cache
 */
public class ControllerHistoryManager {


    private static ControllerHistory getCache(Project project) {
        return getComponent(project).getState();
    }

    private static ControllerHistoryComponent getComponent(Project project) {
        return project.getComponent(ControllerHistoryComponent.class);
    }

    public static String[] getCachedControllers(Project project) {
        List<String> controllerNames = getCache(project).getControllers();
        String[] array = controllerNames.toArray(new String[controllerNames.size() + 1]);
        array[0] = "";
        System.arraycopy(controllerNames.toArray(new String[controllerNames.size()]), 0, array, 1, controllerNames.size());
        return array;
    }

    public static void saveController(Project project, String controller) {
        ControllerHistory cache = getCache(project);
        cache.addController(controller);
    }
}
