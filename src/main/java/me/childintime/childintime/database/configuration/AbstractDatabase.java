package me.childintime.childintime.database.configuration;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;
import me.childintime.childintime.database.DatabaseType;
import me.childintime.childintime.database.connector.DatabaseConnector;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public abstract class AbstractDatabase implements Cloneable {

    /**
     * Name.
     */
    private String name;

    /**
     * Constructor.
     */
    public AbstractDatabase() { }

    /**
     * Constructor.
     * This constructor allows cross-cloning between different kinds of abstract databases.
     *
     * @param other Other to cross-clone.
     */
    public AbstractDatabase(AbstractDatabase other) {
        // Make sure the other isn't null
        if(other == null)
            return;

        // Copy the name
        this.name = other.getName();
    }

    /**
     * Configuration section to load the database from.
     *
     * @param config Configuration section.
     */
    public AbstractDatabase(ConfigurationSection config) { }

    /**
     * Save the base of the database.
     *
     * @param config Configuration section to save into.
     */
    public final void saveBase(ConfigurationSection config) {
        // Save the database type and name
        config.set("type", getType().getTypeId());
        config.set("name", this.name);

        // Save the database instance in the data section
        save(config.createConfigurationSection("data"));
    }

    /**
     * Save the database.
     *
     * @param config Configuration section to save into.
     */
    public abstract void save(ConfigurationSection config);

    /**
     * Load a database from a configuration section.
     *
     * @param config Configuration section to load the database from.
     *
     * @return Loaded database.
     */
    public static AbstractDatabase load(ConfigurationSection config) {
        // Get the database type
        DatabaseType type = DatabaseType.fromTypeId(config.getInt("type", 1));

        // Get the data configuration section
        ConfigurationSection dataSection = config.getSection("data");

        // Abstract database
        AbstractDatabase database;

        // Use the proper loader
        assert type != null;
        switch(type) {
            default:
            case INTEGRATED:
                database = new IntegratedDatabase(dataSection);
                break;

            case REMOTE:
                database = new RemoteDatabase(dataSection);
                break;
        }

        // Set the database name
        database.setName(config.getString("name"));

        // Return the database
        return database;
    }

    /**
     * Get the database type.
     *
     * @return Database type.
     */
    public abstract DatabaseType getType();

    /**
     * Check whether the database is properly configured.
     * This method will return false if one of the fields, for example, a database host field is left blank.
     *
     * @return True if properly configured, false if not.
     */
    public abstract boolean isConfigured();

    /**
     * Get the database name.
     *
     * @return Name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the database name.
     *
     * @param name Database name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Prepare the database for use. This step is required before a connection to this database (configuration) is made.
     * The database must have been fully configured to prepare it.
     *
     * @param progressDialog Progress dialog instance, or null.
     *
     * @return True on success, false on failure.
     */
    public abstract boolean prepare(ProgressDialog progressDialog);

    /**
     * Create a new database connection with the configured credentials.
     *
     * @param progressDialog Progress dialog.
     *
     * @return Database connector.
     *
     * @throws Exception Exception if an error occurred while connecting to the database.
     */
    public DatabaseConnector createConnection(ProgressDialog progressDialog) throws Exception {
        // Prepare the database
        if(!prepare(progressDialog))
            throw new Exception("Failed to prepare database.");

        // Set the status
        if(progressDialog != null)
            progressDialog.setStatus("Connecting to '" + getName() + "'...");

        // Create a new database connector
        DatabaseConnector connector = new DatabaseConnector(this);

        // Create a connection
        connector.createConnection();

        // Set the status
        if(progressDialog != null)
            progressDialog.setStatus("Connected to '" + getName() + "'.");

        // Return the connector
        return connector;
    }

    /**
     * Test the database configuration.
     * This will check whether a database connection could be made to the configured database.
     *
     * @param parent Parent window, may be null.
     * @param progressDialog Progress dialog, may be null.
     *
     * @return True on success, false on failure.
     */
    public boolean test(Window parent, ProgressDialog progressDialog) {
        // Set the status
        if(progressDialog != null)
            progressDialog.setStatus("Testing '" + getName() + "'...");

        // Determine the parent window to use
        final Component messageParent = progressDialog != null ? progressDialog : parent;

        // Create a connection
        try {
            this.createConnection(null);

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Failed to connect, show a status message
            JOptionPane.showMessageDialog(
                    messageParent,
                    "Failed to connect to the '" + getName()  + "' database.\n\n" +
                            "Detailed error message:\n" + e.getMessage(),
                    "Database connection failure",
                    JOptionPane.ERROR_MESSAGE
            );

            // Return the result
            return false;
        }

        // Return the result
        return true;
    }

    public abstract String getDatabaseDriverString();

    public abstract String getDatabaseConnectionString();

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public abstract AbstractDatabase clone();

    @Override
    public boolean equals(Object obj) {
        // Return false if we're not working with a abstract database instance
        if(!(obj instanceof AbstractDatabase))
            return false;

        // Get the abstract database instance
        AbstractDatabase database = (AbstractDatabase) obj;

        // Return true if the type and name are equal
        return this.name.equals(database.getName()) && this.getType().equals(database.getType());
    }
}
