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

package com.magnet.plugin.helpers;

import com.magnet.plugin.models.ParsedUrl;
import com.magnet.plugin.models.Path;
import com.magnet.plugin.models.Query;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryParser {
    public static ParsedUrl parseQuerry(String url) {
        List<Path> paths = new ArrayList<Path>();
        List<Query> queries = new ArrayList<Query>();
        ParsedUrl parsedUrl = null;
        String base;

        try {
            URL aURL=new URL(url);
            base =  aURL.getAuthority();
            String protocol=aURL.getProtocol();
            parsedUrl = new ParsedUrl();
            parsedUrl.setBase(protocol+"://"+base);
            List<NameValuePair> pairs = URLEncodedUtils.parse(aURL.getQuery(),
                    Charset.defaultCharset());
            for (NameValuePair pair : pairs) {
                Query query = new Query(pair.getName(), pair.getValue());
                queries.add(query);
            }
            parsedUrl.setQueries(queries);

            String path = aURL.getPath();
            String[] pathStrings = path.split("/");
            for (String string : pathStrings) {
                if (!string.isEmpty()) {
                    paths.add(new Path(string));
                }
            }
            parsedUrl.setPaths(paths);
        } catch (Exception ex) {
            return null;
        }
        return parsedUrl;
    }
}
