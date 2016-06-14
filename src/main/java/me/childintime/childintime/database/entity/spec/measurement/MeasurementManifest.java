package me.childintime.childintime.database.entity.spec.measurement;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityFieldsInterface;

public class MeasurementManifest extends AbstractEntityManifest {

    /**
     * Entity type name.
     */
    public static final String TYPE_NAME = "Measurement";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "measurement";

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
    public String getTypeName(boolean capital, boolean plural) {
        return (capital ? TYPE_NAME.substring(0, 1).toUpperCase() + TYPE_NAME.substring(1) : TYPE_NAME.toLowerCase()) +
                (plural ? "s" : "");
    }

    @Override
    public String getTableName() {
        return DATABASE_TABLE_NAME;
    }

    @Override
    public EntityFieldsInterface[] getDefaultFields() {
        return new MeasurementFields[]{
                MeasurementFields.STUDENT_ID,
                MeasurementFields.TIME,
                MeasurementFields.PARKOUR_ID
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return MeasurementFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return Measurement.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return MeasurementManager.class;
    }

    @Override
    public MeasurementManager getManagerInstance() {
        return Core.getInstance().getMeasurementManager();
    }
}
