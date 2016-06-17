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

package me.childintime.childintime.permission;

public enum PermissionLevel {

    /**
     * Permission to do anything.
     */
    ALL(0, "All permissions"),

    /**
     * Edit permissions.
     */
    EDIT(1, "Edit permissions"),

    /**
     * View permissions.
     */
    VIEW(2, "View permissions"),

    /**
     * View permissions for anonymous data.
     */
    VIEW_ANONYMOUS(3, "Anonymous view permissions");

    /**
     * Level number.
     */
    private int level;

    /**
     * Permission level name.
     */
    private String title;

    /**
     * Constructor.
     *
     * @param level Level number.
     * @param title Title.
     */
    PermissionLevel(int level, String title) {
        this.level = level;
        this.title = title;
    }

    /**
     * Get the level number.
     *
     * @return Level number.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Get the permission level by the given level number.
     *
     * @param level Level number.
     *
     * @return Permission level.
     */
    public static PermissionLevel getByLevel(int level) {
        // Loop through all the permission levels to find the proper one
        for(PermissionLevel permissionLevel : values())
            if(permissionLevel.getLevel() == level)
                return permissionLevel;

        // Return the base level
        return VIEW_ANONYMOUS;
    }

    /**
     * Check whether the given permission is equal or better than the current permission.
     *
     * @param test Permission to test.
     *
     * @return True if better, false if not.
     */
    public boolean orBetter(PermissionLevel test) {
        return test.getLevel() <= getLevel();
    }

    /**
     * Check whether the given permission is equal or worse than the current permission.
     *
     * @param test Permission to test.
     *
     * @return True if worse, false if not.
     */
    public boolean orWorse(PermissionLevel test) {
        return test.getLevel() >= getLevel();
    }

    @Override
    public String toString() {
        return this.title;
    }
}
