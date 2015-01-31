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

package com.magnet.plugin.r2m.project;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.magnet.langpack.builder.rest.parser.ExampleParser;
import com.magnet.langpack.builder.rest.parser.RestExampleModel;
import com.magnet.plugin.r2m.helpers.FileHelper;
import com.magnet.plugin.r2m.helpers.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cache manager for directory where code and examples are generated first before being copied over to the project
 */
public class CacheManager {
    public static final String RELATIVE_TEST_DIR =  "src/test/java";
    /**
     * Cache sub-directory where test source is generated
     */
    public static final String TEST_SOURCE_SUB_DIR = "source/" + RELATIVE_TEST_DIR;

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
     * where examples, and code is generated and cached before being copied to the project/
     * @param project project
     * @param create whether to create the folder if it does not exist
     * @return location of the directory or null if it does not exist
     */
    public static String getProjectCacheFolder(Project project, boolean create) {
        File dir = new File(project.getBasePath(), CACHE_DIR_REL_PATH);
        if (!dir.exists()) {
            if (!create) {
                return null;
            }
            if (!dir.mkdirs()) {
                // on windows, on an new project, it tries to create it in
                // C:\ProgramData\Microsoft\Windows\Start Menu\Programs\Android Studio\.rest2mobile
                // See https://github.com/magnetsystems/r2m-sdk-android/issues/4
                Logger.info(FileHelper.class, "Couldn't create " + dir);
                return null;
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

    /**
     * Get the cache folder for a particular controller
     * @return folder file or null if it does not exist (with option create set to false
     */
    public File getControllerFolder() {
        return new File(getProjectCacheFolder(project, true) + File.separator + getUniqueFolderName(packageName, controllerName));
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

    public List<RestExampleModel> getControllerMethodsModel() {
        ExampleParser parser = new ExampleParser();
        List<RestExampleModel> methodModels = new ArrayList<RestExampleModel>();
        List<String> methodNames = getControllerMethodNames();
        if (null == methodNames) {
            return null;
        }
        for (String name: methodNames) {
            File file = getControllerMethodExample(name);
            try {
                URL url = file.toURI().toURL();
                methodModels.addAll(parser.parseExample(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                // should not happen
            }
        }
        return methodModels;
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
     * Source folder directory
     *
     * @return system-dependent path to test source folder where code is generated before it is copied to project
     */
    public File getTestControllerSourceFolder() {
        return new File(getControllerFolder(), TEST_SOURCE_SUB_DIR);
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

    /**
     * @param project current project
     * @return array of controller cache folders (sub-dirs) under the {@link #CACHE_DIR_REL_PATH}
     */
    public static File[] getControllerFolders(Project project) {
        String dir = getProjectCacheFolder(project, false);
        if (dir == null) { // does not exist
            return null;
        }
        return new File(dir).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
    }
}
