package me.childintime.childintime.user;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.spec.user.User;
import me.childintime.childintime.database.entity.spec.user.UserFields;
import me.childintime.childintime.database.entity.spec.user.UserManifest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserHelper {

    /**
     * Get the user entity manager.
     *
     * @return User entity manager.
     */
    private static AbstractEntityManager getUserEntityManager() {
        // TODO: Make sure this stuff is initialized!
        return Core.getInstance().getUserManager();
    }

    /**
     * Validate the given user.
     *
     * @param username Username of the user.
     * @param password Password of the user.
     *
     * @return The user object if the credentials are valid, null otherwise.
     */
    public static User validateUser(String username, String password) {
        // Create a database connection
        Connection connection;
        try {
            connection = Core.getInstance().getDatabaseConnector().createConnection();

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

        // Get the users manifest
        UserManifest manifest = UserManifest.getInstance();

        try {
            // Create a prepared statement to check the user credentials\
            // TODO: Select the permission level field here
            final PreparedStatement authStatement = connection.prepareStatement("SELECT" +
                    "   `" + UserFields.ID.getDatabaseField() + "`," +
                    "   `" + UserFields.PASSWORD_HASH.getDatabaseField() + "` " +
                    "FROM `" + manifest.getTableName() + "` " +
                    "WHERE" +
                    "   `" + UserFields.USERNAME.getDatabaseField() + "`=? AND " +
                    "   `" + UserFields.PERMISSION_LEVEL.getDatabaseField() + "`=?");

            // Fill in the username and password hash
            authStatement.setString(1, username);
            authStatement.setString(2, password);

            // Execute the statement
            ResultSet result = authStatement.executeQuery();

            // Make sure a row is selected
            if(!result.next())
                return null;

            // Get the ID and permission level
            // TODO: Fetch permission level here!
            int userId = result.getInt(1);
            int userLevel = result.getInt(1); // TODO: This should be 2!

            // Create a new user object with these properties
            User authenticatedUser = new User(userId);
            authenticatedUser.parseField(UserFields.PERMISSION_LEVEL, userLevel);

            // Return the user object
            return authenticatedUser;

        } catch(SQLException e) {
            // Print the stack trace
            e.printStackTrace();

            // Return null
            return null;
        }
    }
}
