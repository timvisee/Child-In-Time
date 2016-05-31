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
        this.progressDialog.setProgressMax(6);
        this.progressDialog.setShowProgress(true);

        // Prepare the database
        prepare();
        this.progressDialog.increaseProgressValue();

        // Create the user table
        createTableUser();
        this.progressDialog.increaseProgressValue();

        // Create the school table
        createTableSchool();
        this.progressDialog.increaseProgressValue();

        // Create the teacher table
        createTableTeacher();
        this.progressDialog.increaseProgressValue();

        // Create the group table
        createTableGroup();
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
     * Prepare the database.
     *
     * @throws SQLException
     */
    public void prepare() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;" +
                        "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;" +
                        "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;" +
                        "/*!40101 SET NAMES utf8 */;"
                );
                break;

            case SQLITE:
                break;
        }
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
     * Create the school table.
     *
     * @throws SQLException
     */
    public void createTableSchool() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `school` (" +
                        "    `id` INT NOT NULL AUTO_INCREMENT," +
                        "    `name` TEXT NOT NULL," +
                        "    `commune` TEXT NOT NULL," +
                        "    PRIMARY KEY (`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `school` (" +
                        "    `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    `name` TEXT NOT NULL," +
                        "    `commune` TEXT NOT NULL" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the teacher table.
     *
     * @throws SQLException
     */
    public void createTableTeacher() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `teacher` (" +
                        "    `id` INT NOT NULL AUTO_INCREMENT," +
                        "    `first_name` TEXT NOT NULL," +
                        "    `last_name` TEXT NOT NULL," +
                        "    `gender` TINYINT NOT NULL," +
                        "    `is_gym` TINYINT NOT NULL," +
                        "    `school_id` INT NOT NULL," +
                        "    PRIMARY KEY (`id`)," +
                        "    FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)," +
                        "    CHECK(`gender` = 0 OR `gender` = 1)," +
                        "    CHECK(`is_gym` = 0 OR `is_gym` = 1)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `teacher` (" +
                        "    `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    `first_name` TEXT NOT NULL," +
                        "    `last_name` TEXT NOT NULL," +
                        "    `gender` TINYINT NOT NULL," +
                        "    `is_gym` TINYINT NOT NULL," +
                        "    `school_id` INT NOT NULL," +
                        "    FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)," +
                        "    CHECK(`gender` = 0 OR `gender` = 1)," +
                        "    CHECK(`is_gym` = 0 OR `is_gym` = 1)" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the group table.
     *
     * @throws SQLException
     */
    public void createTableGroup() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `group` (" +
                        "    `id` INT NOT NULL AUTO_INCREMENT," +
                        "    `name` TEXT NOT NULL," +
                        "    `school_id` INT NOT NULL," +
                        "    PRIMARY KEY (`id`)," +
                        "    FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `group` (" +
                        "    `id` INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        "    `name` TEXT NOT NULL," +
                        "    `school_id` INTEGER NOT NULL," +
                        "    FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)" +
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
                        "    (NULL, 'admin', MD5('admin'));" +
                        "UNLOCK TABLES;"
                );
                break;

            case SQLITE:
                statement.execute(
                        "INSERT INTO `user` VALUES" +
                        "    (NULL, 'admin', '21232f297a57a5a743894a0e4a801fc3');"
                );
                break;
        }
    }
}
