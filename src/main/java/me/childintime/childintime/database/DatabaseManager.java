package me.childintime.childintime.database;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;
import com.timvisee.yamlwrapper.configuration.YamlConfiguration;
import me.childintime.childintime.App;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseManager {

    /**
     * Databases file name.
     */
    public static final String FILE_NAME = "databases.yml";

    /**
     * List of databases.
     */
    private List<AbstractDatabase> databases;

    /**
     * Constructor.
     */
    public DatabaseManager() {
        this.databases = new ArrayList<>();
    }

    /**
     * Constructor.
     *
     * @param databases List of databases.
     */
    public DatabaseManager(List<AbstractDatabase> databases) {
        this.databases = databases;
    }

    /**
     * Get a list of databases.
     *
     * @return List of databases.
     */
    public List<AbstractDatabase> getDatabases() {
        return this.databases;
    }

    /**
     * Get a clone of the list of databases.
     *
     * @return Clone of the list of databases.
     */
    public List<AbstractDatabase> getDatabasesClone() {
        // Create a new list to put the clones into
        List<AbstractDatabase> clones = new ArrayList<>();

        // Clone each database in the databases list, and put it into the clones list
        clones.addAll(this.databases.stream().map(AbstractDatabase::clone).collect(Collectors.toList()));

        // Return the list of clones
        return clones;
    }

    /**
     * Set the list of databases.
     *
     * @param databases List of databases.
     */
    public void setDatabases(List<AbstractDatabase> databases) {
        this.databases = databases;
    }

    /**
     * Get a database by it's index.
     *
     * @param i Database index.
     *
     * @return Database.
     */
    public AbstractDatabase getDatabase(int i) {
        return this.databases.get(i);
    }

    /**
     * Get the number of databases.
     *
     * @return Number of databases.
     */
    public int getDatabaseCount() {
        return this.databases.size();
    }

    /**
     * Load the databases from a file.
     * The default database configuration will be loaded if the file doesn't exist.
     *
     * @return True if succeed, false if failed.
     * False will also be returned if the default database configuration was loaded because the file didn't exist.
     */
    public boolean load() {
        // Get the save file
        File file = getSaveFile();

        // Make sure the configuration file exists
        if(!file.exists()) {
            // Show a status message
            System.out.println("The database configurations file doesn't exist, loading default configuration...");

            // Load the default database configuration
            loadDefault();

            // Save the database configuration
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Return the result
            return false;
        }

        // Load the configuration
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Get the databases section
        ConfigurationSection databasesSection = config.getSection("databases");

        // Create a list to put the databases in
        List<AbstractDatabase> databases = new ArrayList<>();

        // Loop through the databases in this section();
        for(String key : databasesSection.getKeys("")) {
            // Get the configuration section for this database
            ConfigurationSection databaseSection = databasesSection.getSection(key);

            // Load the database and add it to the list
            databases.add(AbstractDatabase.load(databaseSection));
        }

        // Set the list of databases
        this.databases = databases;

        // Return the result
        return true;
    }

    /**
     * Load the default database configuration.
     * Warning: this might override the current database configuration.
     */
    public void loadDefault() {
        // Clear the list of databases
        this.databases.clear();

        // Create and add an integrated database
        IntegratedDatabase integrated = new IntegratedDatabase(IntegratedDatabase.DEFAULT_FILE);
        integrated.setName("Default integrated database");
        this.databases.add(integrated);

        // Create and add a remote database
        RemoteDatabase remote = new RemoteDatabase();
        remote.setName("Default remote database");
        this.databases.add(remote);
    }

    /**
     * Save all databases to a file.
     */
    public void save() throws IOException {
        // Create a configuration to store the databases into
        YamlConfiguration config = new YamlConfiguration();
        
        // Set the version name and code
        config.set("version.name", App.APP_VERSION_NAME);
        config.set("version.code", App.APP_VERSION_CODE);

        // Create a configuration section for the databases
        ConfigurationSection databasesSection = config.createSection("databases");

        // Loop through all databases, and store them in the section (ordered)
        for(int i = 0; i < getDatabaseCount(); i++) {
            // Get the current database
            AbstractDatabase database = this.databases.get(i);

            // Create a configuration section for this database
            ConfigurationSection databaseSection = databasesSection.createSection(String.valueOf(i));

            // Save the database
            database.saveBase(databaseSection);
        }

        // Save the file
        config.save(getSaveFile());
    }

    /**
     * Get the file the databases are stored in.
     *
     * @return Databases file.
     */
    public static File getSaveFile() {
        return new File(App.getDirectory(), FILE_NAME);
    }
}
