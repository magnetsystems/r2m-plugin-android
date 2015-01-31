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

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Model for example manifest from r2m-examples
 */
public class ExamplesManifest {
    public static final String DESCRIPTION_SEPARATOR_KEY = " - ";

    private static final String FILE_KEY = "file";
    private static final String DESCRIPTION_KEY = "description";

    private final Map<String, ExampleResource> examples;

    public ExamplesManifest(String json) throws IOException {
        this.examples = new HashMap<String, ExampleResource>();

        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            // single example
            reader.beginObject();
            String file = null;
            String description = null;
            while (reader.hasNext()) {
                String key = reader.nextName();
                if (key.equals(FILE_KEY)) {
                    file = reader.nextString();
                } else if (key.equals(DESCRIPTION_KEY)) {
                    description = reader.nextString();
                }
            }
            reader.endObject();
            examples.put(name, new ExampleResource(name, file, description));
        }

    }

    public List<String> getExamplesList() {
        List<String> list = new ArrayList<String>(examples.keySet());
        Collections.sort(list);
        return list;
    }

    public ExampleResource getExample(String name) {
        return examples.get(name);
    }
}
