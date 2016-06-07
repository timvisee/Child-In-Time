package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class StudentManifest extends AbstractDatabaseObjectManifest {

    /**
     * Database object type name.
     */
    public static final String TYPE_NAME = "Student";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "student";

    /**
     * This instance.
     * Singleton.
     */
    private static StudentManifest instance = null;

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static StudentManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new StudentManifest();

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
        return new StudentFields[]{
                StudentFields.FIRST_NAME,
                StudentFields.LAST_NAME,
                StudentFields.GROUP_ID
        };
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFields() {
        return StudentFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObject() {
        return Student.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManager() {
        return StudentManager.class;
    }
}
