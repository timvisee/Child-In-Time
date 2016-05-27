package me.childintime.childintime.database.object;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractDatabaseObject {

    /**
     * Hashmap containing cached fields from the database object.
     */
    protected HashMap<DatabaseFieldsInterface, Object> hashmap;

    /**
     * Check whether the given database object fields are cached.
     *
     * @param fields The fields to check.
     *
     * @return True if all given fields are cached, false if not.
     * False is also returned if just one of the given fields isn't cached.
     */
    public abstract boolean hasFields(DatabaseFieldsInterface[] fields);

    /**
     * Check whether the given database object field is cached.
     *
     * @param field The field.
     *
     * @return True if the given field is cached, false if not.
     */
    public abstract boolean hasField(DatabaseFieldsInterface field);

    /**
     * Fetch the given database fields.
     *
     * @param fields The fields to fetch.
     *
     * @return True if the given fields were fetched successfully.
     */
    public abstract boolean fetchFields(DatabaseFieldsInterface[] fields);

    /**
     * Fetch the given database field.
     *
     * @param field The field to fetch.
     *
     * @return True if the given field was fetched successfully.
     */
    public abstract boolean fetchField(DatabaseFieldsInterface field);

    /**
     * Get the given fields. All fields will be returned from cache when possible, fields that aren't cached are fetched
     * from the database automatically.
     *
     * @param fields Fields to get.
     *
     * @return List of field values.
     *
     * @throws Exception Throws if an error occurred.
     */
    public abstract List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception;

    /**
     * Get the given field. The field will be returned from cache when possible. If the field isn't available in cache,
     * it will be fetched from the database automatically.
     *
     * @param field The field to get.
     *
     * @return The field value.
     *
     * @throws Exception Throws if an error occurred.
     */
    public abstract Object getField(DatabaseFieldsInterface field) throws Exception;

    /**
     * Clear the cached database object fields.
     */
    public void clear() {
        this.hashmap.clear();
    }
}
