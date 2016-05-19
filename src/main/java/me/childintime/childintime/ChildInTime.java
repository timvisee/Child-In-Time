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
        // Show a status message
        System.out.println("Child in Time has started.");

        // Use the system's look and feel
        SwingUtils.useNativeLookAndFeel();

        // Create a progress dialog (test)
        ProgressDialog dialog = new ProgressDialog(null, "MyApplication", true);
        dialog.setVisible(true);
    }
}
