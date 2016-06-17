package me.childintime.childintime;

import me.childintime.childintime.util.file.DirectoryUtils;

import java.io.File;

public class App {

    /**
     * Application name.
     */
    public static final String APP_NAME = "Child in Time";

    /**
     * Application version name.
     */
    public static final String APP_VERSION_NAME = "0.1";

    /**
     * Application version code.
     * This version code is increased for each version.
     */
    public static final int APP_VERSION_CODE = 1;

    /**
     * Name of the directory to use for this application.
     */
    public static final String APP_DIR_NAME = "ChildInTime";

    /**
     * Define whether debug mode is enabled.
     */
    public static final boolean APP_DEBUG = false;

    /**
     * Define whether to clean the application environment on startup to ensure a fresh application instance is started.
     * This must be disabled in production.
     */
    public static final boolean APP_CLEAN_ENVIRONMENT = APP_DEBUG;

    /**
     * Core instance.
     */
    private static Core core;

    /**
     * Main method, called on start.
     *
     * @param args Start up arguments.
     */
    public static void main(String[] args) {
        // Show a start message
        System.out.println(getFullName(true));

        // Create the application core
        App.core = new Core(args, false);

        // Initialize the core
        System.out.println("Initializing " + getFullName(true) + "...");
        App.core.init();
    }

    /**
     * Get the Core instance that is used by this application.
     *
     * @return Core instance.
     */
    public static Core getCore() {
        return App.core;
    }

    /**
     * Get the full application name.
     *
     * @param version True to include the version, false if not.
     *
     * @return Full application name.
     */
    public static String getFullName(boolean version) {
        // Create a string builder for the name
        StringBuilder name = new StringBuilder(APP_NAME);

        // Append the version if specified
        if(version)
            name.append(" v" + APP_VERSION_NAME + " (" + APP_VERSION_CODE + ")");

        // Return the full name
        return name.toString();
    }

    /**
     * Get the application data directory.
     *
     * @return The application data directory.
     */
    public static File getDirectory() {
        return new File(DirectoryUtils.getAppDataDirectory(), App.APP_DIR_NAME);
    }

    /**
     * Get the application data, data directory.
     *
     * @return The application data, data directory.
     */
    public static File getDataDirectory() {
        return new File(getDirectory(), "data");
    }
}
