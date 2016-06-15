package me.childintime.childintime.user;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.spec.user.User;
import me.childintime.childintime.database.entity.spec.user.UserFields;
import me.childintime.childintime.permission.PermissionLevel;

import java.sql.Connection;
import java.sql.SQLException;

public class Authenticator {

    /**
     * The authenticated user.
     */
    private User authUser = null;

    /**
     * Permission level of the authenticated user.
     */
    private PermissionLevel permissionLevel = null;

    /**
     * Authenticate a user.
     *
     * @param user Username.
     * @param password Password.
     *
     * @return True if the user was authenticated, false if not.
     */
    public boolean authenticate(String user, String password) throws SQLException {
        return authenticate(Core.getInstance().getDatabaseConnector().createConnection(), user, password);
    }

    /**
     * Authenticate a user.
     *
     * @param databaseConnection Database connection.
     * @param user Username.
     * @param password Password.
     *
     * @return True if the user was authenticated, false if not.
     */
    public boolean authenticate(Connection databaseConnection, String user, String password) {
        // Try to authenticate the user
        this.authUser = UserHelper.validateUser(databaseConnection, user, password);

        // Make sure the user was authenticated
        if(this.authUser == null)
            return false;

        // Set the permission level
        try {
            this.permissionLevel = (PermissionLevel) this.authUser.getField(UserFields.PERMISSION_LEVEL);

        } catch(Exception e) {
            e.printStackTrace();
        }

        // Return the result
        return true;
    }

    /**
     * Logout the user that is currently authenticated.
     */
    public void logout() {
        // Reset the authenticated user and permission level
        this.authUser = null;
        this.permissionLevel = null;
    }

    /**
     * Check whether a user is authetnicated.
     *
     * @return True if authenticated, false if not.
     */
    public boolean isAuthenticated() {
        return this.authUser != null;
    }

    /**
     * Get the authenticated user.
     *
     * @return Authenticated user.
     */
    public User getAuthenticatedUser() {
        return this.authUser;
    }

    /**
     * Get the permission level.
     *
     * @return Permission level.
     */
    public PermissionLevel getPermissionLevel() {
        return this.permissionLevel;
    }
}
