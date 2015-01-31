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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class FormatHelper {
    public static String formatJSONCode(String code) {
        String formatted="";
        try{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(code);
            formatted = gson.toJson(je);
        }catch (Exception e){
            code = code.replaceAll(" ", "");
            code = code.replaceAll("\\[\\{", "\\[\n \\{");
            code = code.replaceAll("\\{\"", "\\{\n  \"");
            code = code.replaceAll(",\"", ",\n  \"");
            code = code.replaceAll("},\\{", "},\n \\{");
            code = code.replaceAll(":", " : ");
            code = code.replaceAll("}", "\n }");
            code = code.replaceAll("]", "\n]");
            formatted=code;
        }
        return formatted;
    }

    public static String unformatJSONCode(String code) {
        code = code.replaceAll("\n", " ");
        code = code.replaceAll(" ", "");
        return code;
    }
}
