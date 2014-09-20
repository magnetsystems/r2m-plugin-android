package com.magnet.plugin.listeners.generator;

import java.io.File;

/**
 * Callback invoked upon successful generation
 */
public interface PostGenerateCallback {
    void onGenerateFinished(boolean result, File path);
}
