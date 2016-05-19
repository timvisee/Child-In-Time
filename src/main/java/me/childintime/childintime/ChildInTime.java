package me.childintime.childintime;

import me.childintime.childintime.util.swing.ProgressDialog;
import me.childintime.childintime.util.swing.SwingUtils;

public class ChildInTime {

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
