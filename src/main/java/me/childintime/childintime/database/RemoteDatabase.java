package me.childintime.childintime.database;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;

public class RemoteDatabase extends AbstractDatabase {

    /**
     * Database host.
     */
    private String host = null;

    /**
     * Database port.
     */
    private int port;

    /**
     * Database user.
     */
    private String user = null;

    /**
     * Database password.
     */
    private String password = null;

    /**
     * Configuration section to load the database from.
     *
     * @param config Configuration section.
     */
    public RemoteDatabase(ConfigurationSection config) {
        // Call the super
        super(config);

        // Fetch the properties
        this.host = config.getString("host", null);
        this.port = config.getInt("port", 3306);
        this.user = config.getString("user", null);
        this.password = config.getString("password", null);
    }

    /**
     * Get the host.
     *
     * @return Host.
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Check whether the host is configured.
     *
     * @return True if the host is configured, false if not.
     */
    public boolean hasHost() {
        // Make sure the host isn't null
        if(host == null)
            return false;

        // Make sure a valid host has been given
        return this.host.length() > 0;
    }

    /**
     * Set the host.
     *
     * @param host Host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Get the port.
     *
     * @return Port.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Set the port.
     *
     * @param port Port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Get the user.
     *
     * @return User.
     */
    public String getUser() {
        return this.user;
    }

    /**
     * Check whether the user is configured.
     *
     * @return True if configured, false if not.
     */
    public boolean hasUser() {
        return this.user != null;
    }

    /**
     * Set the user.
     *
     * @param user User.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get the password.
     *
     * @return Password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Check whether a password has been configured.
     *
     * @return True if configured, false if not.
     */
    public boolean hasPassword() {
        return this.password != null;
    }

    /**
     * Set the password.
     *
     * @param password Password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void save(ConfigurationSection config) {
        // Save the file path
        config.set("host", this.host);
        config.set("port", this.port);
        config.set("user", this.user);
        config.set("password", this.password);
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.REMOTE;
    }

    @Override
    public boolean isConfigured() {
        // Make sure everything that is required, is configured
        return hasHost() && hasUser();
    }
}
