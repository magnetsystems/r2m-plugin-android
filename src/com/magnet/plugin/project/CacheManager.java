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

package com.magnet.plugin.project;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.magnet.plugin.helpers.FileHelper;
import com.magnet.plugin.helpers.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Cache manager for directory where code and examples are generated first before being copied over to the project
 */
public class CacheManager {
    /**
     * Cache sub-directory where source is generated
     */
    public static final String SOURCE_SUB_DIR = "source";
    /**
     * Cache sub-directory where method rest-by-example files are generated
     */
    public static final String EXAMPLES_SUB_DIR = "examples";
    /**
     * prefix used for method definition in the cache.
     */
    public static final String EXAMPLE_FILE_EXTENSION = ".txt";
    /**
     * Cache dir path relative to the root path of the current project
     */
    public static final String CACHE_DIR_REL_PATH = ".rest2mobile";

    /**
     * current project
     */
    private final Project project;
    /**
     * package name of the rest controller wizard
     */
    private final String packageName;
    /**
     * controller name fo the rest controller wizard
     */
    private final String controllerName;

    /**
     * Constructor
     *
     * @param project        project
     * @param packageName    package name
     * @param controllerName controller name
     */
    public CacheManager(Project project, String packageName, String controllerName) {
        this.project = project;
        this.packageName = packageName;
        this.controllerName = controllerName;
    }

    /**
     * The system-dependent path of the cache directory
     * where examples, and code is generated and cached before being copied to the project
     * Typically: (user.home)/.magnet.com/r2m/myproject/
     *
     * @param project project
     * @return location of the directory
     */
    public static String getProjectCacheFolder(Project project) {
        File dir = new File(project.getBasePath(), CACHE_DIR_REL_PATH);
        if (!dir.exists()) {
            Logger.info(FileHelper.class, "Creating project cache " + dir);
            if (!dir.exists() && !dir.mkdirs()) {
                Logger.error(FileHelper.class, "Couldn't create " + dir);
            }
        }
        return dir.getAbsolutePath();
    }

    /**
     * Examples folder directory
     *
     * @return system-dependent path to examples folder where method files are cached
     */
    public File getControllerExamplesFolder() {
        return new File(getControllerFolder(), EXAMPLES_SUB_DIR);
    }

    private File getControllerFolder() {
        return new File(getProjectCacheFolder(project) + File.separator + getUniqueFolderName(packageName, controllerName));
    }

    /**
     * @return list of cached method files. Each file is an example describing a method, null if no method files found.
     */
    public List<String> getControllerMethodNames() {
        String[] names = getControllerExamplesFolder().list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(EXAMPLE_FILE_EXTENSION);
            }
        });

        if (names == null || names.length == 0) {
            return null;
        }

        for (int i = 0; i < names.length; i++) {
            names[i] = names[i].substring(0, names[i].length() - EXAMPLE_FILE_EXTENSION.length());
        }

        return Arrays.asList(names);
    }

    /**
     * @param methodName methd name
     * @return file instance pointing to the cached example file for this particular method (may not exist)
     */
    private File getControllerMethodExample(String methodName) {
        return new File(getControllerExamplesFolder(), methodName + EXAMPLE_FILE_EXTENSION);
    }

    /**
     * Source folder directory
     *
     * @return system-dependent path to source folder where code is generated before it is copied to project
     */
    public File getControllerSourceFolder() {
        return new File(getControllerFolder(), SOURCE_SUB_DIR);
    }

    /**
     * A unique folder name concatenating package and controller name
     *
     * @param packageName    package name
     * @param controllerName controller name
     * @return unique folder name
     */
    private static String getUniqueFolderName(String packageName, String controllerName) {
        return packageName + "." + controllerName;
    }

    /**
     * Clear the file definition for a particular controller method.
     * @param methodName method name
     */
    public void clearControllerMethodCache(String methodName) {
        FileUtil.delete(getControllerMethodExample(methodName));
    }
}
