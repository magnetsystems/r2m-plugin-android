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

import com.magnet.langpack.builder.rest.RestContentType;
import com.magnet.langpack.builder.rest.parser.ExampleParser;
import com.magnet.plugin.api.mock.WorkerCallback;
import com.magnet.plugin.api.models.ApiMethodModel;
import com.magnet.plugin.api.models.RequestHeaderModel;
import com.magnet.plugin.api.models.RequestModel;
import com.magnet.plugin.helpers.ContentTypeHelper;
import com.magnet.plugin.helpers.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;

/**
 * HTTP Base Request implementation
 */
public abstract class BaseRequest extends AbstractRequest<ApiMethodModel> {

    private final RequestModel requestModel;

    protected abstract HttpRequestBase getRequest(RequestModel requestModel);

    public BaseRequest(WorkerCallback<ApiMethodModel> callback, RequestModel requestModel) {
        super(callback);
        this.requestModel = requestModel;
    }


    @Override
    public void doWork() {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpRequestBase request = getRequest(requestModel);

            List<RequestHeaderModel> requestModelHeaders = requestModel.getHeaders();

            for (RequestHeaderModel header : requestModelHeaders) {
                request.setHeader(header.getName(), header.getValue());
            }

            // If not specified as a header argument, Content-Type is inferred from the request body
            // Add the header here so the request is successful.
            if (request.getHeaders(ContentTypeHelper.CONTENT_TYPE_HEADER) == null ||
                    request.getHeaders(ContentTypeHelper.CONTENT_TYPE_HEADER).length == 0) {
                RestContentType type = ExampleParser.guessContentType(requestModel.getRequest());
                if (null != type) {
                    request.setHeader(ContentTypeHelper.CONTENT_TYPE_HEADER, type.getName());
                }
            }
            // check if content-type is parameterized
            HttpResponse httpResponse = httpClient.execute(request);
            ApiMethodModel methodModel = new ApiMethodModel();
            methodModel.setRequestHeaders(request.getAllHeaders());
            methodModel.setHttpResponse(httpResponse);
            methodModel.setRequestModel(requestModel);
            onSuccess(methodModel);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.info(getClass(), e.toString());
            onError(e);
        }

    }


}
