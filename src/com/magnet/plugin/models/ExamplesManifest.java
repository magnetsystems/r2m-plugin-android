package com.magnet.plugin.models;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Model for example manifest from r2m-examples
 */
public class ExamplesManifest {

    private final Map<String, ExampleResource> examples;
    private static final String FILE_KEY = "file";
    private static final String DESCRIPTION_KEY = "description";


    public ExamplesManifest(String json) throws IOException {
        this.examples = new HashMap<String, ExampleResource>();

        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            // single example
            reader.beginObject();
            String file = null;
            String description = null;
            while (reader.hasNext()) {
                String key = reader.nextName();
                if (key.equals(FILE_KEY)) {
                    file = reader.nextString();
                } else if (key.equals(DESCRIPTION_KEY)) {
                    description = reader.nextString();
                }
            }
            reader.endObject();
            examples.put(name, new ExampleResource(name, file, description));
        }

    }

    public Map<String, ExampleResource> getExamples() {
        return Collections.unmodifiableMap(examples);
    }

    public ExampleResource getExample(String name) {
        return examples.get(name);
    }
}
