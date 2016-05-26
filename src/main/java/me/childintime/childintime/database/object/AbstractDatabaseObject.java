package me.childintime.childintime.database.object;

import java.util.HashMap;

public abstract class AbstractDatabaseObject {

    protected HashMap<DatabaseFieldsInterface, Object> hashmap;

    public abstract boolean hasFields(DatabaseFieldsInterface[] fields);
    public abstract boolean hasField(DatabaseFieldsInterface field);

    public abstract boolean fetchFields(DatabaseFieldsInterface[] fields);
    public abstract boolean fetchField(DatabaseFieldsInterface field);

    public abstract Object[] getFields(DatabaseFieldsInterface[] fields);
    public abstract Object getField(DatabaseFieldsInterface field);
}
