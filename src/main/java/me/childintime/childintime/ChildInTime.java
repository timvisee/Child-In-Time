package me.childintime.childintime;

public class ChildInTime {

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
        // Create the application core
        Core core = new Core(false);

        // Initialize the core
        core.init();
    }
}
