package me.childintime.childintime.database.entity.spec.user;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.*;

import java.util.ArrayList;
import java.util.List;

public class UserManifest extends AbstractEntityManifest {

    /**
     * Entity type name.
     */
    public static final String TYPE_NAME = "User";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "user";

    /**
     * This instance.
     * Singleton.
     */
    private static UserManifest instance = null;

    /**
     * Couples specification for this entity.
     */
    private List<AbstractEntityCoupleManifest> couples = new ArrayList<>();

    /**
     * Get the singleton instance of this class.
     * The class will be instantiated if no instance is loaded.
     *
     * @return Class instance.
     */
    public static UserManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new UserManifest();

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
        return new UserFields[]{
                UserFields.USERNAME
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return UserFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return User.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return UserManager.class;
    }

    @Override
    public UserManager getManagerInstance() {
        return Core.getInstance().getUserManager();
    }

    @Override
    public boolean isCouple() {
        return false;
    }

    @Override
    public List<AbstractEntityCoupleManifest> getCouples() {
        return this.couples;
    }
}
