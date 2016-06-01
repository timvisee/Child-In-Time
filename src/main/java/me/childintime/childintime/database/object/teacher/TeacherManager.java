package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;

public class TeacherManager extends AbstractDatabaseObjectManager{


    @Override
    public String getTypeName() {
        return "Teacher";
    }

    @Override
    public String getTableName() {
        return TeacherFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObjectClass() {
        return Teacher.class;
    }
}
