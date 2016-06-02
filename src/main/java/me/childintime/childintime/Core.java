package me.childintime.childintime;

import me.childintime.childintime.config.AppConfig;
import me.childintime.childintime.config.Config;
import me.childintime.childintime.database.configuration.AbstractDatabase;
import me.childintime.childintime.database.connector.DatabaseConnector;
import me.childintime.childintime.database.configuration.DatabaseManager;
import me.childintime.childintime.database.object.bodystate.BodyStateManager;
import me.childintime.childintime.database.object.group.GroupManager;
import me.childintime.childintime.database.object.measurement.MeasurementManager;
import me.childintime.childintime.database.object.parkour.ParkourManager;
import me.childintime.childintime.database.object.school.SchoolManager;
import me.childintime.childintime.database.object.student.StudentManager;
import me.childintime.childintime.database.object.teacher.TeacherManager;
import me.childintime.childintime.database.object.window.DatabaseObjectManagerDialog;
import me.childintime.childintime.gui.window.DashboardFrame;
import me.childintime.childintime.gui.window.LoginDialog;
import me.childintime.childintime.util.Platform;
import me.childintime.childintime.util.swing.ProgressDialog;
import me.childintime.childintime.util.swing.SwingUtils;
import me.childintime.childintime.util.time.Profiler;

import javax.swing.*;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Core {

    /**
     * Core instance, used for singleton.
     */
    private static Core instance = null;

    /**
     * Application startup arguments.
     */
    private String[] starupArgs = null;

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
     * Body state manager instance.
     */
    private BodyStateManager bodyStateManager;

    /**
     * Group manager instance.
     */
    private GroupManager groupManager;

    /**
     * Measurement manager instance.
     */
    private MeasurementManager measurementManager;

    /**
     * Parkour manager instance.
     */
    private ParkourManager parkourManager;

    /**
     * School manager instance.
     */
    private SchoolManager schoolManager;

    /**
     * Student manager instance.
     */
    private StudentManager studentManager;

    /**
     * Teacher manager instance.
     */
    private TeacherManager teacherManager;

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
     * @param args
     * @param init True to immediately initialize the core.
     */
    public Core(String[] args, boolean init) {
        // Set the core instance
        Core.instance = this;

        // Store the start up arguments
        this.starupArgs = args != null ? args : new String[]{};

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

        // Initialize the database object managers
        this.bodyStateManager = new BodyStateManager();
        this.groupManager = new GroupManager();
        this.measurementManager = new MeasurementManager();
        this.parkourManager = new ParkourManager();
        this.schoolManager = new SchoolManager();
        this.studentManager = new StudentManager();
        this.teacherManager = new TeacherManager();

        // Prepare the application data
        try {
            // Quit the application if the preparation failed
            if(!new InitialSetup(this.progressDialog).setup()) {
                destroy();
                return;
            }

        } catch (Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Show a fancy status message to the user
            JOptionPane.showMessageDialog(this.progressDialog,
                    "Failed to set up application data. The application must now quit.\n\n" +
                            "Error message: " + e.getMessage(),
                    App.APP_NAME, JOptionPane.ERROR_MESSAGE);

            // Destroy
            destroy();
            return;
        }

        // Initialize the configuration
        this.config = new AppConfig();

        // Load the configuration, and make sure it succeeds
        this.progressDialog.setStatus("Loading configuration...");
        if(!this.config.load(true)) {
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

        // Hide the progress dialog before showing the login dialog
        this.progressDialog.setVisible(false);

        // Show the login dialog, and get the selected database
        AbstractDatabase workingDatabase = LoginDialog.showDialog(null);

        // Make sure the login succeeded
        if(workingDatabase == null) {
            // Destroy the core
            destroy();
            return;
        }

        // Show the progress dialog again
        this.progressDialog.setVisible(true);

        // Set up the database connection
        // TODO: Clean this stuff up!
        this.databaseConnector = new DatabaseConnector(workingDatabase);

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

        // Check whether the database contains all required tables
        try {
            // Show a status message
            this.progressDialog.setStatus("Checking database...");

            // Fetch the database meta data
            DatabaseMetaData dbm = this.databaseConnector.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, "user", null);

            // Check whether the required table exists
            if(!tables.next()) {
                // Create a list with the buttons to show in the option dialog
                List<String> buttons = new ArrayList<>();
                buttons.add("Setup Database");
                buttons.add("Quit");

                // Reverse the button list if we're on a Mac OS X system
                if(Platform.isMacOsX())
                    Collections.reverse(buttons);

                // Show the option dialog
                final int option = JOptionPane.showOptionDialog(
                        this.progressDialog,
                        "The current database is empty and is not ready to be used.\n" +
                                "Would you like to set up the database using the default configuration?",
                        "Empty database",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        buttons.toArray(),
                        buttons.get(!Platform.isMacOsX() ? 0 : 1)
                );

                // Make sure the setup option is pressed
                if(option != (!Platform.isMacOsX() ? 0 : 1)) {
                    // TODO: Return to the login dialog, the database setup process is cancelled!
                    JOptionPane.showMessageDialog(this.progressDialog, "Should show login dialog again, not working yet!");
                    System.exit(0);
                }

                // Build the database
                // TODO: Handle this properly!
                try {
                    new DatabaseBuilder(this.databaseConnector, this.progressDialog).build();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        // Show a status message
        System.out.println("The application core has been started, took " + p.getTimeFormatted() + "!");

        // Hide the progress dialog
        this.progressDialog.setVisible(false);

        // TODO: Show a proper dashboard here, instead of this demo window!
        DashboardFrame dashboard = new DashboardFrame("My Dashboard");
        dashboard.setVisible(true);
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
        if(this.databaseManager != null) {
            this.progressDialog.setStatus("Saving database configuration...");
            try {
                this.databaseManager.save();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save database configuration.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
            }
        }

        // Destroy the database connection
        if(databaseConnector != null)
            this.databaseConnector.destroy();

        // Destroy the progress dialog if it hasn't been disposed yet
        if(this.progressDialog != null)
            this.progressDialog.dispose();

        // Show a status message
        System.out.println("The application core has been destroyed.");
    }

    /**
     * Get the application startup arguments.
     *
     * @return Application startup arguments.
     */
    public String[] getStarupArgs() {
        return this.starupArgs;
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

    /**
     * Get the database connector instance.
     *
     * @return Database connector instance.
     */
    public DatabaseConnector getDatabaseConnector() {
        return this.databaseConnector;
    }

    /**
     * Get the body state manager instance.
     *
     * @return Body state manager instance.
     */
    public BodyStateManager getBodyStateManager() {
        return this.bodyStateManager;
    }

    /**
     * Get the group manager instance.
     *
     * @return Group manager instance.
     */
    public GroupManager getGroupManager() {
        return this.groupManager;
    }

    /**
     * Get the measurement manager instance.
     *
     * @return Measurement manager instance.
     */
    public MeasurementManager getMeasurementManager() {
        return this.measurementManager;
    }

    /**
     * Get the parkour manager instance.
     *
     * @return Parkour manager instance.
     */
    public ParkourManager getParkourManager() {
        return this.parkourManager;
    }

    /**
     * Get the school manager instance.
     *
     * @return School manager instance.
     */
    public SchoolManager getSchoolManager() {
        return this.schoolManager;
    }

    /**
     * Get the student manager instance.
     *
     * @return Student manager instance.
     */
    public StudentManager getStudentManager() {
        return this.studentManager;
    }

    /**
     * Get the teacher manager instance.
     *
     * @return Teacher manager instance.
     */
    public TeacherManager getTeacherManager() {
        return this.teacherManager;
    }
}
