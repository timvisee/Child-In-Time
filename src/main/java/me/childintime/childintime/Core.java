package me.childintime.childintime;

import me.childintime.childintime.config.AppConfig;
import me.childintime.childintime.config.Config;
import me.childintime.childintime.database.connector.DatabaseConnector;
import me.childintime.childintime.database.configuration.DatabaseManager;
import me.childintime.childintime.gui.window.LoginDialog;
import me.childintime.childintime.util.swing.ProgressDialog;
import me.childintime.childintime.util.swing.SwingUtils;
import me.childintime.childintime.util.time.Profiler;

import javax.swing.*;
import java.io.IOException;
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
     * Database manager instance.
     */
    private DatabaseManager databaseManager;

    /**
     * Database instance.
     */
    private DatabaseConnector databaseConnector;

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

            // Destroy the core
            destroy();
            return;
        }

        // TODO: Load defaults if a databases configuration doesn't exist

        // Initialize and load the database manager
        this.progressDialog.setStatus("Loading database configuration...");
        this.databaseManager = new DatabaseManager();
        this.databaseManager.load();

        // Show the database selection dialog and handle the result
        this.progressDialog.setVisible(false);
        if(!LoginDialog.start(null)) {
            // Destroy the core
            destroy();
            return;
        }
        this.progressDialog.setVisible(true);

        // Set up the database connection
        // TODO: Clean this stuff up!
        this.databaseConnector = new DatabaseConnector();
        try {
            databaseConnector.getConnection();
            System.out.println("Connected to the database.");

        } catch(SQLException e) {
            // Print the stack trace
            e.printStackTrace();

            // Hide the progress dialog
            this.progressDialog.setVisible(false);

            // Show a message dialog
            JOptionPane.showMessageDialog(null, "Failed to connect to the application database.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);

            // Destroy the core
            destroy();
            return;
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

        // Show the progress dialog
        this.progressDialog.setStatus("Quitting...");
        this.progressDialog.setVisible(true);

        // Save the database configuration
        this.progressDialog.setStatus("Saving database configuration...");
        try {
            this.databaseManager.save();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save database configuration.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

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

    /**
     * Get the database manager instance.
     *
     * @return Database manager instance.
     */
    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }
}
