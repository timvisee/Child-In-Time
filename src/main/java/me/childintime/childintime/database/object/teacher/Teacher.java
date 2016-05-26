package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class Teacher extends AbstractDatabaseObject {

    @Override
    public boolean hasFields(DatabaseFieldsInterface[] fields) {

        for (DatabaseFieldsInterface field : fields) {
            if (!(field instanceof TeacherFields))
                return false;
            // TODO: Check fields
        }

        return true;
    }

    @Override
    public boolean hasField(DatabaseFieldsInterface field) {
        return hasFields(new DatabaseFieldsInterface[]{field});
    }

    @Override
    public boolean fetchFields(DatabaseFieldsInterface[] fields) {
        return false;
    }

    @Override
    public boolean fetchField(DatabaseFieldsInterface fields) {
        return false;
    }

    @Override
    public Object getFields(DatabaseFieldsInterface[] fields) {
        return null;
    }

    @Override
    public Object getField(DatabaseFieldsInterface fields) {
        return null;
    }
}
