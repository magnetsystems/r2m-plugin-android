package com.magnet.plugin.singletons;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.magnet.plugin.listeners.ControllerActionCallback;
import com.magnet.plugin.ui.AddControllerForm;

/**
 * Created by dlernatovich on 9/16/14.
 */
public class Application implements ControllerActionCallback {
    private static Application application;
    private AddControllerForm currentForm;

    public Application() {
        currentForm = null;
    }

    public static Application getApplication() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }

    public void initializeCurrentForm(Project project, AnActionEvent anAction, boolean canBeParent) {
        if (currentForm == null) {
            currentForm = new AddControllerForm(project, anAction, canBeParent, this);
            currentForm.show();
        } else {
            currentForm.getThis().show();
        }
    }

    public AddControllerForm getCurrentForm(Project project, AnActionEvent anAction, boolean canBeParent) {
        initializeCurrentForm(project, anAction, canBeParent);
        return currentForm;
    }

    @Override
    public void isDispose() {
        currentForm = null;
    }
}
