package me.childintime.childintime.config;

import me.childintime.childintime.App;

import java.io.File;

public class AppConfig extends Config {

    /**
     * Configuration file name.
     */
    public static final String CONFIG_FILE_NAME = "config.yml";

    /**
     * Configuration file.
     */
    public static final File CONFIG_FILE = new File(App.getDirectory(), CONFIG_FILE_NAME);

    /**
     * Constructor.
     * Note: this automatically uses the default configuration file of the application.
     */
    public AppConfig() {
        // Create a configuration file instance
        this(CONFIG_FILE);
    }

    /**
     * Constructor.
     *
     * @param configFile Configuration file.
     */
    public AppConfig(File configFile) {
        super(configFile);
    }
}
