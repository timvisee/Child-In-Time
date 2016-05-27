package me.childintime.childintime.database.object;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractDatabaseObject {

    protected HashMap<DatabaseFieldsInterface, Object> hashmap;

    public abstract boolean hasFields(DatabaseFieldsInterface[] fields);
    public abstract boolean hasField(DatabaseFieldsInterface field);

    public abstract boolean fetchFields(DatabaseFieldsInterface[] fields);
    public abstract boolean fetchField(DatabaseFieldsInterface field);

    public abstract List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception;
    public abstract Object getField(DatabaseFieldsInterface field) throws Exception;

    /**
     * Clear the cached database object fields.
     */
    public void clear() {
        this.hashmap.clear();
    }
}
