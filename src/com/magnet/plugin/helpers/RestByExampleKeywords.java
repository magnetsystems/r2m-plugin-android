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

import java.io.File;

/**
 * Constants for Rest-By-Example specification
 */
public interface RestByExampleKeywords {

    /** Indicate a HTTP request */
    String REQUEST_TOKEN = "+Request";

    /** Indicate a HTTP response */
    String RESPONSE_TOKEN = "+Response";

    /** Indicate a HTTP headers for request or response */
    String HEADERS_TOKEN = "+Headers";

    /** Indicate a HTTP body for request or response */
    String BODY_TOKEN = "+Body";

    /** Indicate the method name to generate for the request/response pair */
    String NAME_TOKEN = "+Name";

}
