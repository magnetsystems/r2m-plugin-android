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

import com.magnet.plugin.helpers.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Parsed URL
 */
public class ParsedUrl {

    private List<PathPart> pathParts = new ArrayList<PathPart>();
    private List<Query> queries = new ArrayList<Query>();
    private String baseUrl;
    private boolean pathEndsWithSlash;

    public List<PathPart> getPathParts() {
        return pathParts;
    }

    public void setPathParts(List<PathPart> pathParts) {
        this.pathParts = pathParts;
    }

    public void removePathParam(int index) {
        if (index > -1 && index < pathParts.size()) {
            pathParts.remove(index);
        }
    }

    public void addPathParam(PathPart pathPart) {
        pathParts.add(pathPart);
    }

    public boolean hasPathParams() {
        for (PathPart part : pathParts) {
            if (part.isTemplatized()) {
                return true;
            }
        }
        return false;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public void removeQueryParam(int index) {
        if (index > -1 && index < queries.size()) {
            queries.remove(index);
        }
    }

    public void addQueryParam(Query queryParam) {
        queries.add(queryParam);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String buildUrl(boolean isTemplatized) {
        StringBuilder builder = new StringBuilder(baseUrl);
        for (PathPart path : pathParts) {
            builder.append("/");
            if (isTemplatized) {
                builder.append(path.getTemplatizedPath());
            } else {
                builder.append(path.getEncodedValue());
            }
        }

        if (pathEndsWithSlash) {
            builder.append("/");
        }

        if (queries.size() > 0) {
            builder.append("?");

            for (Query query : queries) {
                String queryString = query.getEncodedKey() + "=" + query.getEncodedValue() + "&";
                builder.append(queryString);
            }
            if (queries.size() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
        }

        Logger.info(getClass(), builder.toString());
        return builder.toString();
    }


    public void setPathWithEndingSlash(boolean b) {
        pathEndsWithSlash = b;
    }

}
