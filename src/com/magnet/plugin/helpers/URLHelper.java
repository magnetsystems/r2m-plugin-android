package com.magnet.plugin.helpers;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.reporter.ConnectionException;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.net.HttpConfigurable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Helper to access URL
 */
public class URLHelper {

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
                        connection.setReadTimeout(Rest2MobileConstants.CONNECTION_TIMEOUT);
                        connection.setConnectTimeout(Rest2MobileConstants.CONNECTION_TIMEOUT);
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
            throw new ConnectionException(IdeBundle.message("updates.timeout.error"));
        }

        if (exception[0] != null) throw exception[0];
        return inputStreams[0];
    }
}
