package com.magnet.plugin.models;

import com.intellij.openapi.project.Project;
import com.magnet.plugin.project.CacheManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cache holding all controllers used in this project
 */
public class ControllerCache {
    private final Project project;
    private List<String> controllerNames;

    public ControllerCache(Project project) {
        this.project = project;
        reset();
    }

    public void addController(String name) {
        if (controllerNames.contains(name)) {
            return;
        }
        controllerNames.add(name);
    }

    public void removeController(String name) {
        controllerNames.remove(name);
    }

    public void reset() {
        this.controllerNames = new ArrayList<String>();
        File[] dirs = CacheManager.getControllerFolders(project);
        for (File d : dirs) {
            controllerNames.add(d.getName());
        }
    }

    public List<String> getControllers() {
        return Collections.unmodifiableList(controllerNames);
    }


}
