package me.childintime.childintime.database.object.group;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class GroupManifest extends AbstractDatabaseObjectManifest {

    /**
     * This instance.
     * Singleton.
     */
    private static GroupManifest instance = null;

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static GroupManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new GroupManifest();

        // Return the instance
        return instance;
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFields() {
        return GroupFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObject() {
        return Group.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManager() {
        return GroupManager.class;
    }
}
