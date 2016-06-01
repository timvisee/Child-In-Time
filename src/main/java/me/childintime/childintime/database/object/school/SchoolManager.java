package me.childintime.childintime.database.object.school;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;

public class SchoolManager extends AbstractDatabaseObjectManager{

    @Override
    public String getTypeName() {
        return "School";
    }

    @Override
    public String getTableName() {
        return SchoolFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObjectClass() {
        return School.class;
    }
}
