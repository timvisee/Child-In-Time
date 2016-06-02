package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class TeacherManager extends AbstractDatabaseObjectManager{

    @Override
    public DatabaseFieldsInterface[] getDefaultFields() {
        return new TeacherFields[]{
                TeacherFields.FIRST_NAME,
                TeacherFields.LAST_NAME
        };
    }

    @Override
    public String getTypeName() {
        return "Teacher";
    }

    @Override
    public String getTableName() {
        return TeacherFields.DATABASE_TABLE_NAME;
    }

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return TeacherManifest.getInstance();
    }
}
