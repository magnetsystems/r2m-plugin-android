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

package com.magnet.plugin.r2m.api.core;

import com.magnet.plugin.r2m.api.mock.WorkerCallback;
import com.magnet.plugin.r2m.api.models.ApiMethodModel;
import com.magnet.plugin.r2m.api.models.RequestModel;
import com.magnet.plugin.r2m.api.requests.DeleteRequest;
import com.magnet.plugin.r2m.api.requests.GetRequest;
import com.magnet.plugin.r2m.api.requests.PostRequest;
import com.magnet.plugin.r2m.api.requests.PutRequest;
import com.magnet.plugin.r2m.api.requests.abs.BaseRequest;

/**
 * Factory for each URL request of type POST, GET, DELETE, PUT
 */
public class RequestFactory {

    public RequestFactory() {
        super();
    }

    public static BaseRequest getRequestForMethod(WorkerCallback<ApiMethodModel> callback, RequestModel model) {
        BaseRequest request = null;
        switch (model.getHttpMethod()) {
            case POST:
                request = new PostRequest(callback, model);
                break;
            case GET:
                request = new GetRequest(callback, model);
                break;
            case DELETE:
                request = new DeleteRequest(callback, model);
                break;
            case PUT:
                request = new PutRequest(callback, model);
                break;
            default:
                break;
        }
        return request;
    }
}
