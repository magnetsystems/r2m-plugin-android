package com.magnet.plugin.models;

/**
 * Model for an example resource from r2m-examples repository
 */
public class ExampleResource {
    public static final String EXAMPLES_BASE_URL = "https://raw.githubusercontent.com/magnetsystems/r2m-examples/master/samples/";

    private final String name;
    private final String file;
    private final String description;

    public ExampleResource(String name, String file, String description) {
        this.name = name;
        this.file = file;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFile() {
        return file;
    }

    public String getUrl() {
        return EXAMPLES_BASE_URL + getFile();
    }

}
