package me.childintime.childintime.database.object.school;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class SchoolManifest extends AbstractDatabaseObjectManifest {

    /**
     * Database object type name.
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
    public DatabaseFieldsInterface[] getDefaultFields() {
        return new SchoolFields[]{
                SchoolFields.NAME,
                SchoolFields.COMMUNE
        };
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
