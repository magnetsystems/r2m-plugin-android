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

import com.intellij.openapi.project.Project;
import com.magnet.plugin.actions.HistoryComponent;
import com.magnet.plugin.models.History;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryHelper {

    private static final int MAX_COUNT=10;
    private static final int START_INDEX=0;

    public static String[] getSavedUrls(Project project){
        HistoryComponent historyComponent=project.getComponent(HistoryComponent.class);
        History history=historyComponent.getState();
        String[] savedUrls=history.getUrls();
        String[] urls=new String[savedUrls.length+1]; //need require empty place for current value;
        urls[START_INDEX]="";
        System.arraycopy(savedUrls, START_INDEX, urls, 1, savedUrls.length);

        return urls;
    }

    public static void saveUrl(Project project, String url){
        HistoryComponent historyComponent=project.getComponent(HistoryComponent.class);
        History history=historyComponent.getState();
        String[] savedUrls=history.getUrls();
        List<String> savedList= new ArrayList<String>();
        savedList.addAll(Arrays.asList(savedUrls).subList(START_INDEX, savedUrls.length));
        if(!savedList.contains(url)){
            savedList.add(START_INDEX, url);
        }

        if(savedList.size()>MAX_COUNT){
            savedList=savedList.subList(START_INDEX, MAX_COUNT);
        }
        String[] urls=new String[savedList.size()];
        urls=savedList.toArray(urls);
        historyComponent.loadState(new History(urls));
    }
}
