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
        this.progressDialog.setProgressMax(35);
        this.progressDialog.setShowProgress(true);

        // Prepare the database
        this.progressDialog.setStatus("Preparing database...");
        prepare();
        this.progressDialog.increaseProgressValue();

        // Create the user table
        this.progressDialog.setStatus("Configuring database...");
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

        // Create the student table
        createTableStudent();
        this.progressDialog.increaseProgressValue();

        // Create the body state table
        createTableBodyState();
        this.progressDialog.increaseProgressValue();

        // Create the parkour table
        createTableParkour();
        this.progressDialog.increaseProgressValue();

        // Create the measurement table
        createTableMeasurement();
        this.progressDialog.increaseProgressValue();

        // Create the group/teacher table
        createTableGroupTeacher();
        this.progressDialog.increaseProgressValue();

        // Create all required meta data tables
        createMetaDataTables("user");
        createMetaDataTables("student");
        createMetaDataTables("teacher");
        createMetaDataTables("group");
        createMetaDataTables("school");
        createMetaDataTables("bodystate");
        createMetaDataTables("parkour");
        createMetaDataTables("measurement");

        // Fill the user table
        this.progressDialog.setStatus("Inserting database data...");
        fillTableUser();
        this.progressDialog.increaseProgressValue();

        // Fill the school table
        fillTableSchool();
        this.progressDialog.increaseProgressValue();

        // Fill the teacher table
        fillTableTeacher();
        this.progressDialog.increaseProgressValue();

        // Fill the group table
        fillTableGroup();
        this.progressDialog.increaseProgressValue();

        // Fill the student table
        fillTableStudent();
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
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `username` TEXT NOT NULL," +
                        "  `password_hash` TEXT NOT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `user` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `username` TEXT NOT NULL," +
                        "  `password_hash` TEXT NOT NULL" +
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
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `name` TEXT NOT NULL," +
                        "  `commune` TEXT NOT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `school` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `name` TEXT NOT NULL," +
                        "  `commune` TEXT NOT NULL" +
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
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `first_name` TEXT NOT NULL," +
                        "  `last_name` TEXT NOT NULL," +
                        "  `gender` TINYINT NOT NULL," +
                        "  `is_gym` TINYINT NOT NULL," +
                        "  `school_id` INT NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)," +
                        "  CHECK(`gender` = 0 OR `gender` = 1)," +
                        "  CHECK(`is_gym` = 0 OR `is_gym` = 1)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `teacher` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `first_name` TEXT NOT NULL," +
                        "  `last_name` TEXT NOT NULL," +
                        "  `gender` TINYINT NOT NULL," +
                        "  `is_gym` TINYINT NOT NULL," +
                        "  `school_id` INT NOT NULL," +
                        "  FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)," +
                        "  CHECK(`gender` = 0 OR `gender` = 1)," +
                        "  CHECK(`is_gym` = 0 OR `is_gym` = 1)" +
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
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `name` TEXT NOT NULL," +
                        "  `school_id` INT NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `group` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `name` TEXT NOT NULL," +
                        "  `school_id` INTEGER NOT NULL," +
                        "  FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the student table.
     *
     * @throws SQLException
     */
    public void createTableStudent() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `student` (" +
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `first_name` TEXT NOT NULL," +
                        "  `last_name` TEXT NOT NULL," +
                        "  `gender` TINYINT NOT NULL," +
                        "  `birthdate` DATE NOT NULL," +
                        "  `group_id` INT NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`group_id`) REFERENCES `group`(`id`)," +
                        "  CHECK (`gender` = 0 or `gender` = 1)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `student` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `first_name` TEXT NOT NULL," +
                        "  `last_name` TEXT NOT NULL," +
                        "  `gender` INTEGER NOT NULL," +
                        "  `birthdate` DATE NOT NULL," +
                        "  `group_id` INTEGER NOT NULL," +
                        "  FOREIGN KEY (`group_id`) REFERENCES `group`(`id`)," +
                        "  CHECK(`gender` = 0 OR `gender` = 1)" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the body state table.
     *
     * @throws SQLException
     */
    public void createTableBodyState() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `bodystate` (" +
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `date` DATE NOT NULL," +
                        "  `length` SMALLINT NOT NULL," +
                        "  `weight` SMALLINT NOT NULL," +
                        "  `student_id` INT NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`student_id`) REFERENCES `student`(`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `bodystate` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `date` DATE NOT NULL," +
                        "  `length` INTEGER NOT NULL," +
                        "  `weight` INTEGER NOT NULL," +
                        "  `student_id` INTEGER NOT NULL," +
                        "  FOREIGN KEY (`student_id`) REFERENCES `student`(`id`)" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the parkour table.
     *
     * @throws SQLException
     */
    public void createTableParkour() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `bodystate` (" +
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `date` DATE NOT NULL," +
                        "  `length` SMALLINT NOT NULL," +
                        "  `weight` SMALLINT NOT NULL," +
                        "  `student_id` INT NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`student_id`) REFERENCES `student`(`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `parkour` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `description` TEXT" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the measurement table.
     *
     * @throws SQLException
     */
    public void createTableMeasurement() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `measurement` (" +
                        "  `id`         INT  NOT NULL AUTO_INCREMENT," +
                        "  `date`       DATE NOT NULL," +
                        "  `time`       INT  NOT NULL," +
                        "  `parkour_id` INT  NOT NULL," +
                        "  `student_id` INT  NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)," +
                        "  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `measurement` (" +
                        "  `id`         INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `date`       DATE    NOT NULL," +
                        "  `time`       INTEGER NOT NULL," +
                        "  `parkour_id` INTEGER NOT NULL," +
                        "  `student_id` INTEGER NOT NULL," +
                        "  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)," +
                        "  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the group/teacher table.
     *
     * @throws SQLException
     */
    public void createTableGroupTeacher() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `group_teacher` (" +
                        "  `group_id`   INT NOT NULL," +
                        "  `teacher_id` INT NOT NULL," +
                        "  PRIMARY KEY (`group_id`, `teacher_id`)," +
                        "  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)," +
                        "  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `group_teacher` (" +
                        "  `group_id`   INTEGER NOT NULL," +
                        "  `teacher_id` INTEGER NOT NULL," +
                        "  PRIMARY KEY (`group_id`, `teacher_id`)," +
                        "  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)," +
                        "  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the meta data tables for the given table.
     *
     * @param tableName Name of the table to create the meta data tables for.
     *
     * @throws SQLException
     */
    public void createMetaDataTables(String tableName) throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                // Create the meta data table
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `" + tableName + "_meta_data` (" +
                        "  `id`         INT      NOT NULL AUTO_INCREMENT," +
                        "  `field`      TEXT     NOT NULL," +
                        "  `type`       SMALLINT NOT NULL," +
                        "  `value`      TEXT     NULL," +
                        "  `" + tableName + "_id` INT      NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`" + tableName + "_id`) REFERENCES `" + tableName + "` (`id`)" +
                        ");"
                );
                this.progressDialog.increaseProgressValue();

                // Create the meta data fields table
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `" + tableName + "_meta_field` (" +
                        "  `id`         INT               NOT NULL AUTO_INCREMENT," +
                        "  `name`       TEXT              NOT NULL," +
                        "  `type`       SMALLINT          NOT NULL," +
                        "  `default`    TEXT              NULL," +
                        "  `allow_null` TINYINT DEFAULT 1 NOT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ");"
                );
                this.progressDialog.increaseProgressValue();

                // Create the meta data values table
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `" + tableName + "_meta_value` (" +
                        "  `id`       INT  NOT NULL AUTO_INCREMENT," +
                        "  `value`    TEXT NOT NULL," +
                        "  `field_id` INT  NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`field_id`) REFERENCES `" + tableName + "_meta_field` (`id`)" +
                        ");"
                );
                this.progressDialog.increaseProgressValue();
                break;

            case SQLITE:
                // Create the meta data table
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `" + tableName + "_meta_data` (" +
                        "  `id`      INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `field`   TEXT    NOT NULL," +
                        "  `type`    INTEGER NOT NULL," +
                        "  `value`   TEXT," +
                        "  `" + tableName + "_id` INTEGER NOT NULL," +
                        "  FOREIGN KEY (`" + tableName + "_id`) REFERENCES `" + tableName + "` (`id`)" +
                        ");"
                );
                this.progressDialog.increaseProgressValue();

                // Create the meta data fields table
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `" + tableName + "_meta_field` (" +
                        "  `id`         INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `name`       TEXT    NOT NULL," +
                        "  `type`       INTEGER NOT NULL," +
                        "  `default`    TEXT," +
                        "  `allow_null` INTEGER             DEFAULT 1 NOT NULL" +
                        ");"
                );
                this.progressDialog.increaseProgressValue();

                // Create the meta data values table
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `" + tableName + "_meta_value` (" +
                        "  `id`       INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `value`    TEXT    NOT NULL," +
                        "  `field_id` INTEGER NOT NULL," +
                        "  FOREIGN KEY (`field_id`) REFERENCES `" + tableName + "_meta_field` (`id`)" +
                        ");"
                );
                this.progressDialog.increaseProgressValue();
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
                        "  (NULL, 'admin', MD5('admin'));" +
                        "UNLOCK TABLES;"
                );
                break;

            case SQLITE:
                statement.execute(
                        "INSERT INTO `user` VALUES" +
                        "  (NULL, 'admin', '21232f297a57a5a743894a0e4a801fc3');"
                );
                break;
        }
    }

    /**
     * Fill the school table.
     *
     * @throws SQLException
     */
    public void fillTableSchool() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "LOCK TABLES `school` WRITE;" +
                        "INSERT INTO `school` VALUES" +
                        "  (NULL, 'De Wegwijzer', 'Alphen aan den Rijn')," +
                        "  (NULL, 'De Hobbitburcht', 'Amsterdam')," +
                        "  (NULL, 'De Stromen', 'Den Haag');" +
                        "UNLOCK TABLES;"
                );
                break;

            case SQLITE:
                statement.execute(
                        "INSERT INTO `school` VALUES" +
                        "  (NULL, 'De Wegwijzer', 'Alphen aan den Rijn')," +
                        "  (NULL, 'De Hobbitburcht', 'Amsterdam')," +
                        "  (NULL, 'De Stromen', 'Den Haag');"
                );
                break;
        }
    }

    /**
     * Fill the teacher table.
     *
     * @throws SQLException
     */
    public void fillTableTeacher() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "LOCK TABLES `teacher` WRITE;" +
                        "INSERT INTO `teacher` VALUES" +
                        "  (NULL, 'Henk', 'Hoornald', 1, 1, 1)," +
                        "  (NULL, 'Henry', 'Hunter', 1, 1, 1)," +
                        "  (NULL, 'Phillip', 'Ward', 1, 0, 1)," +
                        "  (NULL, 'Carlos', 'Ryan', 1, 0, 1)," +
                        "  (NULL, 'Karen', 'Diaz', 0, 1, 2)," +
                        "  (NULL, 'Paula', 'Black', 0, 0, 2)," +
                        "  (NULL, 'Julie', 'Sims', 0, 0, 2)," +
                        "  (NULL, 'Laura', 'Garcia', 0, 1, 3)," +
                        "  (NULL, 'Mark', 'Romero', 1, 0, 3)," +
                        "  (NULL, 'Ashley', 'Jacobs', 0, 0, 3)," +
                        "  (NULL, 'Linda', 'Jackson', 0, 0, 3);" +
                        "UNLOCK TABLES;"
                );
                break;

            case SQLITE:
                statement.execute(
                        "INSERT INTO `teacher` VALUES" +
                        "  (NULL, 'Henk', 'Hoornald', 1, 1, 1)," +
                        "  (NULL, 'Henry', 'Hunter', 1, 1, 1)," +
                        "  (NULL, 'Phillip', 'Ward', 1, 0, 1)," +
                        "  (NULL, 'Carlos', 'Ryan', 1, 0, 1)," +
                        "  (NULL, 'Karen', 'Diaz', 0, 1, 2)," +
                        "  (NULL, 'Paula', 'Black', 0, 0, 2)," +
                        "  (NULL, 'Julie', 'Sims', 0, 0, 2)," +
                        "  (NULL, 'Laura', 'Garcia', 0, 1, 3)," +
                        "  (NULL, 'Mark', 'Romero', 1, 0, 3)," +
                        "  (NULL, 'Ashley', 'Jacobs', 0, 0, 3)," +
                        "  (NULL, 'Linda', 'Jackson', 0, 0, 3);"
                );
                break;
        }
    }

    /**
     * Fill the group table.
     *
     * @throws SQLException
     */
    public void fillTableGroup() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "LOCK TABLES `group` WRITE;" +
                        "INSERT INTO `group` VALUES" +
                        "  (NULL, 'Groep 1', 1)," +
                        "  (NULL, 'Groep 2', 1)," +
                        "  (NULL, 'Groep 3', 1)," +
                        "  (NULL, 'Groep 1', 2)," +
                        "  (NULL, 'Groep 2', 2)," +
                        "  (NULL, 'Groep 3', 2)," +
                        "  (NULL, 'Groep 1', 3)," +
                        "  (NULL, 'Groep 2', 3)," +
                        "  (NULL, 'Groep 3', 3);" +
                        "UNLOCK TABLES;"
                );
                break;

            case SQLITE:
                statement.execute(
                        "INSERT INTO `group` VALUES" +
                        "  (NULL, 'Groep 1', 1)," +
                        "  (NULL, 'Groep 2', 1)," +
                        "  (NULL, 'Groep 3', 1)," +
                        "  (NULL, 'Groep 1', 2)," +
                        "  (NULL, 'Groep 2', 2)," +
                        "  (NULL, 'Groep 3', 2)," +
                        "  (NULL, 'Groep 1', 3)," +
                        "  (NULL, 'Groep 2', 3)," +
                        "  (NULL, 'Groep 3', 3);"
                );
                break;
        }
    }

    /**
     * Fill the student table.
     *
     * @throws SQLException
     */
    public void fillTableStudent() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "LOCK TABLES `student` WRITE;" +
                        "INSERT INTO `student` VALUES" +
                        "  (NULL, 'George', 'Barnes', 1, '2014-04-17', 1)," +
                        "  (NULL, 'Robin', 'Hughes', 0, '2011-01-10', 1)," +
                        "  (NULL, 'Anne', 'Diaz', 0, '2015-02-26', 2)," +
                        "  (NULL, 'Sandra', 'Knight', 0, '2013-08-27', 2)," +
                        "  (NULL, 'Terry', 'Morales', 1, '2015-09-06', 3)," +
                        "  (NULL, 'Jimmy', 'Smith', 1, '2013-04-07', 3)," +
                        "  (NULL, 'Nicholas', 'Dunn', 1, '2013-09-01', 4)," +
                        "  (NULL, 'Raymond', 'Franklin', 1, '2015-12-13', 4)," +
                        "  (NULL, 'Maria', 'Barnes', 0, '2005-08-28', 5)," +
                        "  (NULL, 'Wayne', 'Graham', 1, '2010-10-20', 5)," +
                        "  (NULL, 'Kathy', 'James', 0, '2006-06-05', 6)," +
                        "  (NULL, 'Evelyn', 'Elliott', 0, '2008-11-26', 6)," +
                        "  (NULL, 'Barbara', 'Parker', 0, '2008-05-03', 7)," +
                        "  (NULL, 'Mildred', 'Sanders', 0, '2009-08-22', 7)," +
                        "  (NULL, 'Jason', 'Ross', 1, '2007-07-31', 8)," +
                        "  (NULL, 'Anna', 'Porter', 0, '2008-12-21', 8)," +
                        "  (NULL, 'Karen', 'Wells', 0, '2011-10-05', 8)," +
                        "  (NULL, 'Jonathan', 'Ferguson', 1, '2014-02-06', 9)," +
                        "  (NULL, 'Kathryn', 'Carr', 0, '2006-03-06', 9)," +
                        "  (NULL, 'Sean', 'Reid', 1, '2009-04-20', 9);" +
                        "UNLOCK TABLES;"
                );
                break;

            case SQLITE:
                statement.execute(
                        "INSERT INTO `student` VALUES" +
                        "  (NULL, 'George', 'Barnes', 1, '2014-04-17', 1)," +
                        "  (NULL, 'Robin', 'Hughes', 0, '2011-01-10', 1)," +
                        "  (NULL, 'Anne', 'Diaz', 0, '2015-02-26', 2)," +
                        "  (NULL, 'Sandra', 'Knight', 0, '2013-08-27', 2)," +
                        "  (NULL, 'Terry', 'Morales', 1, '2015-09-06', 3)," +
                        "  (NULL, 'Jimmy', 'Smith', 1, '2013-04-07', 3)," +
                        "  (NULL, 'Nicholas', 'Dunn', 1, '2013-09-01', 4)," +
                        "  (NULL, 'Raymond', 'Franklin', 1, '2015-12-13', 4)," +
                        "  (NULL, 'Maria', 'Barnes', 0, '2005-08-28', 5)," +
                        "  (NULL, 'Wayne', 'Graham', 1, '2010-10-20', 5)," +
                        "  (NULL, 'Kathy', 'James', 0, '2006-06-05', 6)," +
                        "  (NULL, 'Evelyn', 'Elliott', 0, '2008-11-26', 6)," +
                        "  (NULL, 'Barbara', 'Parker', 0, '2008-05-03', 7)," +
                        "  (NULL, 'Mildred', 'Sanders', 0, '2009-08-22', 7)," +
                        "  (NULL, 'Jason', 'Ross', 1, '2007-07-31', 8)," +
                        "  (NULL, 'Anna', 'Porter', 0, '2008-12-21', 8)," +
                        "  (NULL, 'Karen', 'Wells', 0, '2011-10-05', 8)," +
                        "  (NULL, 'Jonathan', 'Ferguson', 1, '2014-02-06', 9)," +
                        "  (NULL, 'Kathryn', 'Carr', 0, '2006-03-06', 9)," +
                        "  (NULL, 'Sean', 'Reid', 1, '2009-04-20', 9);"
                );
                break;
        }
    }
}
