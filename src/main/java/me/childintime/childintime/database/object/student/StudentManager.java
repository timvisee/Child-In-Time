package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;

public class StudentManager extends AbstractDatabaseObjectManager{

    @Override
    public String getTypeName() {
        return "Student";
    }

    @Override
    public String getTableName() {
        return StudentFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObjectClass() {
        return Student.class;
    }
}
