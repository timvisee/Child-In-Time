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

package me.childintime.childintime.store;

public class LoginStore {

    /**
     * Store section.
     */
    public static final String STORE_SECTION = "login";

    /**
     * Store key for the username.
     */
    public static final String STORE_KEY_USER = "user";

    /**
     * Store key for the password.
     */
    public static final String STORE_KEY_PASSWORD = "password";

    /**
     * Get the stored username.
     *
     * @param def Default.
     *
     * @return Stored username.
     */
    public static String getUsername(String def) {
        return GlobalStore.getValue(STORE_SECTION, STORE_KEY_USER, def);
    }

    /**
     * Check whether a username is stored.
     *
     * @return Stored username.
     */
    public static boolean hasUsername() {
        return GlobalStore.hasValue(STORE_SECTION, STORE_KEY_USER);
    }

    /**
     * Set the stored username.
     *
     * @param username Stored username.
     */
    public static void setUsername(String username) {
        GlobalStore.setValue(STORE_SECTION, STORE_KEY_USER, username);
    }

    /**
     * Get the stored password.
     *
     * @param def Default.
     *
     * @return Stored password.
     */
    public static String getPassword(String def) {
        return GlobalStore.getValue(STORE_SECTION, STORE_KEY_PASSWORD, def);
    }

    /**
     * Check whether a password is stored.
     *
     * @return Stored password.
     */
    public static boolean hasPassword() {
        return GlobalStore.hasValue(STORE_SECTION, STORE_KEY_PASSWORD);
    }

    /**
     * Set the stored password.
     *
     * @param password Stored password.
     */
    public static void setPassword(String password) {
        GlobalStore.setValue(STORE_SECTION, STORE_KEY_PASSWORD, password);
    }
}
