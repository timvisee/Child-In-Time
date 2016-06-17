/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package me.childintime.childintime;

import com.apple.osx.adapter.OSXAdapter;
import me.childintime.childintime.config.AppConfig;
import me.childintime.childintime.config.Config;
import me.childintime.childintime.database.configuration.AbstractDatabase;
import me.childintime.childintime.database.configuration.DatabaseManager;
import me.childintime.childintime.database.connector.DatabaseConnector;
import me.childintime.childintime.database.entity.spec.bodystate.BodyStateManager;
import me.childintime.childintime.database.entity.spec.couple.groupteacher.GroupTeacherManager;
import me.childintime.childintime.database.entity.spec.couple.studentsport.StudentSportManager;
import me.childintime.childintime.database.entity.spec.group.GroupManager;
import me.childintime.childintime.database.entity.spec.measurement.MeasurementManager;
import me.childintime.childintime.database.entity.spec.parkour.ParkourManager;
import me.childintime.childintime.database.entity.spec.school.SchoolManager;
import me.childintime.childintime.database.entity.spec.sport.SportManager;
import me.childintime.childintime.database.entity.spec.student.StudentManager;
import me.childintime.childintime.database.entity.spec.teacher.TeacherManager;
import me.childintime.childintime.database.entity.spec.user.UserManager;
import me.childintime.childintime.ui.window.AboutDialog;
import me.childintime.childintime.ui.window.DashboardFrame;
import me.childintime.childintime.ui.window.LoginDialog;
import me.childintime.childintime.user.Authenticator;
import me.childintime.childintime.util.Platform;
import me.childintime.childintime.util.swing.ProgressDialog;
import me.childintime.childintime.util.swing.SwingUtils;
import me.childintime.childintime.util.time.Profiler;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

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
     * User manager instance.
     */
    private StudentManager studentManager;

    /**
     * Teacher manager instance.
     */
    private TeacherManager teacherManager;

    /**
     * Group teacher couple manager instance.
     */
    private GroupTeacherManager groupTeacherManager;

    /**
     * Sport manager.
     */
    private SportManager sportManager;

    /**
     * Student sport couple manager.
     */
    private StudentSportManager studentSportManager;

    /**
     * User manager instance.
     */
    private UserManager userManager;

    /**
     * Progress dialog instance.
     */
    private ProgressDialog progressDialog;

    /**
     * Authenticator.
     */
    private Authenticator authenticator = new Authenticator();

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

        // Set some Mac OS X properties
        if(Platform.isMacOsX()) {
            System.out.println("Configuring application for Mac OS X...");
            System.setProperty("com.apple.macos.useScreenMenuBar", "true");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", App.APP_NAME);
            System.setProperty("apple.awt.application.name", App.APP_NAME);
        }

//        // Enable hardware accelerated rendering using OpenGL for Java2D on non-OSX platforms, including AWT and Swing
//        if(!Platform.isMacOsX()) {
//            System.out.println("Enabling hardware acceleration...");
//            System.setProperty("sun.java2d.opengl", "true");
//        }

        // Set the Swing look and feel to the systems native
        SwingUtils.useNativeLookAndFeel();

        // Initialize and show the progress dialog
        this.progressDialog = new ProgressDialog(null, App.APP_NAME, false, "Initializing...", true);

        // Set up Mac OS X native menu items
        if(Platform.isMacOsX()) {
            // Show a status message
            this.progressDialog.setStatus("Setting up Mac OS X menus...");

            // Attach the about dialog to the Mac OS X about menu item
            try {
                OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[]) null));
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        // Initialize the entity managers
        this.bodyStateManager = new BodyStateManager();
        this.groupManager = new GroupManager();
        this.measurementManager = new MeasurementManager();
        this.parkourManager = new ParkourManager();
        this.schoolManager = new SchoolManager();
        this.studentManager = new StudentManager();
        this.teacherManager = new TeacherManager();
        this.userManager = new UserManager();
        this.groupTeacherManager = new GroupTeacherManager();
        this.sportManager = new SportManager();
        this.studentSportManager = new StudentSportManager();

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

        // Show a status message
        System.out.println("The application core has been started, took " + p.getTimeFormatted() + "!");

        // Hide the progress dialog
        this.progressDialog.setVisible(false);

        // Show the dashboard
        DashboardFrame dashboard = new DashboardFrame();
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

        // Save the configuration
        if(this.config != null) {
            // Show a status message
            this.progressDialog.setStatus("Saving configuration...");

            // Save the configuration
            getConfig().save();
        }

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
     * @return User manager instance.
     */
    public StudentManager getStudentManager() {
        return this.studentManager;
    }

    /**
     * Get the user manager instance.
     *
     * @return User manager instance.
     */
    public UserManager getUserManager() {
        return this.userManager;
    }

    /**
     * Get the teacher manager instance.
     *
     * @return Teacher manager instance.
     */
    public TeacherManager getTeacherManager() {
        return this.teacherManager;
    }

    /**
     * Group teacher couple manager.
     *
     * @return Group teacher couple manager.
     */
    public GroupTeacherManager getGroupTeacherCoupleManager() {
        return this.groupTeacherManager;
    }

    /**
     * Get the sport manager.
     *
     * @return Sport manager.
     */
    public SportManager getSportManager() {
        return this.sportManager;
    }

    /**
     * Get the student sport couple manager.
     *
     * @return Student sport couple manager.
     */
    public StudentSportManager getStudentSportCoupleManager() {
        return this.studentSportManager;
    }

    /**
     * Get the authenticator.
     *
     * @return Authenticator.
     */
    public Authenticator getAuthenticator() {
        return this.authenticator;
    }

    /**
     * Show the about dialog.
     */
    public void about() {
        new AboutDialog(null, true);
    }
}
