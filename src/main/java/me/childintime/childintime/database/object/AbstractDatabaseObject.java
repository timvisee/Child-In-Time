package me.childintime.childintime.database.object;

public abstract class AbstractDatabaseObject {

    public abstract boolean hasFields(DatabaseFieldsInterface[] fields);
    public abstract boolean hasField(DatabaseFieldsInterface field);

    public abstract boolean fetchFields(DatabaseFieldsInterface[] fields);
    public abstract boolean fetchField(DatabaseFieldsInterface fields);

    public abstract Object getFields(DatabaseFieldsInterface[] fields);
    public abstract Object getField(DatabaseFieldsInterface fields);
}
