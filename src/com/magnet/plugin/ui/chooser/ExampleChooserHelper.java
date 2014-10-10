package com.magnet.plugin.ui.chooser;

import com.intellij.openapi.ui.Messages;
import com.magnet.langpack.builder.rest.parser.ExampleParser;
import com.magnet.langpack.builder.rest.parser.RestExampleModel;
import com.magnet.plugin.helpers.IOUtils;
import com.magnet.plugin.helpers.Logger;
import com.magnet.plugin.helpers.UIHelper;
import com.magnet.plugin.helpers.URLHelper;
import com.magnet.plugin.models.ExampleResource;
import com.magnet.plugin.models.ExamplesManifest;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helper for REST example chooser
 */
public class ExampleChooserHelper {

    private static final String MANIFEST_URL = ExampleResource.EXAMPLES_BASE_URL + "manifest.json";

    private static ExamplesManifest EXAMPLES_MANIFEST = null;

    public static String showExamplesDialog() {
        Map<String, ExampleResource> examples = getManifest().getExamples();
        String[] values = examples.keySet().toArray(new String[examples.size()]);
        String response = Messages.showEditableChooseDialog(
                "Enter a URL, path, or Github example",
                "Choose an REST example",
                Messages.getQuestionIcon(),
                values,
                "GoogleDistance",
                null);
        return response;
    }


    private static ExamplesManifest getManifest() {
        if (null != EXAMPLES_MANIFEST) {
            return EXAMPLES_MANIFEST;
        }
        InputStream infoStream = null;
        try {
            infoStream = URLHelper.loadUrl(MANIFEST_URL);
            String json = IOUtils.toString(infoStream);
            EXAMPLES_MANIFEST = new ExamplesManifest(json);
        } catch (Exception ex) {
            UIHelper.showErrorMessage("Couldn't parse manifest at URL: " + MANIFEST_URL);
            Logger.error(ExampleChooserHelper.class, ex.getMessage());
            EXAMPLES_MANIFEST = null;
        } finally {
            if (infoStream != null) {
                try {
                    infoStream.close();
                } catch (IOException e1) {
                    // ignore
                }
            }
        }
        return EXAMPLES_MANIFEST;
    }

    public static List<RestExampleModel> getControllersMethodsByName(String name) {
        String url = getExampleUrlByName(name);
        return getControllersMethodsByUrl(url);
    }

    private static String getExampleUrlByName(String name) {
        ExamplesManifest manifest = getManifest();
        ExampleResource example = manifest.getExample(name);
        return example.getUrl();

    }

    public static List<RestExampleModel> getControllersMethodsByUrl(String urlString) {
        ExampleParser parser = new ExampleParser();
        List<RestExampleModel> methodModels = new ArrayList<RestExampleModel>();
        try {
            URL url = new URL(urlString);
            methodModels.add(parser.parse(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // should not happen
        }
        return methodModels;
    }


}
