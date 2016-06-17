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

package me.childintime.childintime.database.entity.spec.group;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.*;
import me.childintime.childintime.database.entity.spec.couple.groupteacher.GroupTeacherManifest;

import java.util.ArrayList;
import java.util.List;

public class GroupManifest extends AbstractEntityManifest {

    /**
     * Entity type name.
     */
    public static final String TYPE_NAME = "Group";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "group";

    /**
     * This instance.
     * Singleton.
     */
    private static GroupManifest instance = null;

    /**
     * Couples specification for this entity.
     */
    private List<AbstractEntityCoupleManifest> couples = new ArrayList<AbstractEntityCoupleManifest>() {{
        add(GroupTeacherManifest.getInstance());
    }};

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static GroupManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new GroupManifest();

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
        return new GroupFields[]{
                GroupFields.NAME,
                GroupFields.SCHOOL_ID
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return GroupFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return Group.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return GroupManager.class;
    }

    @Override
    public GroupManager getManagerInstance() {
        return Core.getInstance().getGroupManager();
    }

    @Override
    public boolean isCouple() {
        return false;
    }

    @Override
    public List<AbstractEntityCoupleManifest> getCouples() {
        return this.couples;
    }
}
