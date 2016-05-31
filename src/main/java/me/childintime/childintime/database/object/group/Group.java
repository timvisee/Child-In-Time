package me.childintime.childintime.database.object.group;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.util.List;

public class Group extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Group";

    @Override
    public boolean hasFields(DatabaseFieldsInterface[] fields) {
        return false;
    }

    @Override
    public boolean fetchFields(DatabaseFieldsInterface[] fields) {
        return false;
    }

    @Override
    public List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception {
        return null;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }
}
