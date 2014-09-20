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

package com.magnet.plugin.api.requests;

import com.magnet.plugin.api.mock.WorkerCallback;
import com.magnet.plugin.api.models.ApiMethodModel;
import com.magnet.plugin.api.models.RequestModel;
import com.magnet.plugin.api.requests.abs.BaseRequest;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

/**
 * Created by alshvets on 8/20/14.
 */
public class PutRequest extends BaseRequest {


    public PutRequest(WorkerCallback<ApiMethodModel> callback, RequestModel apiData) {
        super(callback, apiData);
    }

    @Override
    protected HttpRequestBase getRequest(RequestModel requestModel) {
        HttpPut httpPost=new HttpPut(requestModel.getUrl());
        try {
            httpPost.setEntity(new StringEntity(requestModel.getRequest(),"UTF-8"));
        } catch (Exception e) {
        }
        return httpPost;
    }
}
