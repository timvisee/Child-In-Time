package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class MeasurementManifest extends AbstractDatabaseObjectManifest {

    /**
     * This instance.
     * Singleton.
     */
    private static MeasurementManifest instance = null;

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static MeasurementManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new MeasurementManifest();

        // Return the instance
        return instance;
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFields() {
        return MeasurementFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObject() {
        return Measurement.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManager() {
        return MeasurementManager.class;
    }
}
