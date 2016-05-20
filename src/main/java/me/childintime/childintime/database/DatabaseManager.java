package me.childintime.childintime.database;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;
import com.timvisee.yamlwrapper.configuration.YamlConfiguration;
import me.childintime.childintime.App;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    /**
     * Databases file name.
     */
    public static final String FILE_NAME = "dbs.yml";

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
     * Get the number of databases.
     *
     * @return Number of databases.
     */
    public int getDatabaseCount() {
        return this.databases.size();
    }

    /**
     * Load the databases from a file.
     *
     * @return True if succeed, false if failed. False will also be returned if the databases file doesn't exist.
     */
    public boolean load() {
        // Get the save file
        File file = getSaveFile();

        // Make sure the configuration file exists
        if(!file.exists())
            return false;

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
            database.save(databaseSection);
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
