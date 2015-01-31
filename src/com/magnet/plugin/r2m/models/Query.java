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

package com.magnet.plugin.r2m.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Query {

    /**
     * query key (not encoded)
     */
    private String key = "";
    /**
     * query value (not encoded)
     */
    private String value = "";

    public String getKey() {
        return key;
    }

    public String getEncodedKey() {
        try {
            return java.net.URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEncodedValue() {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setEncodedValue(String encodedValue) {
        try {
            this.value = URLDecoder.decode(encodedValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    public void setEncodedKey(String encodedKey) {
        try {
            this.key = URLDecoder.decode(encodedKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }


    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Query{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public Query(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Query() {
    }
}
