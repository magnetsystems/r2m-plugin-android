package com.magnet.plugin.api.models;

/**
 * Represent a response in the model
 */
public class ResponseModel {
    private final String body;

    public ResponseModel(String body) {
        this.body = body;
    }

    public String getBody(){
        return body;
    }
}
