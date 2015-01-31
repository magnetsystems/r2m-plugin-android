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

package com.magnet.plugin.r2m.singletons;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.magnet.plugin.r2m.listeners.ControllerActionCallback;
import com.magnet.plugin.r2m.ui.AddControllerForm;

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
            currentForm.getThis().setVisible(true);
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
