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

package me.childintime.childintime.database.entity.spec.couple.studentsport;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class StudentSport extends AbstractEntity {

    /**
     * Constructor.
     */
    public StudentSport() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public StudentSport(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return StudentSportManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Prefetch the fields
            getFields(new StudentSportFields[]{
                    StudentSportFields.SPORT_ID,
                    StudentSportFields.SPORT_ID
            });

            // Build and return the display name
            return getFieldFormatted(StudentSportFields.STUDENT_ID) + " & " + getFieldFormatted(StudentSportFields.SPORT_ID);

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
