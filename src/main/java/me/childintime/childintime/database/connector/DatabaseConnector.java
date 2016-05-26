package me.childintime.childintime.database.connector;

import me.childintime.childintime.database.configuration.AbstractDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    /**
     * Database connection timeout in seconds.
     */
    public static final int CONNECTION_TIMEOUT = 10;

    private AbstractDatabase database;

    private Connection connection;

    public DatabaseConnector(AbstractDatabase database) {
        this.database = database;
    }

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
        if(this.connection == null) {
            // Initialize
            init();

            // Create and return a new connection
            return createConnection();
        }

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
        // Create the connection and explicitly save it
        this.connection = DriverManager.getConnection(database.getDatabaseConnectionString());

        // Return the connection
        return this.connection;
    }

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
}
