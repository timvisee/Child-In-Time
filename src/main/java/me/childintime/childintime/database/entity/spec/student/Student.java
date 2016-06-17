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

package me.childintime.childintime.database.entity.spec.student;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.permission.PermissionLevel;

public class Student extends AbstractEntity {

    /**
     * Constructor.
     */
    public Student() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public Student(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return StudentManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Make sure the user has permission to view the name
            if(PermissionLevel.VIEW.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
                // Pre-fetch the required fields if not cached
                getFields(new StudentFields[]{
                        StudentFields.FIRST_NAME,
                        StudentFields.LAST_NAME
                });

                // Build and return the display name
                return String.valueOf(getField(StudentFields.FIRST_NAME)) + " " + String.valueOf(getField(StudentFields.LAST_NAME));

            } else
                // Return the ID instead
                return String.valueOf("Student " + getId());

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
