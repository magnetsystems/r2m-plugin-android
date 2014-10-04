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

package com.magnet.plugin.models;

import com.intellij.util.net.HTTPMethod;
import com.magnet.plugin.api.models.RequestHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class Method {

    private String methodName;
    private String url;
    private List<RequestHeaderModel> headers = new ArrayList<RequestHeaderModel>();
    private List<PathPart> pathParts = new ArrayList<PathPart>();
    private List<Query> queries = new ArrayList<Query>();

    private String payload;
    private HTTPMethod httpMethod;
    private String response;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<RequestHeaderModel> getHeaders() {
        return headers;
    }

    public void setHeaders(List<RequestHeaderModel> headers) {
        this.headers = headers;
    }

    public List<PathPart> getPathParts() {
        List<PathPart> pathPartList = new ArrayList<PathPart>();
        return pathPartList;
    }

    public void setPathParts(List<PathPart> pathParts) {
        this.pathParts = pathParts;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Method{\n" +
                "methodName='" + methodName + '\'' +
                "\n, url='" + url + '\'' +
                "\n, headers=" + headers +
                "\n, paths=" + pathParts +
                "\n, queries=" + queries +
                "\n, payload='" + payload + '\'' +
                "\n, httpMethod=" + httpMethod +
                "\n, response='" + response + '\'' +
                "\n}";
    }
}
