package me.childintime.childintime.database.object.group;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;

public class GroupManager extends AbstractDatabaseObjectManager{


    @Override
    public String getTypeName() {
        return "Group";
    }

    @Override
    public String getTableName() {
        return GroupFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObjectClass() {
        return Group.class;
    }
}
