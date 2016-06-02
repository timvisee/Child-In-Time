package me.childintime.childintime.database.object.school;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class SchoolManifest extends AbstractDatabaseObjectManifest {

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "school";

    /**
     * This instance.
     * Singleton.
     */
    private static SchoolManifest instance = null;

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static SchoolManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new SchoolManifest();

        // Return the instance
        return instance;
    }

    @Override
    public String getTableName() {
        return DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFields() {
        return SchoolFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObject() {
        return School.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManager() {
        return SchoolManager.class;
    }
}
