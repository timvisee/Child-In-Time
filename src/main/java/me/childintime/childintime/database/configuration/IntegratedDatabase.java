package me.childintime.childintime.database.configuration;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;
import me.childintime.childintime.App;
import me.childintime.childintime.database.DatabaseType;

import java.io.File;

public class IntegratedDatabase extends AbstractDatabase implements Cloneable {

    /**
     * Default database file.
     */
    public static final File DEFAULT_FILE = new File(App.getDirectory(), "/db/local.db");

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
