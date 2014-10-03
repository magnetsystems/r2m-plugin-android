package com.magnet.plugin.components;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for cache component
 */
public abstract class CacheComponent<T> implements ProjectComponent, PersistentStateComponent<T> {
    private T cache;
    private final String name;

    public CacheComponent(T cache, String name) {
        this.cache = cache;
        this.name = name;
    }

    public void initComponent() {}

    public void disposeComponent() {}

    @NotNull
    public String getComponentName() {
        return name;
    }

    public void projectOpened() {}

    public void projectClosed() {}

    @Nullable
    @Override
    public T getState() {
        return cache;
    }

    @Override
    public void loadState(T cache) {
        this.cache = cache;
    }

}
