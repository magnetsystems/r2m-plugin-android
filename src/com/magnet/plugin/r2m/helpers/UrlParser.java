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

package com.magnet.plugin.r2m.helpers;

import com.magnet.plugin.r2m.messages.Rest2MobileMessages;
import com.magnet.plugin.r2m.models.ParsedUrl;
import com.magnet.plugin.r2m.models.PathPart;
import com.magnet.plugin.r2m.models.Query;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParser {
    private static Pattern PATH_PARAM_PATTERN = Pattern.compile(Rest2MobileConstants.TEMPLATE_VARIABLE_REGEX);

    public static ParsedUrl parseUrl(String url) {
        List<PathPart> pathParts = new ArrayList<PathPart>();
        List<Query> queries = new ArrayList<Query>();
        ParsedUrl parsedUrl;
        String base;

        try {
            URL aURL = new URL(url);
            base = aURL.getAuthority();
            String protocol = aURL.getProtocol();
            parsedUrl = new ParsedUrl();
            parsedUrl.setPathWithEndingSlash(aURL.getPath().endsWith("/"));
            parsedUrl.setBaseUrl(protocol + "://" + base);
            List<NameValuePair> pairs = URLEncodedUtils.parse(aURL.getQuery(),
                    Charset.defaultCharset());
            for (NameValuePair pair : pairs) {
                Query query = new Query(pair.getName(), pair.getValue());
                queries.add(query);
            }
            parsedUrl.setQueries(queries);

            String[] pathStrings = aURL.getPath().split("/");
            for (String pathPart : pathStrings) {
              Matcher m = PATH_PARAM_PATTERN.matcher(pathPart);
              if (m.find()) {
                String paramDef = m.group(1);
                String[] paramParts = paramDef.split(":");
                if (paramParts.length > 1) {
                  pathParts.add(new PathPart(paramParts[1].trim(), paramParts[0].trim()));
                } else {
                  pathParts.add(new PathPart(paramParts[0].trim()));
                }
              } else {
                if(!pathPart.isEmpty()) {
                  pathParts.add(new PathPart(pathPart));
                }
              }
            }
            parsedUrl.setPathParts(pathParts);
        } catch (Exception ex) {
            Logger.error(UrlParser.class, Rest2MobileMessages.getMessage("CANNOT_PARSE_URL", url));
            return null;
        }
        return parsedUrl;
    }

    /**
     * @param url url where path param are expanded (removed "{""}")
     * @return expanded url
     */
    public static String expandUrl(String url) {
        Matcher m = PATH_PARAM_PATTERN.matcher(url);
        while (m.find()) {
          String paramDef = m.group(1);
          String[] paramParts = paramDef.split(":");
          if (paramParts.length > 1) {
            url = url.replaceAll(Rest2MobileConstants.START_TEMPLATE_VARIABLE_REGEX + paramParts[0] + Rest2MobileConstants.END_TEMPLATE_VARIABLE_REGEX, paramParts[1]);
          } else {
            url = url.replaceAll(Rest2MobileConstants.START_TEMPLATE_VARIABLE_REGEX + paramParts[0] + Rest2MobileConstants.END_TEMPLATE_VARIABLE_REGEX, paramParts[0]);
          }
        }

        return url;
    }
}
