package me.childintime.childintime.database.object;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractDatabaseObjectManifest {

    /**
     * Base constructor.
     */
    public AbstractDatabaseObjectManifest() { }

    /**
     * Get the name of the current database object manager type.
     *
     * @param capital True to make the first character capital.
     * @param plural True to make the type name a plural.
     *
     * @return Database object manager type name.
     */
    public abstract String getTypeName(boolean capital, boolean plural);

    /**
     * Get the database table name for the manifest´s object.
     *
     * @return Database table name.
     */
    public abstract String getTableName();

    /**
     * Get the default database object fields.
     *
     * @return Default object fields to fetch.
     */
    public abstract DatabaseFieldsInterface[] getDefaultFields();

    /**
     * Get the fields class for this database object.
     *
     * @return Fields class.
     */
    public abstract Class<? extends DatabaseFieldsInterface> getFields();

    /**
     * Get the field values for this database object from the fields class.
     *
     * @return Field values.
     *
     * @throws NoSuchMethodException Throws if the fields class is invalid.
     * @throws InvocationTargetException Throws if an error occurred.
     * @throws IllegalAccessException Throws if an error occurred.
     */
    public DatabaseFieldsInterface[] getFieldValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Get the field values from the fields class instance
        return (DatabaseFieldsInterface[]) getFields().getMethod("values").invoke(getFields());
    }

    /**
     * Get the object and instance class for this database object.
     *
     * @return Object class
     */
    public abstract Class<? extends AbstractDatabaseObject> getObject();

    /**
     * Get the manager class for this database object.
     *
     * @return Manager class.
     */
    public abstract Class<? extends AbstractDatabaseObjectManager> getManager();
}
