package me.childintime.childintime.database.connector;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;
import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.configuration.AbstractDatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

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

    public void getConnection() throws SQLException {
        if(connection == null) {
            init();
            connection = createConnection();
        } else if(!connection.isValid(0)) {
            connection = createConnection();
        }
    }

    public Connection createConnection() throws SQLException {

        return DriverManager.getConnection(database.getDatabaseConnectionString());
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
