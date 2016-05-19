package me.childintime.childintime;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private boolean integrated = true;

    private String hostname;
    private String database;
    private String username;
    private String password;

    private Connection connection;

    public Database() {
        ConfigurationSection config = Core.getInstance().getConfig().getConfig().getSection("database");
        hostname = config.getString("hostname");
        database = config.getString("database");
        username = config.getString("username");
        password = config.getString("password");
    }

    public void init() {

        try {
            String driver;

            // Load the proper driver class
            if(!integrated)
                driver = "com.mysql.jdbc.Driver";
            else
                driver = "org.sqlite.JDBC";

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

        String connectionString;

        // Define the proper connection string
        if(!integrated)
            connectionString = "jdbc:mysql://" + hostname + "/" + database + "?" +
                    "user=" + username + "&password=" + password;
        else {
            File databaseFile = new File(App.getDirectory(), "/db/integrated.db");
            connectionString = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
        }

        return DriverManager.getConnection(connectionString);
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
