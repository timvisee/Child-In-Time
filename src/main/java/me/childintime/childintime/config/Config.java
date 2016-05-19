package me.childintime.childintime.config;

import com.timvisee.yamlwrapper.configuration.YamlConfiguration;
import me.childintime.childintime.util.time.Profiler;

import java.io.File;

public class Config {

    /**
     * Configuration file.
     */
    private File configFile;

    /**
     * The configuration instance.
     */
    private YamlConfiguration config;

    /**
     * Constructor.
     *
     * @param configFile Configuration file.
     */
    public Config(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Get the configuration file.
     *
     * @return Configuration file.
     */
    public File getConfigFile() {
        return this.configFile;
    }

    /**
     * Set the configuartion file.
     *
     * @param configFile Configuration file.
     */
    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Load the configuration file.
     *
     * @return True if succeed, false on failure.
     */
    public boolean load() {
        // Start a profiler
        Profiler p = new Profiler(true);

        // Make sure the configuration file is configured
        if(this.configFile == null) {
            // Show a status message
            System.out.println("Failed to load configuration file, file not configured.");

            // Return the result
            return false;
        }

        // Show a status message
        System.out.println("Loading configuration from " + this.configFile.getAbsolutePath());

        // Make sure the configuration file exists
        if (!this.configFile.exists()) {
            // Show a status message
            System.out.println("Failed to load configuration file, file doesn't exist.");

            // Return the result
            return false;
        }

        // Load the configuration file and make sure it's valid
        if((this.config = YamlConfiguration.loadConfiguration(this.configFile)) == null) {
            // Show a status message
            System.out.println("Failed to load configuration file.");

            // Return the result
            return false;
        }

        // File loaded successfully, show status message
        System.out.println("Configuration loaded successfully, took " + p.getTimeFormatted() + "!");

        // Return the result
        return true;
    }

    /**
     * Load the configuration file.
     *
     * @param configFile Configuration file to load.
     *
     * @return True if succeed, false on failure.
     */
    public boolean load(File configFile) {
        // Set the configuration file
        setConfigFile(configFile);

        // Load the configuration file and return the result
        return load();
    }

    /**
     * Check whether the configuration is loaded.
     *
     * @return True if the configuration is loaded, false if not.
     */
    public boolean isLoaded() {
        return this.config != null;
    }

    /**
     * Unload any loaded configuration.
     */
    public void unload() {
        this.config = null;
    }

    /**
     * Get the configuration.
     * Note: the configuration must have been loaded.
     *
     * @return Configuration, or null if not loaded.
     */
    public YamlConfiguration getConfig() {
        return config;
    }
}
