package me.childintime.childintime.database.configuration;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;
import me.childintime.childintime.database.DatabaseType;

public class RemoteDatabase extends AbstractDatabase implements Cloneable {

    /**
     * Database host.
     */
    private String host = null;

    /**
     * Database port.
     */
    private int port = 3306;

    /**
     * Database user.
     */
    private String user = null;

    /**
     * Database password.
     */
    private String password = null;

    /**
     * Constructor.
     */
    public RemoteDatabase() {
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
    public RemoteDatabase(AbstractDatabase other) {
        // Call the super
        super(other);

        // Make sure the other isn't null
        if(other == null)
            return;

        // Clone the fields if the type is the same
        if(getType().equals(other.getType())) {
            // Cast the type
            RemoteDatabase otherRemote = (RemoteDatabase) other;

            // Set the remote database fields
            this.setHost(otherRemote.getHost());
            this.setPort(otherRemote.getPort());
            this.setUser(otherRemote.getUser());
            this.setPassword(otherRemote.getPassword());
        }
    }

    /**
     * Constructor.
     *
     * @param host Database host or null.
     * @param port Database port.
     * @param user Database user or null.
     * @param password Database password or null.
     */
    public RemoteDatabase(String host, int port, String user, String password) {
        // Construct the super
        super();

        // Set the name
        setName(getType().toString());

        // Set the fields
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

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

    @Override
    public RemoteDatabase clone() {
        return new RemoteDatabase(this);
    }

    @Override
    public boolean equals(Object obj) {
        // Equal the parent
        if(!super.equals(obj))
            return false;

        // Get the integrated database instance
        RemoteDatabase database = (RemoteDatabase) obj;

        // Compare all fields
        return (this.host != null ? this.host.equals(database.getHost()) : database.getHost() == null) &&
                this.port == database.getPort() &&
                (this.user != null ? this.user.equals(database.getUser()) : database.getUser() == null) &&
                (this.password != null ? this.password.equals(database.getPassword()) : database.getPassword() == null);
    }
}
