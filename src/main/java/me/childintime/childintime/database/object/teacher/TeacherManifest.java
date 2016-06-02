package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class TeacherManifest extends AbstractDatabaseObjectManifest {

    /**
     * This instance.
     * Singleton.
     */
    private static TeacherManifest instance = null;

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static TeacherManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new TeacherManifest();

        // Return the instance
        return instance;
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFields() {
        return TeacherFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObject() {
        return Teacher.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManager() {
        return TeacherManager.class;
    }
}
