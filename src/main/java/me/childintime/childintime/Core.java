package me.childintime.childintime;

import me.childintime.childintime.util.swing.SwingUtils;

public class Core {

    /**
     * This instance.
     * Used for singleton.
     */
    private static Core instance = null;

    /**
     * Get the instance.
     *
     * @return Instance.
     */
    public static Core getInstance() {
        return Core.instance;
    }

    /**
     * Constructor.
     *
     * @param init True to immediately initialize the core.
     */
    public Core(boolean init) {
        // Set the core instance
        Core.instance = this;

        // Initialize
        if(init)
            init();
    }

    /**
     * Initialize the application.
     */
    public void init() {
        // Set the Swing look and feel to the systems native
        SwingUtils.useNativeLookAndFeel();
    }
}
