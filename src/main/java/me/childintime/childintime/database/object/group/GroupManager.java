package me.childintime.childintime.database.object.group;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class GroupManager extends AbstractDatabaseObjectManager {

    @Override
    public DatabaseFieldsInterface[] getDefaultFields() {
        return new GroupFields[]{
                GroupFields.NAME
        };
    }

    @Override
    public String getTypeName() {
        return "Group";
    }

    @Override
    public String getTableName() {
        return GroupFields.DATABASE_TABLE_NAME;
    }

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return GroupManifest.getInstance();
    }
}
