package me.childintime.childintime;

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
     * Main method, called on start.
     *
     * @param args Start up arguments.
     */
    public static void main(String[] args) {
        // Show a start message
        System.out.println(getFullName(true));

        // Create the application core
        Core core = new Core(false);

        // Initialize the core
        System.out.println("Initializing " + getFullName(true) + "...");
        core.init();
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
}
