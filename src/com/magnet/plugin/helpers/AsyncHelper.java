package com.magnet.plugin.helpers;

import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ex.ProjectManagerEx;
import com.magnet.plugin.generator.Generator;
import com.magnet.plugin.constants.GenerateActions;
import com.magnet.plugin.listeners.generator.PostGenerateCallback;
import com.magnet.plugin.listeners.generator.ProgressGenerateCallback;
import com.magnet.plugin.project.ProjectManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

import static com.magnet.plugin.helpers.UIHelper.MESSAGE_GENERATING_SERVICE;

/**
 * Trigger code generation
 */
public class AsyncHelper implements ProgressGenerateCallback {

    private final Project project;
    private final String packageName;
    private final String controllerName;
    private PostGenerateCallback callback;
    private boolean result;

    public AsyncHelper(Project project, String packageName, String controllerName, PostGenerateCallback callback) {
        this.project = project;
        this.packageName = packageName;
        this.controllerName = controllerName;
        this.callback = callback;
        this.result = false;
    }

    public void runGenerateTask() {
        this.result = false;
        runGeneration();
    }

    private void runGeneration() {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, MESSAGE_GENERATING_SERVICE, false) {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                try {
                    getGenerator().generate(progressIndicator);
                } catch (Exception e) {
                    final String error = e.getMessage();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            onActionFailure(error);
                        }
                    });
                    return;
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        onActionSuccess(GenerateActions.GENERATE_SUCCESS);
                    }
                });
            }
        });
    }

    private void performFileOperation() {
        project.save();
        FileDocumentManager.getInstance().saveAllDocuments();
        ProjectManagerEx.getInstanceEx().blockReloadingProjectOnExternalChanges();
        ProgressManager.getInstance().run(new Task.Backgroundable(project, MESSAGE_GENERATING_SERVICE, false) {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                getGenerator().makeFilePerformance(progressIndicator);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        onActionSuccess(GenerateActions.FILE_OPERATION_SUCCESS);
                    }
                });
            }
        });
    }


    private void showOverrideConfirmationDialog(final List<String> list) {
        this.result = false;
        StringBuilder sb = new StringBuilder("\n");
        for (String e : list) {
            sb.append(e).append("\n");
        }
        int option = JOptionPane.showConfirmDialog(
                null,
                "Please, confirm: this will override these existing files:" + sb.toString(),
                "Warning",
                JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            onActionSuccess(GenerateActions.START_FILE_OPERATIONS);
        } else {
            this.result = true;
            onActionSuccess(GenerateActions.FILE_OPERATION_SUCCESS);
        }
    }

    private Generator getGenerator() {
        return new Generator(project, packageName, controllerName);
    }


    @Override
    public void onActionFailure(String error) {
        UIHelper.showErrorMessage(error);
    }

    @Override
    public void onActionSuccess(GenerateActions actions) {
        switch (actions) {
            case GENERATE_SUCCESS: {
                List<String> list = FileHelper.getCommonFiles(project, controllerName, packageName);
                if (list != null && !list.isEmpty()) {
                    showOverrideConfirmationDialog(list);
                } else {
                    onActionSuccess(GenerateActions.START_FILE_OPERATIONS);
                }
                break;
            }
            case START_FILE_OPERATIONS: {
                performFileOperation();
                break;
            }

            case FILE_OPERATION_SUCCESS: {
                callback.onGenerateFinished(this.result, ProjectManager.getClassFile(project, packageName, controllerName));
                break;
            }

            default:
                throw new IllegalArgumentException("Actions should be type of GenerateActions.class");
        }

    }
}
