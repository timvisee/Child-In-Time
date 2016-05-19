package me.childintime.childintime;

import me.childintime.childintime.util.swing.ProgressDialog;

public class ChildInTime {

    /**
     * Main method, called on start.
     *
     * @param args Start up arguments.
     */
    public static void main(String[] args) {
        // Show a status message
        System.out.println("Child in Time has started.");

        // Create a progress dialog (test)
        ProgressDialog dialog = new ProgressDialog(null, "MyApplication", "MyStatus");
        dialog.setVisible(true);
    }
}
