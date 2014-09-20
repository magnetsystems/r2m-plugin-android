package com.magnet.plugin.helpers;

/**
 * Constants
 */
public interface Rest2MobileConstants {
    /**
     * Public name fo the plugin
     */
    String PUBLIC_TOOL_NAME = "rest2mobile";
    /**
     * Developer landing page URL
     */
    String MAGNET_DEVELOPER_URL = "developer.magnet.com";
    /**
     * Public package for this plugin used to identify the plugin
     */
    String PUBLIC_TOOL_PACKAGE = "com.magnet.r2m";
    /**
     * Where to find the version information
     */
    String PUBLIC_VERSION_URL = "https://raw.githubusercontent.com/magnetsystems/r2m-plugin-android/master/version.properties";
    /**
     * connection timeout for http request
     */
    int CONNECTION_TIMEOUT = 10000;

    //
    // Key used in file pointed to by PUBLIC_VERSION_URL describing the version
    //
    String LATEST_VERSION_KEY = "version";
    String DOWNLOAD_URL_KEY = "downloadUrl";
    String DESCRIPTION_KEY="description";
    String COMMENTS_KEY="comments";

    /**
     * Default documentation
     */
    String DOCUMENTATION_URL = "https://github.com/magnetsystems/rest2mobile/wiki";
}
