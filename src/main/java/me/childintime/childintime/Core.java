package me.childintime.childintime;

import me.childintime.childintime.config.AppConfig;
import me.childintime.childintime.config.Config;
import me.childintime.childintime.util.swing.ProgressDialog;
import me.childintime.childintime.util.swing.SwingUtils;
import me.childintime.childintime.util.time.Profiler;

import javax.swing.*;
import java.sql.SQLException;

public class Core {

    /**
     * This instance.
     * Used for singleton.
     */
    private static Core instance = null;

    /**
     * Configuration instance.
     */
    private Config config;

    /**
     * Database instance.
     */
    private Database database;

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
        // Start a profiler to measure the initialization time
        Profiler p = new Profiler(true);

        // Show a status message
        System.out.println("Starting application core...");

        // Set the Swing look and feel to the systems native
        SwingUtils.useNativeLookAndFeel();

        // Initialize and show the progress dialog
        this.progressDialog = new ProgressDialog(null, App.APP_NAME, false, "Initializing...", true);

        // Initialize the configuration
        this.config = new AppConfig();

        // Load the configuration, and make sure it succeeds
        this.progressDialog.setStatus("Loading configuration...");
        if(!this.config.load()) {
            // Hide the progress dialog
            this.progressDialog.setVisible(false);

            // Show a message dialog
            JOptionPane.showMessageDialog(null, "Failed to load application configuration.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);

            // TODO: Properly close the application!
        }

        // Connect to the database
        this.progressDialog.setStatus("Connecting to the database...");

        // TODO: Set up the database manager here
        // TODO: Connect the database manager

        this.database = new Database();

        try {
            database.getConnection();
            this.progressDialog.setStatus("Connected to the database...");
        } catch(SQLException e) {
            this.progressDialog.setStatus("Could not connect to the database...");
        }

        // Show a status message
        System.out.println("The application core has been started, took " + p.getTimeFormatted() + "!");

        // Hide the progress dialog
        this.progressDialog.setVisible(false);
    }

    /**
     * Destroy the instance after it has been initialized.
     * This safely closes and destroy all IO handles, and initialized instances.
     */
    public void destroy() {
        // Show a status message
        System.out.println("Destroying application core...");

        // TODO: Destroy the database manager

        // Destroy the progress dialog if it hasn't been disposed yet
        if(this.progressDialog != null)
            this.progressDialog.dispose();

        // Show a status message
        System.out.println("The application core has been destroyed.");
    }

    /**
     * Get the configuration instance.
     *
     * @return Configuration instance.
     */
    public Config getConfig() {
        return this.config;
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
