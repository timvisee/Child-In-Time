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

package me.childintime.childintime.database;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import me.childintime.childintime.App;
import me.childintime.childintime.database.connector.DatabaseConnector;
import me.childintime.childintime.hash.HashUtil;
import me.childintime.childintime.util.swing.ProgressDialog;

import java.security.NoSuchAlgorithmException;
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
     * The username of the administrative user.
     */
    private String adminUser = "admin";

    /**
     * The password of the administrative user.
     */
    private String adminPassword = null;

    /**
     * Defines whether to generate any sample data.
     */
    public boolean generateSampleData = true;

    /**
     * The number of students to generate.
     */
    public int generateStudentCount;

    /**
     * The number of teachers to generate.
     */
    public int generateTeacherCount;

    /**
     * The number of schools to generate.
     */
    public int generateSchoolCount;

    /**
     * The number of groups to generate.
     */
    public int generateGroupCount;

    /**
     * The number of measurements to generate.
     */
    public int generateMeasurementCount;

    /**
     * The number of bodystates to generate.
     */
    public int generateBodyStateCount;

    /**
     * The number of parkours to generate.
     */
    public int generateParkourCount;

    /**
     * The number of sports to generate.
     */
    public int generateSportCount;

    /**
     * The number of group & teacher couples to generate.
     */
    public int generateGroupTeacherCount;

    /**
     * The number of student & sport couples to generate.
     */
    public int generateStudentSportCount;

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
     * Set the administrative user credentials.
     *
     * @param user Administrative user username.
     * @param password Administrative user password.
     */
    public void setAdminUser(String user, String password) {
        this.adminUser = user;
        this.adminPassword = password;
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

        // Create all tables
        createTables();

        // Create the default user
        this.progressDialog.setShowProgress(false);
        this.progressDialog.setStatus("Creating administrative user...");
        addTableUser(this.adminUser, this.adminPassword);

        // Generate the sample data
        if(this.generateSampleData)
            generateSampleData();

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
     * Generate sample data.
     *
     * @throws SQLException
     */
    public void generateSampleData() throws SQLException {
        // Calculate the total number of entities to generate and set the progress dialog max
        this.progressDialog.setProgressMax(generateSchoolCount +
                generateTeacherCount +
                generateGroupCount +
                generateStudentCount +
                generateBodyStateCount +
                generateParkourCount +
                generateMeasurementCount +
                generateStudentSportCount +
                generateGroupTeacherCount
        );

        // Show the progress
        this.progressDialog.setProgressValue(0);
        this.progressDialog.setShowProgress(true);

        // Fill the school table
        this.progressDialog.setStatus("Generating schools...");
        fillTableSchool();

        // Fill the teacher table
        this.progressDialog.setStatus("Generating teachers...");
        fillTableTeacher();

        // Fill the group table
        this.progressDialog.setStatus("Generating groups...");
        fillTableGroup();

        // Fill the student table
        this.progressDialog.setStatus("Generating students...");
        fillTableStudent();

        // Fill the body state table
        this.progressDialog.setStatus("Generating body states...");
        fillTableBodyState();

        // Fill the parkour table
        this.progressDialog.setStatus("Generating parkours...");
        fillTableParkour();

        // Fill the measurement table
        this.progressDialog.setStatus("Generating measurements...");
        fillTableMeasurement();

        // Fill the sports table
        this.progressDialog.setStatus("Generating sports...");
        fillTableSport();

        // Fill the group/teacher table
        this.progressDialog.setStatus("Generating group/teacher couples...");
        fillTableGroupTeacher();

        // Fill the student/sport table
        this.progressDialog.setStatus("Generating student/sport couples...");
        fillTableStudentSport();
    }

    /**
     * Create the tables.
     * @throws SQLException
     */
    public void createTables() throws SQLException {
        // Configure the progress
        this.progressDialog.setProgressValue(0);
        this.progressDialog.setProgressMax(11);
        this.progressDialog.setShowProgress(true);

        // Create the user table
        this.progressDialog.setStatus("Building database...");
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

        // Create the sport table
        createTableSport();
        this.progressDialog.increaseProgressValue();

        // Create the student/sport table
        createTableStudentSport();
        this.progressDialog.increaseProgressValue();
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
                        "  `permission_level` INTEGER NOT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `user` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `username` TEXT NOT NULL," +
                        "  `password_hash` TEXT NOT NULL," +
                        "  `permission_level` SMALLINT NOT NULL" +
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
                                " ON DELETE CASCADE," +
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
                                " ON DELETE CASCADE," +
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
                                " ON DELETE CASCADE" +
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
                                " ON DELETE CASCADE" +
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
                                " ON DELETE CASCADE," +
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
                                " ON DELETE CASCADE," +
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
                                " ON DELETE CASCADE" +
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
                                " ON DELETE CASCADE" +
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
                                " ON DELETE CASCADE," +
                        "  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)" +
                                " ON DELETE CASCADE" +
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
                                " ON DELETE CASCADE," +
                        "  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)" +
                                " ON DELETE CASCADE" +
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
                                "  `id`         INT NOT NULL AUTO_INCREMENT," +
                                "  `group_id`   INT NOT NULL," +
                                "  `teacher_id` INT NOT NULL," +
                                "  PRIMARY KEY (`id`)," +
                                "  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)" +
                                " ON DELETE CASCADE," +
                                "  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)" +
                                " ON DELETE CASCADE" +
                                ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `group_teacher` (" +
                                "  `id`         INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "  `group_id`   INTEGER NOT NULL," +
                                "  `teacher_id` INTEGER NOT NULL," +
                                "  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)" +
                                " ON DELETE CASCADE," +
                                "  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)" +
                                " ON DELETE CASCADE" +
                                ");"
                );
                break;
        }
    }

    /**
     * Create the student/sport table.
     *
     * @throws SQLException
     */
    public void createTableSport() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `sport` (" +
                        "  `id`         INT  NOT NULL AUTO_INCREMENT," +
                        "  `name`       TEXT NOT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `sport` (" +
                        "  `id`         INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `name`       TEXT NOT NULL" +
                        ");"
                );
                break;
        }
    }

    /**
     * Create the student/sport table.
     *
     * @throws SQLException
     */
    public void createTableStudentSport() throws SQLException {
        // Create a statement
        Statement statement = this.databaseConnector.getConnection().createStatement();

        // Execute the table create query
        switch(this.databaseConnector.getDialect()) {
            case MYSQL:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `student_sport` (" +
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `student_id`   INT NOT NULL," +
                        "  `sport_id` INT NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)" +
                        "    ON DELETE CASCADE," +
                        "  FOREIGN KEY (`sport_id`) REFERENCES `sport` (`id`)" +
                        "    ON DELETE CASCADE" +
                        ");"
                );
                break;

            case SQLITE:
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS `student_sport` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `student_id`   INTEGER NOT NULL," +
                        "  `sport_id` INTEGER NOT NULL," +
                        "  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)" +
                        "  ON DELETE CASCADE," +
                        "  FOREIGN KEY (`sport_id`) REFERENCES `sport` (`id`)" +
                        "  ON DELETE CASCADE" +
                        ");"
                );
                break;
        }
    }

    /**
     * Add an administrative user to the users table.
     *
     * @throws SQLException
     */
    public void addTableUser(String user, String password) throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `user` VALUES (NULL, ?, ?, ?);"
        );

        // Fill the prepared statement
        prepared.setString(1, user);
        prepared.setInt(3, 0);

        // Set the password hash
        try {
            prepared.setString(2, HashUtil.hash(password));
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Execute the prepared statement
        prepared.execute();
    }

    /**
     * Fill the school table.
     *
     * @throws SQLException
     */
    public void fillTableSchool() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `school` VALUES (NULL, ?, ?);"
        );

        // Loop for the determined count
        for(int i = 0; i < this.generateSchoolCount; i++) {
            // Fill the prepared statement
            prepared.setString(1, this.faker.university().name());
            prepared.setString(2, this.faker.address().city());

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the teacher table.
     *
     * @throws SQLException
     */
    public void fillTableTeacher() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `teacher` VALUES (NULL, ?, ?, ?, ?, ?);"
        );

        // Loop for the determined count
        for(int i = 0; i < this.generateTeacherCount; i++) {
            // Generate a name
            Name name = this.faker.name();

            // Fill the prepared statement
            prepared.setString(1, name.firstName());
            prepared.setString(2, name.lastName());
            prepared.setInt(3, this.faker.number().numberBetween(0, 2));
            prepared.setInt(4, this.faker.number().numberBetween(0, 2));
            prepared.setInt(5, this.faker.number().numberBetween(1, this.generateSchoolCount + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the group table.
     *
     * @throws SQLException
     */
    public void fillTableGroup() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `group` VALUES (NULL, ?, ?);"
        );

        // Insert default groups
        for(int i = 0; i < this.generateGroupCount; i++) {
            // Fill the prepared statement
            prepared.setString(1, "Group " + (i + 1));
            prepared.setInt(2, this.faker.number().numberBetween(1, this.generateSchoolCount + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the student table.
     *
     * @throws SQLException
     */
    public void fillTableStudent() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `student` VALUES (NULL, ?, ?, ?, ?, ?);"
        );

        // Loop for the determined count
        for(int i = 0; i < this.generateStudentCount; i++) {
            // Generate a name
            Name name = this.faker.name();

            // Fill the prepared statement
            prepared.setString(1, name.firstName());
            prepared.setString(2, name.lastName());
            prepared.setInt(3, this.faker.number().numberBetween(0, 2));
            prepared.setString(4, dateFormat.format(this.faker.date().past(20 * 356, TimeUnit.DAYS)));
            prepared.setInt(5, this.faker.number().numberBetween(1, this.generateGroupCount + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the body state table.
     *
     * @throws SQLException
     */
    public void fillTableBodyState() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `bodystate` VALUES (NULL, ?, ?, ?, ?);"
        );

        // Loop for the determined count
        for(int i = 0; i < this.generateBodyStateCount; i++) {
            // Fill the prepared statement
            prepared.setString(1, dateFormat.format(this.faker.date().past(6 * 356, TimeUnit.DAYS)));
            prepared.setInt(2, this.faker.number().numberBetween(175, 180));
            prepared.setInt(3, this.faker.number().numberBetween(65000, 75000));
            prepared.setInt(4, this.faker.number().numberBetween(1, this.generateStudentCount + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the parkour table.
     *
     * @throws SQLException
     */
    public void fillTableParkour() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `parkour` VALUES (NULL, ?);"
        );

        // Insert default parkours
        for(int i = 0; i < this.generateParkourCount; i++) {
            // Fill the prepared statement
            prepared.setString(1, "Parkour " + (i + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the measurement table.
     *
     * @throws SQLException
     */
    public void fillTableMeasurement() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `measurement` VALUES (NULL, ?, ?, ?, ?);"
        );

        // Loop for the determined count
        for(int i = 0; i < this.generateMeasurementCount; i++) {
            // Fill the prepared statement
            prepared.setString(1, dateFormat.format(this.faker.date().past(3 * 356, TimeUnit.DAYS)));
            prepared.setInt(2, this.faker.number().numberBetween(16000, 30000));
            prepared.setInt(3, this.faker.number().numberBetween(1, this.generateParkourCount + 1));
            prepared.setInt(4, this.faker.number().numberBetween(1, this.generateStudentCount + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the group/teacher table.
     *
     * @throws SQLException
     */
    public void fillTableGroupTeacher() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `group_teacher` VALUES (NULL, ?, ?);"
        );

        // Insert default group teachers
        for(int i = 0; i < this.generateGroupTeacherCount; i++) {
            // Fill the prepared statement
            prepared.setInt(1, this.faker.number().numberBetween(1, this.generateGroupCount + 1));
            prepared.setInt(2, this.faker.number().numberBetween(1, this.generateTeacherCount + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the sport table.
     *
     * @throws SQLException
     */
    public void fillTableSport() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `sport` VALUES (NULL, ?);"
        );

        // Loop for the determined count
        for(int i = 0; i < this.generateSportCount; i++) {
            // Generate a sport
            final String sport = this.faker.team().sport();

            // Fill the prepared statement
            prepared.setString(1, sport.substring(0, 1).toUpperCase() + sport.substring(1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }

    /**
     * Fill the student/sport table.
     *
     * @throws SQLException
     */
    public void fillTableStudentSport() throws SQLException {
        // Create a prepared statement
        PreparedStatement prepared = this.databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `student_sport` VALUES (NULL, ?, ?);"
        );

        // Insert default group teachers
        for(int i = 0; i < this.generateStudentSportCount; i++) {
            // Fill the prepared statement
            prepared.setInt(1, this.faker.number().numberBetween(1, this.generateStudentCount + 1));
            prepared.setInt(2, this.faker.number().numberBetween(1, this.generateSportCount + 1));

            // Execute the prepared statement
            prepared.execute();

            // Increase the progress status
            this.progressDialog.increaseProgressValue();
        }
    }
}
