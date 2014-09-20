package com.magnet.plugin.actions;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.magnet.plugin.models.History;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Created by alshvets on 9/9/14.
 */
@State(
        name = "HistoryComponent",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.WORKSPACE_FILE),
                @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/r2murlcache.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class HistoryComponent implements ProjectComponent, PersistentStateComponent<History> {

    private History history;

    public HistoryComponent(Project project) {
        history=new History(new String[0]);
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "HistoryComponent";
    }

    public void projectOpened() {
    }

    public void projectClosed() {
        // called when project is being closed
    }

    @Nullable
    @Override
    public History getState() {
        return history;
    }

    @Override
    public void loadState(History history) {
        this.history=history;
    }
}
