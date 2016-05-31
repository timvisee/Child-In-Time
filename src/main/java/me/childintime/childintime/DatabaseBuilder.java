package me.childintime.childintime;

import me.childintime.childintime.database.connector.DatabaseConnector;
import me.childintime.childintime.util.swing.ProgressDialog;

import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseBuilder {

    /**
     * Database connector instance.
     */
    private DatabaseConnector databaseConnector;

    /**
     * Progress dialog instance.
     */
    private ProgressDialog progressDialog;

    /**
     * Define whether to dispose the progress dialog after we're finished.
     */
    private boolean disposeProgressDialog = false;

    /**
     * Constructor.
     *
     * @param databaseConnector Database connector instance.
     * @param progressDialog Progress dialog, or null.
     */
    public DatabaseBuilder(DatabaseConnector databaseConnector, ProgressDialog progressDialog) {
        // Set the fields
        this.databaseConnector = databaseConnector;
        this.progressDialog = progressDialog;

        // Create a progress dialog if it's null
        if(this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(null, App.APP_NAME, false, "Building database...", false);
            this.disposeProgressDialog = true;
        }
    }

    /**
     * Build the database.
     *
     * @return True on success, false on failure.
     */
    public boolean build() throws SQLException {
        // Show the progress dialog if it isn't visible yet
        this.progressDialog.setVisible(true);

        // Get the original progress status
        final boolean originalShowProgress = this.progressDialog.isShowProgress();
        final int originalProgressValue = this.progressDialog.getProgressValue();
        final int originalProgressMax = this.progressDialog.getProgressMax();

        // Configure the progress
        this.progressDialog.setProgressValue(0);
        this.progressDialog.setProgressMax(2);
        this.progressDialog.setShowProgress(true);

        // Create the user table
        createTableUser();
        this.progressDialog.increaseProgressValue();

        // Fill the user table
        fillTableUser();
        this.progressDialog.increaseProgressValue();

        // Revert the progress dialog state
        this.progressDialog.setProgressMax(originalProgressMax);
        this.progressDialog.setProgressValue(originalProgressValue);
        this.progressDialog.setShowProgress(originalShowProgress);

        // Dispose the progress dialog
        if(this.disposeProgressDialog)
            this.progressDialog.dispose();

        // Return the result
        return true;
    }

    /**
     * Create the user table.
     *
     * @throws SQLException
     */
    public void createTableUser() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `user` (" +
                        "    `id` INT NOT NULL AUTO_INCREMENT," +
                        "    `username` TEXT NOT NULL," +
                        "    `password_hash` TEXT NOT NULL," +
                        "    PRIMARY KEY (`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `user` (" +
                        "    `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    `username` TEXT NOT NULL," +
                        "    `password_hash` TEXT NOT NULL" +
                        ");"
                );
                break;
        }
    }

    /**
     * Fill the user table.
     *
     * @throws SQLException
     */
    public void fillTableUser() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "LOCK TABLES `user` WRITE;" +
                        "INSERT INTO `user` VALUES" +
                        "(NULL, 'admin', MD5('admin'));" +
                        "UNLOCK TABLES;"
                );
                break;

            case SQLITE:
                statement.execute(
                        "INSERT INTO `user` VALUES" +
                        "(NULL, 'admin', '21232f297a57a5a743894a0e4a801fc3');"
                );
                break;
        }
    }
}
