package com.magnet.plugin.models;

import org.apache.http.entity.StringEntity;

import java.util.List;

/**
 * Created by alshvets on 9/9/14.
 */
public class History {

    public History() {
    }

    private String[] urls;

    public History(String[] urls) {
        this.urls=urls;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }
}