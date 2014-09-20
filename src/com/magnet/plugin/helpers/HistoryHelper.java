package com.magnet.plugin.helpers;

import com.intellij.openapi.project.Project;
import com.magnet.plugin.actions.HistoryComponent;
import com.magnet.plugin.models.History;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alshvets on 9/10/14.
 */
public class HistoryHelper {

    private static final int MAX_COUNT=10;
    private static final int START_INDEX=0;

    public static String[] getSavedUrls(Project project){
        HistoryComponent historyComponent=project.getComponent(HistoryComponent.class);
        History history=historyComponent.getState();
        String[] savedUrls=history.getUrls();
        String[] urls=new String[savedUrls.length+1]; //need require empty place for current value;
        urls[START_INDEX]="";
        for(int i=START_INDEX; i<savedUrls.length;i++){
            urls[i+1]=savedUrls[i];
        }

        return urls;
    }

    public static void saveUrl(Project project, String url){
        HistoryComponent historyComponent=project.getComponent(HistoryComponent.class);
        History history=historyComponent.getState();
        String[] savedUrls=history.getUrls();
        List<String> savedList= new ArrayList<String>();
        for(int i=START_INDEX; i<savedUrls.length;i++){
            savedList.add(savedUrls[i]);
        }
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
