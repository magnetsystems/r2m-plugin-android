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

package com.magnet.plugin.components;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.magnet.plugin.models.History;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manage history
 */
@State(
        name = "HistoryComponent",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.WORKSPACE_FILE),
                @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/r2murlcache.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class HistoryComponent implements ProjectComponent, PersistentStateComponent<History> {

    public final static String NAME = "HistoryComponent";

    private History cache;

    public HistoryComponent(Project project) {
        this.cache = new History(new String[0]);
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return NAME;
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    @Nullable
    @Override
    public History getState() {
        return cache;
    }

    @Override
    public void loadState(History cache) {
        this.cache = cache;
    }

}

