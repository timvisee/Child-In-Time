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

package me.childintime.childintime.database.entity.spec.couple.groupteacher;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityCoupleManifest;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.EntityFieldsInterface;

import java.util.ArrayList;
import java.util.List;

public class GroupTeacherManifest extends AbstractEntityCoupleManifest {

    /**
     * Entity type name.
     */
    public static final String TYPE_NAME = "Group & teacher couple";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "group_teacher";

    /**
     * This instance.
     * Singleton.
     */
    private static GroupTeacherManifest instance = null;

    /**
     * Couples specification for this entity.
     */
    private List<AbstractEntityCoupleManifest> couples = new ArrayList<>();

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static GroupTeacherManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new GroupTeacherManifest();

        // Return the instance
        return instance;
    }

    @Override
    public String getTypeName(boolean capital, boolean plural) {
        return (capital ? TYPE_NAME.substring(0, 1).toUpperCase() + TYPE_NAME.substring(1) : TYPE_NAME.toLowerCase()) +
                (plural ? "s" : "");
    }

    @Override
    public String getTableName() {
        return DATABASE_TABLE_NAME;
    }

    @Override
    public EntityFieldsInterface[] getDefaultFields() {
        return new GroupTeacherFields[]{
                GroupTeacherFields.GROUP_ID,
                GroupTeacherFields.TEACHER_ID
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return GroupTeacherFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return GroupTeacher.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return GroupTeacherManager.class;
    }

    @Override
    public GroupTeacherManager getManagerInstance() {
        return Core.getInstance().getGroupTeacherCoupleManager();
    }

    @Override
    public boolean isCouple() {
        return true;
    }

    @Override
    public List<AbstractEntityCoupleManifest> getCouples() {
        return this.couples;
    }
}
