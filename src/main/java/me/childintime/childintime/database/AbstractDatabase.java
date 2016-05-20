package me.childintime.childintime.database;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;

public abstract class AbstractDatabase {

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
        // Save the database type
        config.set("type", getType().getTypeId());

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

        // Use the proper loader
        assert type != null;
        switch(type) {
            default:
            case INTEGRATED:
                return new IntegratedDatabase(dataSection);

            case REMOTE:
                return new RemoteDatabase(dataSection);
        }
    }

    /**
     * Get the database type.
     *
     * @return Database type.
     */
    public abstract DatabaseType getType();
}
