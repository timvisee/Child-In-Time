package me.childintime.childintime.database.object.parkour;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class ParkourManifest extends AbstractDatabaseObjectManifest {

    /**
     * Database object type name.
     */
    public static final String TYPE_NAME = "Parkour";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "parkour";

    /**
     * This instance.
     * Singleton.
     */
    private static ParkourManifest instance = null;

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static ParkourManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new ParkourManifest();

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
        return ParkourFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObject() {
        return Parkour.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManager() {
        return ParkourManager.class;
    }
}
