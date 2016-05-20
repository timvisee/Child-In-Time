package me.childintime.childintime.database;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;

public class RemoteDatabase extends AbstractDatabase {

    private String host;
    private int port;
    private String user;
    private String password;

    /**
     * Configuration section to load the database from.
     *
     * @param config Configuration section.
     */
    public RemoteDatabase(ConfigurationSection config) {
        // Call the super
        super(config);

        // Fetch the properties
        this.host = config.getString("host", "localhost");
        this.port = config.getInt("port", 3306);
        this.user = config.getString("user", "root");
        this.password = config.getString("password", "");
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
}
