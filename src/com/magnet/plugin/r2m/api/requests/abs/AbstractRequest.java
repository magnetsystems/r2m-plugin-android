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

package com.magnet.plugin.r2m.api.requests.abs;

import com.magnet.plugin.common.Logger;
import com.magnet.plugin.r2m.api.mock.Worker;
import com.magnet.plugin.r2m.api.mock.WorkerCallback;
import com.magnet.plugin.r2m.helpers.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Abstract implementation for all HTTP requests
 *
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
        if (getCallback() == null) {
            return;
        } else {
            getCallback().onSuccess(t);
        }
        setCallback(null);
    }

    @Override
    public void onError(final Exception e) {
        Logger.info(getClass(), "onError: " + e.toString());

        if (getCallback() != null) {
            getCallback().onError(e);
        }
        setCallback(null);
    }

    public WorkerCallback<T> getCallback() {
        return callback;
    }

    protected void setCallback(WorkerCallback<T> callback) {
        this.callback = callback;
    }

    protected String toString(InputStream is) throws IOException {
        return IOUtils.toString(is);
    }

}
