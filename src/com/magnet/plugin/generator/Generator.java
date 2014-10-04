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

package com.magnet.plugin.generator;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.magnet.plugin.api.models.ApiMethodModel;
import com.magnet.plugin.api.models.RequestHeaderModel;
import com.magnet.plugin.helpers.ControllerHistoryManager;
import com.magnet.plugin.helpers.FileHelper;
import com.magnet.plugin.helpers.Logger;
import com.magnet.plugin.helpers.RestByExampleKeywords;
import com.magnet.plugin.project.CacheManager;
import com.magnet.plugin.project.ProjectManager;
import com.magnet.tools.cli.simple.SimpleGenCommand;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Captures all generation operation
 */
public class Generator {
    private static final String MAPPING_FILE = "magnet_type_mapper.xml";

    private final Project project;
    private final String packageName;
    private final String controllerName;
    private final CacheManager cacheManager;

    public Generator(Project project, String packageName, String controllerName) {
        this.project = project;
        this.packageName = packageName;
        this.controllerName = controllerName;
        this.cacheManager = new CacheManager(project, packageName, controllerName);
    }

    /**
     * Create a method file in the cache dir (not in the project directory yet)
     * @param methodModel method model
     */
    public void createMethodFile(ApiMethodModel methodModel) {
        PrintWriter writer = null;
        try {
            File dir = cacheManager.getControllerExamplesFolder();
            if (!dir.exists() && !dir.mkdirs()) {
                Logger.error("Couldn't generate " + dir);
            }
            String methodName = methodModel.getRequestModel().getMethodName();
            File methodFile = new File(dir, methodName + CacheManager.EXAMPLE_FILE_EXTENSION);

            writer = new PrintWriter(methodFile, "UTF-8");

            // +Name section
            writer.println(RestByExampleKeywords.NAME_TOKEN + " " + methodName);
            writer.println();

            // +Request section
            writer.println(RestByExampleKeywords.REQUEST_TOKEN);
            writer.println(methodModel.getRequestModel().getHttpMethod().toString() + " " + methodModel.getRequestModel().getUrl());

            // +Headers request sub-section
            writer.println(RestByExampleKeywords.HEADERS_TOKEN);
            for (RequestHeaderModel header : methodModel.getRequestModel().getHeaders()) {
                writer.println(header.getName() + " : " + header.getValue());
            }

            // +Body request sub-section
            writer.println(RestByExampleKeywords.BODY_TOKEN);
            String body = methodModel.getRequestModel().getRequest();
            if (!body.isEmpty()) {
                writer.println(body);
            }
            writer.println();

            //+Response section
            writer.println(RestByExampleKeywords.RESPONSE_TOKEN);

            //+Body response sub-section
            writer.println(RestByExampleKeywords.BODY_TOKEN);
            writer.println(methodModel.getResponseModel().getBody());

            Logger.info(Generator.class, "Created cached method definition : " + methodFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Generate code and method specifications in cache directory (not in project directory yet)
     * @param progressIndicator UI progress indicator
     */
    public void generate(ProgressIndicator progressIndicator) throws Exception {
        try {
            File tempDirPath = cacheManager.getControllerExamplesFolder();
            File codeDirPath = cacheManager.getControllerSourceFolder();

            // Use a list rather than a string to handle command arguments with whitespaces
            List<String> cmdArgs = new ArrayList<String>();
            cmdArgs.add("-f"); // delete target directory if it already exists
            cmdArgs.add("-e");
            cmdArgs.add(tempDirPath.getAbsolutePath());
//            cmdArgs.add("-v"); // verbose mode
//            cmdArgs.add("-t"); // tracing mode
            cmdArgs.add("-c");
            cmdArgs.add(controllerName);
            cmdArgs.add("-o");
            cmdArgs.add(codeDirPath.getAbsolutePath());
            //empty json property policy
            cmdArgs.add("-j");
            cmdArgs.add("ignore");
            //generate unit test
            //cmdArgs.add("-u");
          if (!packageName.trim().isEmpty()) {
                cmdArgs.add("-p");
                cmdArgs.add(packageName);
            }
            cmdArgs.add("android");

            displayIndicatorMessage(progressIndicator, "Generating code...", 40);
            StringBuilder sb = new SimpleGenCommand().execute(cmdArgs);
            Logger.info(FileHelper.class, sb.toString());
        } catch (Exception e) {
            Logger.error(FileHelper.class, "Generation failed with exception");
            Logger.error(FileHelper.class, ExceptionUtils.getStackTrace(e));
            throw e;

        }
    }

    public void makeFilePerformance(ProgressIndicator progressIndicator) {
        progressIndicator.setFraction(0.1);
        try {
            displayIndicatorMessage(progressIndicator, "Removed temporary files...", 10);
            File cachedSourceFolder = cacheManager.getControllerSourceFolder();

            // copy test files
            File generatedTestFiles = new File(cachedSourceFolder, "src/test/java");
            File targetTestFolder = ProjectManager.getTestSourceFolderFile(project);
            if(generatedTestFiles.exists()) {
                if(targetTestFolder.exists()) {
                    FileUtils.copyDirectory(generatedTestFiles, targetTestFolder);
                }
                FileUtils.deleteDirectory(generatedTestFiles);
            }

            // copy others
            FileUtils.copyDirectory(cachedSourceFolder, ProjectManager.getSourceFolderFile(project));
            ControllerHistoryManager.saveController(project, cacheManager.getControllerFolder().getName());
            displayIndicatorMessage(progressIndicator, "Completed generation", 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void copyMappings(File sourcePath, File resFolder) {
        File mappingsFile = new File(sourcePath, MAPPING_FILE);
        File resFile = new File(resFolder, "xml");
        resFile.mkdirs();
        if (mappingsFile.exists() && !mappingsFile.isDirectory()) {
            try {
                FileUtil.copy(mappingsFile, resFile);
//                FileUtil.delete(mappingsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void displayIndicatorMessage(ProgressIndicator progressIndicator,
                                                String message,
                                                int value)
            throws InterruptedException {
        if (null == progressIndicator) {
            return;
        }
        progressIndicator.setFraction((((double) value) / 100));
        progressIndicator.setText(message);
        Thread.sleep(1000);
    }


}
