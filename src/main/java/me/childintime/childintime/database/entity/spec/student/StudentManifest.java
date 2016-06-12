package me.childintime.childintime.database.entity.spec.student;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityFieldsInterface;

public class StudentManifest extends AbstractEntityManifest {

    /**
     * Entity type name.
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
    public EntityFieldsInterface[] getDefaultFields() {
        return new StudentFields[]{
                StudentFields.FIRST_NAME,
                StudentFields.LAST_NAME,
                StudentFields.GROUP_ID
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return StudentFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return Student.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return StudentManager.class;
    }

    @Override
    public StudentManager getManagerInstance() {
        return Core.getInstance().getStudentManager();
    }
}
