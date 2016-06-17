/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package me.childintime.childintime.config;

import com.timvisee.yamlwrapper.configuration.YamlConfiguration;
import me.childintime.childintime.util.time.Profiler;

import java.io.File;
import java.io.IOException;

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
        return load(true);
    }

    /**
     * Load the configuration file.
     *
     * @param create Create the configuration file if it doesn't exist.
     *
     * @return True if succeed, false on failure.
     */
    public boolean load(boolean create) {
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
            // Show an error if the file shouldn't be created either
            if(!create) {
                // Show a status message
                System.out.println("Failed to load configuration file, file doesn't exist.");

                // Return the result
                return false;
            }

            // Show a status message
            System.out.println("Configuration file doesn't exist, creating it now...");

            // Create the parent directory if it doesn't exist
            if(!this.configFile.getParentFile().isDirectory() && !this.configFile.getParentFile().mkdirs()) {
                // Show a status message
                System.out.println("Failed to create the configuration directory.");

                // Return the result
                return false;
            }

            // Set the configuration object
            this.config = new YamlConfiguration();

            // Return the result
            return true;
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
        return load(true);
    }

    /**
     * Save the configuration.
     *
     * @return True on success, false on failure.
     */
    public boolean save() {
        return save(getConfigFile());
    }

    /**
     * Save the configuration.
     *
     * @param configFile Configuration file.
     *
     * @return True on success, false on failure.
     */
    public boolean save(File configFile) {
        try {
            // Save
            this.config.save(configFile);

            // Return the result
            return true;

        } catch(IOException e) {
            // Print the stack trace
            e.printStackTrace();

            // Return the result
            return false;
        }
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
