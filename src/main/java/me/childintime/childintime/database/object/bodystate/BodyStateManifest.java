package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class BodyStateManifest extends AbstractDatabaseObjectManifest {

    /**
     * Database object type name.
     */
    public static final String TYPE_NAME = "Body state";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "bodystate";

    /**
     * This instance.
     * Singleton.
     */
    private static BodyStateManifest instance = null;

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static BodyStateManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new BodyStateManifest();

        // Return the instance
        return instance;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public String getTableName() {
        return DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFields() {
        return BodyStateFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObject() {
        return BodyState.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManager() {
        return BodyStateManager.class;
    }
}
