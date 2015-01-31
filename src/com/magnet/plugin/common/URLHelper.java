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
package com.magnet.plugin.common;

import com.intellij.ide.IdeBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.net.HttpConfigurable;
import com.magnet.plugin.common.helpers.FormattedLogger;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Helper to access URL
 */
public class URLHelper {
    /**
     * connection timeout for http request
     */
    private static final int CONNECTION_TIMEOUT = 10000;

    public static InputStream loadUrl(final String url) throws Exception {
        final InputStream[] inputStreams = new InputStream[]{null};
        final Exception[] exception = new Exception[]{null};
        Future<?> downloadThreadFuture = ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            public void run() {
                try {
                    HttpURLConnection connection;
                    if (ApplicationManager.getApplication() != null) {
                        connection = HttpConfigurable.getInstance().openHttpConnection(url);
                    } else {
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setReadTimeout(CONNECTION_TIMEOUT);
                        connection.setConnectTimeout(CONNECTION_TIMEOUT);
                    }
                    connection.connect();

                    inputStreams[0] = connection.getInputStream();
                } catch (IOException e) {
                    exception[0] = e;
                }
            }
        });

        try {
            downloadThreadFuture.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException ignored) {
        }

        if (!downloadThreadFuture.isDone()) {
            downloadThreadFuture.cancel(true);
            throw new Exception(IdeBundle.message("updates.timeout.error"));
        }

        if (exception[0] != null) throw exception[0];
        return inputStreams[0];
    }

    /**
     * Utility method to get a URL instance from either a file path or a url string
     *
     * @param source path or url string to the file
     * @return URL instance or null if source is invalid
     */
    private static URL getURL(String source) {
        URL url = null;
        try {
            url = new URL(source);
        } catch (MalformedURLException e) {
            File file = new File(source);
            if (file.exists()) {
                try {
                    url = file.toURI().toURL();
                } catch (Exception ex) {
                    //
                }
            }
        }
        return url;

    }

    /**
     * Get the file corresponding to the <code>source</code>
     * If <code>source</code> is a file path then return the file instance for it
     * If <code>source</code> is a URL then copy the resource to a local file, and return an instance of it
     * If there is no resource associated with source, then throw a CommandException
     *
     * @return file instance
     */
    public static File getFileFromURL(String source) throws Exception {

        File file = new File(source);
        if (file.exists()) {
            return file;
        }

        URL url = getURL(source);
        if (url == null) {
            return null;
        }

        String fileName = source.substring(source.lastIndexOf("/") + 1, source.length());
        File temp = File.createTempFile("downloaded-" + System.currentTimeMillis(), fileName);

        InputStream in = null;
        OutputStream out = null;
        try {
            in = loadUrl(source);
            if (null == in) {
                return null;
            }
            out = new FileOutputStream(temp);
            org.apache.commons.io.IOUtils.copy(in, out);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return temp;
    }

    public static boolean checkURL(String urlS){
        FormattedLogger logger = new FormattedLogger(URLHelper.class);
        logger.append("checkURL:" + urlS);
        try {
            URL url = new URL(urlS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            logger.append("Response code: " + conn.getResponseCode());
            logger.showInfoLog();
            return true;
        } catch (IOException e) {
            logger.append(e.getMessage());
            logger.showErrorLog();
        }
        return false;
    }

    public static String checkURLConnection(String urlS, String username, String password){
        String status = "Error connection to server!";
        FormattedLogger logger = new FormattedLogger(URLHelper.class);
        logger.append("checkURLConnection:" + urlS);
        try {
            URL url = new URL(urlS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (username != null && username.trim().length() > 0 && password != null && password.trim().length() > 0) {
                final String authString = username + ":" + password;
                conn.setRequestProperty("Authorization", "Basic " + Base64.encode(authString.getBytes()));
            }

            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            status = "" + conn.getResponseCode();
            logger.append("Response code:" + status);
            logger.showInfoLog();
        } catch (Exception e) {
            logger.append(">>>" +  e.getClass().getName() + " message: " + e.getMessage());
            logger.showErrorLog();
        }
        return status;
    }

    public static boolean checkPort(int port) {
        return port > 0 && port <= 65535;
    }

    public static boolean checkPort(String port){
        try{
            return checkPort(Integer.parseInt(port));
        }catch (NumberFormatException ex){
            return false;
        }
    }

}