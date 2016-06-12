package me.childintime.childintime.database.entity;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractEntityManifest {

    /**
     * Base constructor.
     */
    public AbstractEntityManifest() { }

    /**
     * Get the name of the current entity manager type.
     *
     * @param capital True to make the first character capital.
     * @param plural True to make the type name a plural.
     *
     * @return Entity manager type name.
     */
    public abstract String getTypeName(boolean capital, boolean plural);

    /**
     * Get the database table name for the manifest´s entity.
     *
     * @return Database table name.
     */
    public abstract String getTableName();

    /**
     * Get the default entity fields.
     *
     * @return Default entity fields to fetch.
     */
    public abstract EntityFieldsInterface[] getDefaultFields();

    /**
     * Get the fields class for this entity.
     *
     * @return Fields class.
     */
    public abstract Class<? extends EntityFieldsInterface> getFields();

    /**
     * Get the field values for this entity from the fields class.
     *
     * @return Field values.
     *
     * @throws NoSuchMethodException Throws if the fields class is invalid.
     * @throws InvocationTargetException Throws if an error occurred.
     * @throws IllegalAccessException Throws if an error occurred.
     */
    public EntityFieldsInterface[] getFieldValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Get the field values from the fields class instance
        return (EntityFieldsInterface[]) getFields().getMethod("values").invoke(getFields());
    }

    /**
     * Get the entity and instance class for this entity.
     *
     * @return Entity class
     */
    public abstract Class<? extends AbstractEntity> getEntity();

    /**
     * Get the manager class for this entity.
     *
     * @return Manager class.
     */
    public abstract Class<? extends AbstractEntityManager> getManager();

    /**
     * Get the manager instance.
     *
     * @return Manager instance.
     */
    public abstract AbstractEntityManager getManagerInstance();
}
