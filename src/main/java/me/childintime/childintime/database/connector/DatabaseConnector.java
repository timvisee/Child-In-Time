package me.childintime.childintime.database.connector;

import me.childintime.childintime.database.DatabaseDialect;
import me.childintime.childintime.database.configuration.AbstractDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    /**
     * Database connection timeout in seconds.
     */
    public static final int CONNECTION_TIMEOUT = 10;

    /**
     * Abstract database instance, containing the configuration.
     */
    private AbstractDatabase database;

    /**
     * Database connection instance.
     */
    private Connection connection;

    /**
     * Constructor.
     *
     * @param database Database configuration.
     */
    public DatabaseConnector(AbstractDatabase database) {
        this.database = database;
    }

    /**
     * Initialize.
     */
    public void init() {
        try {
            String driver = database.getDatabaseDriverString();
            Class.forName(driver);
        } catch(ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Get the database connection.
     * This will return the last connection that was created on this database connector.
     * A new connection will be created automatically if no connection was made yet.
     *
     * @return Database connection.
     *
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        // Create a new connection none was created yet
        if(this.connection == null)
            return createConnection();

        // Create and return a new connection if the it is invalid
        else if(!this.connection.isValid(CONNECTION_TIMEOUT))
            return createConnection();

        // Return the connection
        return this.connection;
    }

    /**
     * Create a new database connection.
     *
     * @return Connection.
     *
     * @throws SQLException
     */
    public Connection createConnection() throws SQLException {
        // Initialize on the first connection
        if(this.connection == null)
            init();

        // Create the connection and explicitly save it
        this.connection = DriverManager.getConnection(database.getDatabaseConnectionUrl());

        // Return the connection
        return this.connection;
    }

    /**
     * Destroy the database connector.
     */
    public void destroy() {
        if(connection != null) {
            try {
                connection.close();
            } catch(SQLException e) {
                // Connection could not be closed.
            } finally {
                connection = null;
            }
        }
    }

    /**
     * Get the database dialect.
     *
     * @return Database dialect.
     */
    public DatabaseDialect getDialect() {
        return this.database.getDialect();
    }
}
