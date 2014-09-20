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
import com.magnet.plugin.project.CacheManager;
import com.magnet.plugin.project.ProjectManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest-By-Example specification file helper
 */
public class FileHelper {

    /**
     * Check that the folder for the generated controller contains classes that already exist in the project
     * @param project project
     * @param controllerName controller name
     * @param packageName controller package name
     * @return the list of common classes
     */
    public static List<String> getCommonFiles(Project project, String controllerName, String packageName) {

        File tempSourceDir = new CacheManager(project, packageName, controllerName).getControllerSourceFolder();
        File projectSourceDir = ProjectManager.getSourceFolderFile(project);

        List<String> fileNamesFromTempSource = new ArrayList<String>();
        List<String> fileNamesFromProjectSource = new ArrayList<String>();

        getRelativeFiles(tempSourceDir, fileNamesFromTempSource);
        getRelativeFiles(projectSourceDir, fileNamesFromProjectSource);

        System.out.println(fileNamesFromTempSource);
        System.out.println(fileNamesFromProjectSource);

        List<String> list = new ArrayList<String>();
        for (String s : fileNamesFromTempSource) {
            if (fileNamesFromProjectSource.contains(s)) {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * Get all java class (package.class) located under folder
     * @param classes list of classes
     * @param folder folder to recursively introspect
     * @return list of classes
     */
    public static List<String> getRelativeFiles(final File folder, final List<String> classes) {
        return getClassesFromFolder(folder, folder, classes);
    }

    private static List<String> getClassesFromFolder(final File root, final File folder, final List<String> classes) {
        if (!folder.exists() || !folder.isDirectory()) {
            Logger.error(FileHelper.class, "Cache folder does not exist: " + folder);
            return null;
        }
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getClassesFromFolder(root, fileEntry, classes);
            } else {
                String path = fileEntry.getAbsolutePath();
                String relative = root.toURI().relativize(new File(path).toURI()).getPath();
                classes.add(relative);
            }
        }
        return classes;
    }


}
