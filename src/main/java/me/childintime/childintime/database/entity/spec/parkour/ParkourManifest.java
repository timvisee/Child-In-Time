package me.childintime.childintime.database.entity.spec.parkour;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityFieldsInterface;

import java.util.ArrayList;
import java.util.List;

public class ParkourManifest extends AbstractEntityManifest {

    /**
     * Entity type name.
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
     * Couples specification for this entity.
     */
    private List<AbstractEntityManifest> couples = new ArrayList<>();

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
        return new ParkourFields[]{
                ParkourFields.DESCRIPTION
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return ParkourFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return Parkour.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return ParkourManager.class;
    }

    @Override
    public ParkourManager getManagerInstance() {
        return Core.getInstance().getParkourManager();
    }

    @Override
    public boolean isCouple() {
        return false;
    }

    @Override
    public List<AbstractEntityManifest> getCouples() {
        return this.couples;
    }
}
