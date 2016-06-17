/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package me.childintime.childintime.user;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.spec.user.User;
import me.childintime.childintime.database.entity.spec.user.UserFields;
import me.childintime.childintime.database.entity.spec.user.UserManifest;
import me.childintime.childintime.hash.HashUtil;

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
    public static User validateUser(String username, String password) throws SQLException {
        return validateUser(Core.getInstance().getDatabaseConnector().createConnection(), username, password);
    }

    /**
     * Validate the given user.
     *
     * @param databaseConnector Database connection.
     * @param username Username of the user.
     * @param password Password of the user.
     *
     * @return The user object if the credentials are valid, null otherwise.
     */
    public static User validateUser(Connection databaseConnector, String username, String password) {
        // Get the users manifest
        UserManifest manifest = UserManifest.getInstance();

        try {
            // Hash the password
            final String passwordHash = HashUtil.hash(password);

            // Create a prepared statement to authenticate the user credentials\
            final PreparedStatement authStatement = databaseConnector.prepareStatement("SELECT" +
                    "   `" + UserFields.ID.getDatabaseField() + "`," +
                    "   `" + UserFields.PERMISSION_LEVEL.getDatabaseField() + "` " +
                    "FROM `" + manifest.getTableName() + "` " +
                    "WHERE" +
                    "   `" + UserFields.USERNAME.getDatabaseField() + "`=? AND " +
                    "   `" + UserFields.PASSWORD_HASH.getDatabaseField() + "`=?");

            // Fill in the username and password hash
            authStatement.setString(1, username);
            authStatement.setString(2, passwordHash);

            // Execute the statement
            ResultSet result = authStatement.executeQuery();

            // Make sure a row is selected
            if(!result.next())
                return null;

            // Get the ID and permission level
            int userId = result.getInt(1);
            int userLevel = result.getInt(2);

            // Create a new user object with these properties
            User authenticatedUser = new User(userId);
            authenticatedUser.parseField(UserFields.PERMISSION_LEVEL, userLevel);

            // Return the user object
            return authenticatedUser;

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Return null
            return null;
        }
    }
}
