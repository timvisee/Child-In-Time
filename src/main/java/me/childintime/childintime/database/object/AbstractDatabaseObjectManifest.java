package me.childintime.childintime.database.object;

public abstract class AbstractDatabaseObjectManifest {

    /**
     * Base constructor.
     */
    public AbstractDatabaseObjectManifest() { }

    /**
     * Get the fields class for this database object.
     *
     * @return Fields class.
     */
    public abstract Class<? extends DatabaseFieldsInterface> getFields();

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
