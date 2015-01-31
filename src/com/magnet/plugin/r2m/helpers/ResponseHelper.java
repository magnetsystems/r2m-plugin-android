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

import com.magnet.plugin.r2m.api.models.ApiMethodModel;

import java.io.IOException;
import java.util.Arrays;

/**
 * Response helper
 */
public class ResponseHelper {

    public static String processResponse(ApiMethodModel methodModel){
        String entity = "";
        if(null != methodModel.getHttpResponse().getEntity()) {
          try {
            entity = IOUtils.toString(methodModel.getHttpResponse().getEntity().getContent());
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        Logger.info(ResponseHelper.class, Arrays.asList(methodModel.getRequestHeaders()).toString());
        Logger.info(ResponseHelper.class, methodModel.getHttpResponse().getStatusLine().getReasonPhrase());
        Logger.info(ResponseHelper.class, Arrays.asList(methodModel.getHttpResponse().getAllHeaders()).toString());
        if (entity != null && !entity.isEmpty()) {
            Logger.info(ResponseHelper.class, entity);
        }

        return entity;

    }

}
