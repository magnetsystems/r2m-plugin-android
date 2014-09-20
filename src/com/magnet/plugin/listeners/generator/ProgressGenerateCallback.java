package com.magnet.plugin.listeners.generator;

import com.magnet.plugin.constants.GenerateActions;

/**
 * Created by dlernatovich on 9/16/14.
 */
public interface ProgressGenerateCallback {
    void onActionSuccess(GenerateActions actions);
    void onActionFailure(String error);
}
