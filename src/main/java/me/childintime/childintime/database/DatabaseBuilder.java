package me.childintime.childintime.database;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import me.childintime.childintime.App;
import me.childintime.childintime.database.connector.DatabaseConnector;
import me.childintime.childintime.util.swing.ProgressDialog;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
     * Date format to use for the database.
     */
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Java faker, used to generate fake user data.
     */
    private final Faker faker = new Faker(Locale.ENGLISH);

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
        this.progressDialog.setProgressMax(42);
        this.progressDialog.setShowProgress(true);

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
        this.progressDialog.setStatus("Generating fake data...");
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

        // Fill the body state table
        fillTableBodyState();
        this.progressDialog.increaseProgressValue();

        // Fill the parkour table
        fillTableParkour();
        this.progressDialog.increaseProgressValue();

        // Fill the measurement table
        fillTableMeasurement();
        this.progressDialog.increaseProgressValue();

        // Fill the group/teacher table
        fillTableGroupTeacher();
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
                        "  FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)" +
                                " ON DELETE RESTRICT," +
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
                        "  FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)" +
                                " ON DELETE RESTRICT," +
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
                                " ON DELETE RESTRICT" +
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
                                " ON DELETE RESTRICT" +
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
                        "  FOREIGN KEY (`group_id`) REFERENCES `group`(`id`)" +
                                " ON DELETE RESTRICT," +
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
                        "  FOREIGN KEY (`group_id`) REFERENCES `group`(`id`)" +
                                " ON DELETE RESTRICT," +
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
                        "  `weight` INT NOT NULL," +
                        "  `student_id` INT NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`student_id`) REFERENCES `student`(`id`)" +
                                " ON DELETE RESTRICT" +
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
                                " ON DELETE RESTRICT" +
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
                        "CREATE TABLE IF NOT EXISTS `parkour` (" +
                        "  `id`          INT  NOT NULL AUTO_INCREMENT," +
                        "  `description` TEXT NULL," +
                        "  PRIMARY KEY (`id`)" +
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
                        "  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)" +
                                " ON DELETE RESTRICT," +
                        "  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)" +
                                " ON DELETE RESTRICT" +
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
                        "  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)" +
                                " ON DELETE RESTRICT," +
                        "  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)" +
                                " ON DELETE RESTRICT" +
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
                        "  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)" +
                                " ON DELETE RESTRICT," +
                        "  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)" +
                                " ON DELETE RESTRICT" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `group_teacher` (" +
                        "  `group_id`   INTEGER NOT NULL," +
                        "  `teacher_id` INTEGER NOT NULL," +
                        "  PRIMARY KEY (`group_id`, `teacher_id`)," +
                        "  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)" +
                                " ON DELETE RESTRICT," +
                        "  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)" +
                                " ON DELETE RESTRICT" +
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
                                " ON DELETE RESTRICT" +
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
                                " ON DELETE RESTRICT" +
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
                                " ON DELETE RESTRICT" +
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
                                " ON DELETE RESTRICT" +
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
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `user` VALUES (NULL, ?, ?);"
        );

        // Fill the prepared statement
        prepared.setString(1, "admin");
        prepared.setString(2, "21232f297a57a5a743894a0e4a801fc3"); // MD5('admin')

        // Execute the prepared statement
        prepared.execute();
    }

    /**
     * Fill the school table.
     *
     * @throws SQLException
     */
    public void fillTableSchool() throws SQLException {
        // Create a new progress dialog specifically for generating the current data
        ProgressDialog progressDialog = new ProgressDialog(this.progressDialog, "Generating fake data...", false, "Generating fake schools...", true);

        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `school` VALUES (NULL, ?, ?);"
        );

        // Determine the number of schools to generate
        final int schoolCount = this.faker.number().numberBetween(3, 6);

        // Configure the progress dialog
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(schoolCount);

        // Loop for the determined count
        for(int i = 0; i < schoolCount; i++) {
            // Fill the prepared statement
            prepared.setString(1, this.faker.university().name());
            prepared.setString(2, this.faker.address().city());

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();
    }

    /**
     * Fill the teacher table.
     *
     * @throws SQLException
     */
    public void fillTableTeacher() throws SQLException {
        // Create a new progress dialog specifically for generating the current data
        ProgressDialog progressDialog = new ProgressDialog(this.progressDialog, "Generating fake data...", false, "Generating fake teachers...", true);

        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `teacher` VALUES (NULL, ?, ?, ?, ?, ?);"
        );

        // Determine the number of students to generate
        final int teacherCount = this.faker.number().numberBetween(10, 15);

        // Configure the progress dialog
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(teacherCount);

        // Loop for the determined count
        for(int i = 0; i < teacherCount; i++) {
            // Generate a name
            Name name = this.faker.name();

            // Fill the prepared statement
            prepared.setString(1, name.firstName());
            prepared.setString(2, name.lastName());
            prepared.setInt(3, this.faker.number().numberBetween(0, 2));
            prepared.setInt(4, this.faker.number().numberBetween(0, 2));
            prepared.setInt(5, this.faker.number().numberBetween(1, 4));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();
    }

    /**
     * Fill the group table.
     *
     * @throws SQLException
     */
    public void fillTableGroup() throws SQLException {
        // Create a new progress dialog specifically for generating the current data
        ProgressDialog progressDialog = new ProgressDialog(this.progressDialog, "Generating fake data...", false, "Generating fake groups...", true);

        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `group` VALUES (NULL, ?, ?);"
        );

        // Configure the progress dialog
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(9);

        // Insert default groups
        for(int i = 0; i < 9; i++) {
            // Fill the prepared statement
            prepared.setString(1, "Group " + ((i % 3) + 1));
            prepared.setInt(2, (i / 3) + 1);

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();
    }

    /**
     * Fill the student table.
     *
     * @throws SQLException
     */
    public void fillTableStudent() throws SQLException {
        // Create a new progress dialog specifically for generating the current data
        ProgressDialog progressDialog = new ProgressDialog(this.progressDialog, "Generating fake data...", false, "Generating fake students...", true);

        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `student` VALUES (NULL, ?, ?, ?, ?, ?);"
        );

        // Determine the number of students to generate
        final int studentCount = this.faker.number().numberBetween(50, 100);

        // Configure the progress dialog
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(studentCount);

        // Loop for the determined count
        for(int i = 0; i < studentCount; i++) {
            // Generate a name
            Name name = this.faker.name();

            // Fill the prepared statement
            prepared.setString(1, name.firstName());
            prepared.setString(2, name.lastName());
            prepared.setInt(3, this.faker.number().numberBetween(0, 2));
            prepared.setString(4, dateFormat.format(this.faker.date().past(20 * 356, TimeUnit.DAYS)));
            prepared.setInt(5, this.faker.number().numberBetween(1, 10));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();
    }

    /**
     * Fill the body state table.
     *
     * @throws SQLException
     */
    public void fillTableBodyState() throws SQLException {
        // Create a new progress dialog specifically for generating the current data
        ProgressDialog progressDialog = new ProgressDialog(this.progressDialog, "Generating fake data...", false, "Generating fake body states...", true);

        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `bodystate` VALUES (NULL, ?, ?, ?, ?);"
        );

        // Determine the number of students to generate
        final int bodyStateCount = this.faker.number().numberBetween(50, 100);

        // Configure the progress dialog
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(bodyStateCount);

        // Loop for the determined count
        for(int i = 0; i < bodyStateCount; i++) {
            // Fill the prepared statement
            prepared.setString(1, dateFormat.format(this.faker.date().past(3 * 356, TimeUnit.DAYS)));
            prepared.setInt(2, this.faker.number().numberBetween(110, 160));
            prepared.setInt(3, this.faker.number().numberBetween(30000, 55000));
            prepared.setInt(4, this.faker.number().numberBetween(1, 51));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();
    }

    /**
     * Fill the parkour table.
     *
     * @throws SQLException
     */
    public void fillTableParkour() throws SQLException {
        // Create a new progress dialog specifically for generating the current data
        ProgressDialog progressDialog = new ProgressDialog(this.progressDialog, "Generating fake data...", false, "Generating fake parkours...", true);

        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `parkour` VALUES (NULL, ?);"
        );

        // Configure the progress dialog
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(3);

        // Insert default parkours
        for(int i = 0; i < 3; i++) {
            // Fill the prepared statement
            prepared.setString(1, "Parkour " + (i + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();
    }

    /**
     * Fill the measurement table.
     *
     * @throws SQLException
     */
    public void fillTableMeasurement() throws SQLException {
        // Create a new progress dialog specifically for generating the current data
        ProgressDialog progressDialog = new ProgressDialog(this.progressDialog, "Generating fake data...", false, "Generating fake measurements...", true);

        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `measurement` VALUES (NULL, ?, ?, ?, ?);"
        );

        // Determine the number of students to generate
        final int measurementCount = this.faker.number().numberBetween(50, 100);

        // Configure the progress dialog
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(measurementCount);

        // Loop for the determined count
        for(int i = 0; i < measurementCount; i++) {
            // Fill the prepared statement
            prepared.setString(1, dateFormat.format(this.faker.date().past(3 * 356, TimeUnit.DAYS)));
            prepared.setInt(2, this.faker.number().numberBetween(16000, 30000));
            prepared.setInt(3, this.faker.number().numberBetween(1, 4));
            prepared.setInt(4, this.faker.number().numberBetween(1, 51));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();
    }

    /**
     * Fill the group/teacher table.
     *
     * @throws SQLException
     */
    public void fillTableGroupTeacher() throws SQLException {
        // Create a new progress dialog specifically for generating the current data
        ProgressDialog progressDialog = new ProgressDialog(this.progressDialog, "Generating fake data...", false, "Coupling group teachers...", true);

        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `group_teacher` VALUES (?, ?);"
        );

        // Configure the progress dialog
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(9);

        // Insert default group teachers
        for(int i = 0; i < 9; i++) {
            // Fill the prepared statement
            prepared.setInt(1, i + 1);
            prepared.setInt(2, (i % 3) + 1);

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();
    }
}
