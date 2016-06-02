package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class StudentManifest extends AbstractDatabaseObjectManifest {

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
