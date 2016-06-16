package me.childintime.childintime.database.entity.spec.teacher;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.*;
import me.childintime.childintime.database.entity.spec.couple.groupteacher.GroupTeacherManifest;
import me.childintime.childintime.permission.PermissionLevel;

import java.util.ArrayList;
import java.util.List;

public class TeacherManifest extends AbstractEntityManifest {

    /**
     * Entity type name.
     */
    public static final String TYPE_NAME = "Teacher";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "teacher";

    /**
     * This instance.
     * Singleton.
     */
    private static TeacherManifest instance = null;

    /**
     * Couples specification for this entity.
     */
    private List<AbstractEntityCoupleManifest> couples = new ArrayList<AbstractEntityCoupleManifest>() {{
        add(GroupTeacherManifest.getInstance());
    }};

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
        if(PermissionLevel.VIEW.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel()))
            return new TeacherFields[]{
                    TeacherFields.FIRST_NAME,
                    TeacherFields.LAST_NAME,
                    TeacherFields.SCHOOL_ID
            };
        else
            return new TeacherFields[]{
                    TeacherFields.ID,
                    TeacherFields.SCHOOL_ID
            };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return TeacherFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return Teacher.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return TeacherManager.class;
    }

    @Override
    public TeacherManager getManagerInstance() {
        return Core.getInstance().getTeacherManager();
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
