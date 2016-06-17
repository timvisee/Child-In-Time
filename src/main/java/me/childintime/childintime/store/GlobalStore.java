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

import me.childintime.childintime.Core;

public class GlobalStore {

    /**
     * Base path.
     */
    public static final String BASE_PATH = "store";

    /**
     * Path separator.
     */
    public static final String PATH_SEPARATOR = ".";

    /**
     * Get the path for the given section and key.
     *
     * @param section Section.
     * @param key Key.
     *
     * @return Path.
     */
    private static String getPath(String section, String key) {
        return BASE_PATH + PATH_SEPARATOR + section + PATH_SEPARATOR + key;
    }

    /**
     * Check whether anything is stored for the given key.
     *
     * @param section Section.
     * @param key Key.
     *
     * @return True if a value is stored.
     */
    public static boolean hasValue(String section, String key) {
        return Core.getInstance().getConfig().getConfig().isString(getPath(section, key));
    }

    /**
     * Get the value from the given store.
     *
     * @param section Section.
     * @param key Key.
     * @param def Default value.
     *
     * @return Stored value.
     */
    public static String getValue(String section, String key, String def) {
        return Core.getInstance().getConfig().getConfig().getString(getPath(section, key), def);
    }

    /**
     * Set the value from the given store.
     *
     * @param section Section.
     * @param key Key.
     * @param value The value.
     */
    public static void setValue(String section, String key, String value) {
        Core.getInstance().getConfig().getConfig().set(getPath(section, key), value);
    }

    /**
     * Clear the value.
     *
     * @param section Section.
     * @param key Key.
     */
    public static void clearValue(String section, String key, String value) {
        setValue(section, key, null);
    }
}
