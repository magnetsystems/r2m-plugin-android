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

package com.magnet.plugin.r2m.generator;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.magnet.plugin.r2m.api.models.ApiMethodModel;
import com.magnet.plugin.r2m.api.models.RequestHeaderModel;
import com.magnet.plugin.r2m.helpers.ControllerHistoryManager;
import com.magnet.plugin.r2m.helpers.FileHelper;
import com.magnet.plugin.r2m.helpers.Logger;
import com.magnet.plugin.r2m.helpers.RestByExampleKeywords;
import com.magnet.plugin.r2m.messages.Rest2MobileMessages;
import com.magnet.plugin.r2m.project.CacheManager;
import com.magnet.plugin.r2m.project.ProjectManager;
import com.magnet.tools.cli.simple.SimpleGenCommand;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Captures all generation operation
 */
public class Generator {

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
            writer.println(methodModel.getRequestModel().getHttpMethod().toString() + " " + methodModel.getRequestModel().getTemplateUrl());

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

            //displayIndicatorMessage(progressIndicator, "Generating code...", 40);
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
            File cachedSourceFolder = cacheManager.getControllerSourceFolder();

            // copy test files
            File generatedTestFiles = new File(cachedSourceFolder, CacheManager.RELATIVE_TEST_DIR);
            File targetTestFolder = ProjectManager.getTestSourceFolderFile(project);
            if(generatedTestFiles.exists()) {
                if(targetTestFolder != null && targetTestFolder.exists()) {
                    // if test filename exists, copy the test into a fileName_latest.java test
                    List<String> testFiles = FileHelper.getCommonTestFiles(project, controllerName, packageName);
                    String fileName = testFiles == null || testFiles.size() == 0 ? null : testFiles.get(0); // assuming one test class
                    if (fileName == null || !fileName.endsWith(".java")) {
                        FileUtils.copyDirectory(generatedTestFiles, targetTestFolder);
                    } else {
                        String newFileName = fileName + ".latest";
                        String header = Rest2MobileMessages.getMessage(Rest2MobileMessages.LATEST_TEST_CLASS_HEADER, fileName.substring(fileName.lastIndexOf('/') + 1));
                        File newFile = new File(generatedTestFiles, newFileName);
                        File oldFile = new File(generatedTestFiles, fileName);
                        String content = FileUtils.readFileToString(oldFile);
                        FileUtils.write(newFile, header);
                        FileUtils.write(newFile, content, true);
                        FileUtils.forceDelete(oldFile);
                        FileUtils.copyDirectory(generatedTestFiles, targetTestFolder);
                    }
                }
                // TODO: the src directory can be confusing, it should be called test,or tests as with ios.
                File rootTestDirectory = new File(cachedSourceFolder, "src");
                FileUtils.deleteDirectory(rootTestDirectory);
            }

            // copy others
            FileUtils.copyDirectory(cachedSourceFolder, ProjectManager.getSourceFolderFile(project));
            ControllerHistoryManager.saveController(project, cacheManager.getControllerFolder().getName());
            File controllerFile = ProjectManager.getControllerFile(project, packageName, controllerName);
            if (null != controllerFile) {
                showControllerFile(controllerFile);
            }
            //displayIndicatorMessage(progressIndicator, "Completed generation", 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show file in editor
     * @param file file to show
     */
    private void showControllerFile(final File file) {
        //need to be in main dispatcher thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VirtualFile vFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
                if (vFile == null) {
                    return; // do nothing
                }
                FileEditorManager.getInstance(project).openFile(vFile, true);
            }
        });

    }

    // TODO: disabled wherever we call it, somehow the progress indicator is not showing up,
    // TODO: but that's alright because generation is quasi instantaneous
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
