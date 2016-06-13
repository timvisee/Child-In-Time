package me.childintime.childintime.database.entity.spec.group;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityFieldsInterface;

public class GroupManifest extends AbstractEntityManifest {

    /**
     * Entity type name.
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
    public EntityFieldsInterface[] getDefaultFields() {
        return new GroupFields[]{
                GroupFields.NAME,
                GroupFields.SCHOOL_ID
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return GroupFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return Group.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return GroupManager.class;
    }

    @Override
    public GroupManager getManagerInstance() {
        return Core.getInstance().getGroupManager();
    }
}
