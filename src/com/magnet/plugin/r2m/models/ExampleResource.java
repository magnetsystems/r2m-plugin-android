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

/**
 * Model for an example resource from r2m-examples repository
 */
public class ExampleResource {
    public static final String EXAMPLES_BASE_URL = "https://raw.githubusercontent.com/magnetsystems/r2m-examples/master/samples/";

    private final String name;
    private final String file;
    private final String description;

    public ExampleResource(String name, String file, String description) {
        this.name = name;
        this.file = file;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFile() {
        return file;
    }

    public String getUrl() {
        return EXAMPLES_BASE_URL + getFile();
    }

}
