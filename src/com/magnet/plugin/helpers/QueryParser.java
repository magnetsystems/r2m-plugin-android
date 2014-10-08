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
import com.magnet.plugin.models.PathPart;
import com.magnet.plugin.models.Query;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
    public static ParsedUrl parseQuery(String url) {
        List<PathPart> pathParts = new ArrayList<PathPart>();
        List<Query> queries = new ArrayList<Query>();
        ParsedUrl parsedUrl;
        String base;

        try {
            URL aURL = new URL(url);
            base = aURL.getAuthority();
            String protocol = aURL.getProtocol();
            parsedUrl = new ParsedUrl();
            parsedUrl.setBase(protocol + "://" + base);
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
                    List<String> params = findPathVariables(string);
                    if (params == null || params.isEmpty()) {
                        pathParts.add(new PathPart(string));
                    } else {
                        String param = params.get(0); // assuming only one param
                        pathParts.add(new PathPart(param, param));
                    }
                }
            }
            parsedUrl.setPathParts(pathParts);
        } catch (Exception ex) {
            Logger.error(QueryParser.class, "Can't parse URL " + url);
            return null;
        }
        return parsedUrl;
    }

    /**
     * Find path variables in template url. Variables are identified by <code>{var}</code>
     * @param templateUrl template url
     * @return list of path param variable name in url
     */
    public static List<String> findPathVariables(String templateUrl) {
        Pattern p = Pattern.compile(Rest2MobileConstants.TEMPLATE_VARIABLE_REGEX);
        Matcher m = p.matcher(templateUrl);
        List<String> l = new ArrayList<String>();
        while (m.find()) {
            l.add(m.group().substring(1, m.group().length() - 1));
        }
        return l;
    }

    /**
     * @param url url where path param are expanded (removed "{""}")
     * @return expanded url
     */
    public static String expandUrl(String url) {
        url = url.replaceAll(Rest2MobileConstants.START_TEMPLATE_VARIABLE_REGEX, "");
        url = url.replaceAll(Rest2MobileConstants.END_TEMPLATE_VARIABLE_REGEX, "");
        return url;
    }
}
