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

package com.magnet.plugin.api.requests.abs;

import com.magnet.plugin.api.mock.Worker;
import com.magnet.plugin.api.mock.WorkerCallback;
import com.magnet.plugin.api.models.Error;
import com.magnet.plugin.helpers.IOUtils;
import com.magnet.plugin.helpers.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Abstract implementation for all HTTP requests
 * @param <T>
 */
public abstract class AbstractRequest<T> implements Runnable, Worker<T>, WorkerCallback<T> {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private WorkerCallback<T> callback;


    public AbstractRequest(WorkerCallback<T> callback) {
        this.setCallback(callback);
    }

    public void execute() {
        executor.execute(this);
    }

    @Override
    public void run() {
        try {
            doWork();
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onSuccess(final T t) {
//        Logger.info(getClass(), "onSuccess");
        if (getCallback() == null) {
            return;
        } else {
            getCallback().onSuccess(t);
        }
        setCallback(null);
    }

    @Override
    public void onError(final Exception e) {
        e.printStackTrace();
        Logger.info(getClass(), "onError: " + e.toString());

        if (getCallback() != null) {
            getCallback().onError(e);
        }
        setCallback(null);
    }

    @Override
    public void onError(final Error error) {
        Logger.error(getClass(), "onError: " + error.getErrorMessage());
        if (getCallback() != null) {
            getCallback().onError(error);
        }
        setCallback(null);
    }

    public WorkerCallback<T> getCallback() {
        return callback;
    }

    protected void setCallback(WorkerCallback<T> callback) {
        this.callback = callback;
    }

    public void removeCallback() {
        setCallback(null);
    }


    protected String toString(InputStream is) throws IOException {
        return IOUtils.toString(is);
    }

}
