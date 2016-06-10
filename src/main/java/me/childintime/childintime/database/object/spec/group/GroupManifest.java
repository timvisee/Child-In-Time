package me.childintime.childintime.database.object.spec.group;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class GroupManifest extends AbstractDatabaseObjectManifest {

    /**
     * Database object type name.
     */
    public static final String TYPE_NAME = "Group";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "group";

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
        return new GroupFields[]{
                GroupFields.NAME
        };
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

    @Override
    public GroupManager getManagerInstance() {
        return Core.getInstance().getGroupManager();
    }
}
