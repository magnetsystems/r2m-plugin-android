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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.util.Processor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project utilities
 */
public class ProjectHelper {

    private static final String MAGNET_LIB_PREFIX = "magnet-sdk-android";

    public static boolean isContainsSDK(Project project) {
        boolean result = false;

        List<String> libraryNames = getLibrariesName(project);
        for (String s : libraryNames) {
            if (s.contains(MAGNET_LIB_PREFIX)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static String getMagnetLibName(Project project) {
        String result = null;

        List<String> libraryNames = getLibrariesName(project);
        for (String s : libraryNames) {
            if (s.contains(MAGNET_LIB_PREFIX)) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static Module getMainModule(Project project) {
        Module result = null;
        Module[] modules = ModuleManager.getInstance(project).getModules();
        final Map<String, Module> moduleMap = getModulesMap(modules);
        if (moduleMap.size() > 1) {
            moduleMap.remove(project.getName());
            for (Module module : modules) {
                ModuleRootManager.getInstance(module).orderEntries().forEachModule(new Processor<Module>() {
                    @Override
                    public boolean process(Module module) {
                        moduleMap.remove(module.getName());
                        return true;
                    }
                });
            }
        }
        List<Module> list = new ArrayList<Module>(moduleMap.values());
        if (list.size() > 0) {
            result = list.get(0);
        }

        return result;
    }

    public static String getModulePath(Module module, Project project) {
        String result = null;
        if ((module != null) && (project != null)) {
            result = project.getBasePath();
        }
        return result;
    }

    private static Map<String, Module> getModulesMap(Module[] modules) {
        final Map<String, Module> moduleMap = new HashMap<String, Module>();
        for (Module module : modules) {
            moduleMap.put(module.getName(), module);
        }
        return moduleMap;
    }

    private static List<String> getLibrariesName(Project project) {
        Module[] modules = ModuleManager.getInstance(project).getModules();
        final List<String> libraryNames = new ArrayList<String>();


        for (Module module : modules) {
            ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(new Processor<Library>() {
                @Override
                public boolean process(Library library) {
                    libraryNames.add(library.getName());
                    return true;
                }
            });
        }
        return libraryNames;
    }
}
