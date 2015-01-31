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

package com.magnet.plugin.r2m.messages;

/**
 * Placeholder for retrieving all L10N messages
 *
 */
public class R2MMessages {
    /**
     * @param key  constant identifying L10n message
     * @param args arguments to be expanded in L10n message
     * @return expanded L10n message given its key and arguments
     */
    public static String getMessage(String key, Object... args) {
        return MessagesSupport.getMessage("r2m/r2m", key, args);
    }


}
