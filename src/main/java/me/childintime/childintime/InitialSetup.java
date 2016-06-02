package me.childintime.childintime;

import me.childintime.childintime.util.Platform;
import me.childintime.childintime.util.swing.ProgressDialog;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InitialSetup {

    /**
     * Startup flag to hide the environment cleaning warning.
     */
    public static final String FLAG_HIDE_CLEAN_ENVIRONMENT_WARNING = "-hideCleanEnvironmentWarning";

    /**
     * Progress dialog instance.
     */
    private ProgressDialog progressDialog;

    /**
     * Defines whether something has changed.
     */
    private boolean changed = false;

    /**
     * Defines whether the confirmation dialog has been shown to the user.
     */
    private boolean confirmDialogShown = false;

    /**
     * Defines whether the user agree'd if the confirmation dialog was shown.
     */
    private boolean confirmDialogAgree = false;

    /**
     * Constructor.
     */
    public InitialSetup(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    /**
     * Do the initial application setup.
     *
     * @boolean True on success, false if the initial setup failed and the application should quit.
     */
    public boolean setup() {
        // Clean the environment
        cleanApplicationEnvironment();

        // Set up the application directory
        if(!setupApplicationDirectory())
            return false;

        // Set up the integrated databases
        if(!setupIntegratedDatabases())
            return false;

        // Finish
        finish();

        // Everything seems to be fine, return true
        return true;
    }

    /**
     * Clean and refresh the application environment for developers.
     * This deletes all application files and configurations from previous versions.
     * This ensures that a fresh application instance is used for development.
     */
    private void cleanApplicationEnvironment() {
        // Show a status message
        this.progressDialog.setStatus("Confirming environment cleanup...");

        // Make sure the clean environment feature is enabled
        if(!App.APP_CLEAN_ENVIRONMENT)
            return;

        // Show a warning
        if(!Arrays.asList(Core.getInstance().getStarupArgs()).contains(FLAG_HIDE_CLEAN_ENVIRONMENT_WARNING)) {
            // Create a list with the buttons to show in the option dialog
            List<String> buttons = new ArrayList<>();
            buttons.add("Clean Environment");
            buttons.add("Keep Environment");

            // Reverse the button list if we're on a Mac OS X system
            if(Platform.isMacOsX())
                Collections.reverse(buttons);

            // Show the option dialog
            final int option = JOptionPane.showOptionDialog(
                                this.progressDialog,
                                "The developer option Clean Environment is enabled.\n\n" +
                                        "Would you like to clean your application environment?\n\n" +
                                        "Cleaning the environment will delete all previous application files and configurations,\n" +
                                        "to ensure that you're using a fresh application instance for development.\n\n" +
                                        "This feature must be disabled in production.",
                                App.APP_NAME + " - Developer mode - Environment cleanup",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                    null,
                    buttons.toArray(),
                    buttons.get(!Platform.isMacOsX() ? 0 : 1)
            );

            // Make sure the clean option is pressed
            if(option != (!Platform.isMacOsX() ? 0 : 1))
                return;
        }

        // Show a status message
        this.progressDialog.setStatus("Refreshing application environment...");

        // Delete the application directory
        try {
            if(App.getDirectory().exists())
                FileUtils.deleteDirectory(App.getDirectory());

        } catch(IOException e) {
            showError("Failed to refresh application environment.");
            e.printStackTrace();
        }
    }

    /**
     * Set up the application directory.
     *
     * @return True if succeed, false if not and the application should quit.
     */
    private boolean setupApplicationDirectory() {
        // Show a status message
        this.progressDialog.setStatus("Setting up application directory...");

        // Get the application directory
        File directory = App.getDirectory();

        // Check whether the directory exists
        if(!directory.exists()) {
            // Set the changed flag
            this.changed = true;

            // Verify the confirmation state
            if(!getConfirmationState())
                return false;

            // Create the base directory
            if(!directory.mkdirs()) {
                // Show an error, and return
                showError("Failed to create application directory.");
                return false;
            }
        }

        // Everything seems to be fine, return true
        return true;
    }

    /**
     * Set up the integrated databases.
     *
     * @return True if succeed, false if not and the application should quit.
     */
    public boolean setupIntegratedDatabases() {
        // Show a status message
        this.progressDialog.setStatus("Setting up integrated databases...");

        // Get the default integrated databases directory
        File directory = new File(App.getDirectory(), "databases");

        // Check whether the default integrated databases directory exists
        if(!directory.isDirectory()) {
            // Set the changed flag
            this.changed = true;

            // Verify the confirmation state
            if(!getConfirmationState())
                return false;

            // Create the default directory
            if(!directory.mkdirs()) {
                // Show an error, and return
                showError("Failed to set up integrated databases.");
                return false;
            }
        }

        // Everything seems to be fine, return true
        return true;
    }

    /**
     * Finish the initial setup process.
     */
    public void finish() {
        // Explicitly wait for the JVM to release all created files if anything has changed
        if(this.changed) {
            this.progressDialog.setStatus("Waiting on JVM...");
            try {
                Thread.sleep(500);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the user's confirmation state.
     * This will automatically show the confirmation dialog if it hasn't been shown yet.
     *
     * @return True if confirmed, false if not.
     */
    private boolean getConfirmationState() {
        // Show the confirmation dialog if it hasn't been shown yet
        if(!this.confirmDialogShown) {
            // Show the dialog
            showConfirmationDialog();

            // Set the showed dialog flag
            this.confirmDialogShown = true;
        }

        // Return the result
        return this.confirmDialogAgree;
    }

    /**
     * Show the confirmation dialog.
     *
     * @return True if the user agree'd, false if not.
     */
    private boolean showConfirmationDialog() {
        // Create a list with the buttons to show in the option dialog
        List<String> buttons = new ArrayList<>();
        buttons.add("Continue");
        buttons.add("Quit");

        // Reverse the button list if we're on a Mac OS X system
        if(Platform.isMacOsX())
            Collections.reverse(buttons);

        // Show the option dialog
        final int option = JOptionPane.showOptionDialog(
                this.progressDialog,
                        "This is the first time you're using " + App.APP_NAME + " on this system.\n" +
                        "Some application files are required to be installed.\n" +
                        "Please Continue and allow us to set things up for you.",
                App.APP_NAME + " - Initial setup",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                buttons.toArray(),
                buttons.get(!Platform.isMacOsX() ? 0 : 1)
        );

        // Determine, set and return the result
        this.confirmDialogAgree = (option == (!Platform.isMacOsX() ? 0 : 1));
        return this.confirmDialogAgree;
    }

    /**
     * Show a proper error.
     *
     * @param message Error message.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this.progressDialog,
                "An error occurred while setting up the application files. " + App.APP_NAME + " will now quit.\n\n" +
                        "Detailed error message:\n" +
                        message,
                App.APP_NAME + "Initial setup error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
