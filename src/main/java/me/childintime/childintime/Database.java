package me.childintime.childintime;

import com.timvisee.yamlwrapper.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

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
            String driver = "com.mysql.jdbc.Driver";
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

        String connectionString = "jdbc:mysql://" + hostname + "/" + database + "?" +
                "user=" + username + "&password=" + password;

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
