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

package com.magnet.plugin.r2m.api.models;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * Represent a Rest By Example model
 */
public class ApiMethodModel {


    /** the request model */
    private RequestModel requestModel;
    /** the response model */
    private ResponseModel responseModel;

    /** a http response from a live test */
    private HttpResponse httpResponse;
    /** the request headers */
    private Header[] requestHeaders;

    /** the response body */
    private String responseBody;

    /**
     * @return live test http response
     */
    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    /**
     * set live test http response
     * @param httpResponse http response
     */
    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * @return request headers
     */
    public Header[] getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * set actual request headers
     * @param requestHeaders request headers
     */
    public void setRequestHeaders(Header[] requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    /**
     * @return request model
     */
    public RequestModel getRequestModel() {
        return requestModel;
    }

    /**
     * The request model
     * @param model model
     */
    public void setRequestModel(RequestModel model) {
        this.requestModel = model;
    }

    /**
     * @return method response model
     */
    public ResponseModel getResponseModel() {
        return responseModel;
    }

    /**
     * Set response model
     * @param responseModel response model
     */
    public void setResponseModel(ResponseModel responseModel) {
        this.responseModel = responseModel;
    }

}
