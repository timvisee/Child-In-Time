package me.childintime.childintime.database.object.school;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.util.List;

public class School extends AbstractDatabaseObject {

    @Override
    public boolean hasFields(DatabaseFieldsInterface[] fields) {
        return false;
    }

    @Override
    public boolean hasField(DatabaseFieldsInterface field) {
        return false;
    }

    @Override
    public boolean fetchFields(DatabaseFieldsInterface[] fields) {
        return false;
    }

    @Override
    public boolean fetchField(DatabaseFieldsInterface field) {
        return false;
    }

    @Override
    public List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception {
        return null;
    }

    @Override
    public Object getField(DatabaseFieldsInterface field) throws Exception {
        return null;
    }
}
