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

package me.childintime.childintime.database.configuration;

import com.timvisee.yamlwrapper.ConfigurationSection;
import me.childintime.childintime.App;
import me.childintime.childintime.database.DatabaseDialect;
import me.childintime.childintime.database.DatabaseType;
import me.childintime.childintime.util.swing.ProgressDialog;

import java.io.File;

public class IntegratedDatabase extends AbstractDatabase implements Cloneable {

    /**
     * Default database file.
     */
    public static final File DEFAULT_FILE = new File(App.getDirectory(), "/databases/local.db");

    /**
     * Database file to use.
     */
    private File file;

    /**
     * Constructor.
     */
    public IntegratedDatabase() {
        // Construct the super
        super();

        // Set the name
        setName(getType().toString());
    }

    /**
     * Constructor.
     * This constructor allows cross-cloning between different kinds of abstract databases.
     *
     * @param other Other to cross-clone.
     */
    public IntegratedDatabase(AbstractDatabase other) {
        // Call the super
        super(other);

        // Make sure the other isn't null
        if(other == null)
            return;

        // Clone the fields if the type is the same
        if(getType().equals(other.getType())) {
            // Cast the type
            IntegratedDatabase otherIntegrated = (IntegratedDatabase) other;

            // Set the file
            this.setFile(otherIntegrated.getFile());
        }
    }

    /**
     * Constructor.
     *
     * @param file File.
     */
    public IntegratedDatabase(File file) {
        // Construct the super
        super();

        // Set the name
        setName(getType().toString());

        // Set the file
        this.file = file;
    }

    /**
     * Configuration section to load the database from.
     *
     * @param config Configuration section.
     */
    public IntegratedDatabase(ConfigurationSection config) {
        // Call the super
        super(config);

        // Fetch the file from the configuration
        String rawFile = config.getString("file", null);

        // Determine the file
        if(rawFile != null)
            this.file = new File(rawFile);
    }

    /**
     * Get the database file.
     *
     * @return Database file, or null if not configured.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Check whether a file has been configured.
     *
     * @return True if a file has been configured.
     */
    public boolean hasFile() {
        return this.file != null;
    }

    /**
     * Set the database file.
     *
     * @param file Database file.
     */
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void save(ConfigurationSection config) {
        // Save the file path
        config.set("file", file != null ? file.getAbsolutePath() : null);
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.INTEGRATED;
    }

    @Override
    public boolean isConfigured() {
        // Make sure the database file is configured
        return hasFile();
    }

    @Override
    public boolean prepare(ProgressDialog progressDialog) {
        // Set the status and show the progress dialog
        if(progressDialog != null) {
            // Set the status
            progressDialog.setStatus("Checking database configuration...");

            // Make the dialog visible
            if(!progressDialog.isVisible())
                progressDialog.setVisible(true);
        }

        // Make sure the database configuration is configured successfully
        if(!isConfigured())
            return false;

        // Looking for database file
        if(progressDialog != null)
            progressDialog.setStatus("Looking for database file...");

        // Get the database file
        File file = getFile();

        // Check whether the file exists
        if(file.exists())
            return true;

        // Get the parent directory of the file
        File parent = file.getParentFile();

        // Create the database directory
        if(progressDialog != null)
            progressDialog.setStatus("Creating required database files...");

        // Create the parent directory if it doesn't exist
        if(!parent.isDirectory())
            if(!parent.mkdirs())
                return false;

        // Return the result
        return true;
    }

    @Override
    public String getDatabaseDriverString() {
        return "org.sqlite.JDBC";
    }

    @Override
    public String getDatabaseConnectionUrl() {
        return "jdbc:sqlite:" + getFile().getAbsolutePath();
    }

    @Override
    public DatabaseDialect getDialect() {
        return DatabaseDialect.SQLITE;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public IntegratedDatabase clone() {
        return new IntegratedDatabase(this);
    }

    @Override
    public boolean equals(Object obj) {
        // Equal the parent
        if(!super.equals(obj))
            return false;

        // Get the integrated database instance
        IntegratedDatabase database = (IntegratedDatabase) obj;

        // Compare all fields
        return this.file == database.getFile();
    }
}
