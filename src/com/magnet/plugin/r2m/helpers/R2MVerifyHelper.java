package com.magnet.plugin.r2m.helpers;

import com.magnet.plugin.common.helpers.VerifyHelper;
import com.magnet.plugin.r2m.messages.R2MMessages;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * R2M specific verification methods.
 */
public class R2MVerifyHelper {

    /**
     * The supported schemes when validating the url
     */
    private static final String[] SUPPORTED_PROTOCOL_SCHEMES = {"http", "https"};

    private static final long URL_VALIDATION_OPTIONS = UrlValidator.ALLOW_LOCAL_URLS;

    public static boolean isValidUrl(String url) {
        String templateURL = url;
        templateURL = templateURL.replaceAll(R2MConstants.START_TEMPLATE_VARIABLE_REGEX, "");
        templateURL = templateURL.replaceAll(R2MConstants.END_TEMPLATE_VARIABLE_REGEX, "");
        UrlValidator urlValidator = new UrlValidator(SUPPORTED_PROTOCOL_SCHEMES, URL_VALIDATION_OPTIONS);
        return urlValidator.isValid(templateURL);
    }

    public static boolean isValidUrlWithoutPerformance(String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    private static String verifyByKeyWords(String text) {
        if (VerifyHelper.KEYWORDS.contains(text)) {
            UIHelper.showErrorMessage(R2MMessages.getMessage("NAME_CONFLICTS_WITH_JAVA_KEYWORDS"));
            text = "";
        }
        return text;
    }

    public static String verifyClassName(String name) {
        return verify(name, "[^A-Za-z0-9_]", true);
    }

    public static String verifyMethodName(String name) {
        return verify(name, "[^A-Za-z0-9_]", false);
    }

    public static String verifyVariableName(String name) {
        return verify(name, "[^A-Za-z0-9_]", false);
    }

    private static String verify(String name, String regExp, boolean isUpper) {
        name = name.replaceAll(regExp, "");
        if (!name.equalsIgnoreCase("")) {
            if (isUpper) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            } else {
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
            }
        }
        name = verifyByKeyWords(name);
        return name;
    }


    public static String verifyPackageName(String name) {
        name = name.replaceAll(">", "");
        return name;
    }





}
