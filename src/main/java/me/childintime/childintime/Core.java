package me.childintime.childintime;

import me.childintime.childintime.util.swing.ProgressDialog;
import me.childintime.childintime.util.swing.SwingUtils;

public class Core {

    /**
     * This instance.
     * Used for singleton.
     */
    private static Core instance = null;

    /**
     * Progress dialog instance.
     */
    private ProgressDialog progressDialog;

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
        // Show a status message
        System.out.println("Initializing " + ChildInTime.getFullName(true) + "...");

        // Set the Swing look and feel to the systems native
        SwingUtils.useNativeLookAndFeel();

        // Initialize and show the progress dialog
        this.progressDialog = new ProgressDialog(null, ChildInTime.APP_NAME, false, "Initializing...", true);

        // Show a status message
        System.out.println("Initialized successfully!");

        // Hide the progress dialog
        this.progressDialog.setVisible(false);
    }

    /**
     * Get the progress dialog instance.
     *
     * @return Progres dialog.
     */
    public ProgressDialog getProgressDialog() {
        return this.progressDialog;
    }
}
