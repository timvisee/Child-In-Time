package me.childintime.childintime.database.entity.spec.school;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityFieldsInterface;

import java.util.ArrayList;
import java.util.List;

public class SchoolManifest extends AbstractEntityManifest {

    /**
     * Entity type name.
     */
    public static final String TYPE_NAME = "School";

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
     * Couples specification for this entity.
     */
    private List<AbstractEntityManifest> couples = new ArrayList<>();

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
        return new SchoolFields[]{
                SchoolFields.NAME,
                SchoolFields.COMMUNE
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return SchoolFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return School.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return SchoolManager.class;
    }

    @Override
    public SchoolManager getManagerInstance() {
        return Core.getInstance().getSchoolManager();
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
