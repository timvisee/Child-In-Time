package me.childintime.childintime.database.entity.spec.couple.studentsport;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityCoupleManifest;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.EntityFieldsInterface;

import java.util.ArrayList;
import java.util.List;

public class StudentSportManifest extends AbstractEntityCoupleManifest {

    /**
     * Entity type name.
     */
    public static final String TYPE_NAME = "Student & sport couple";

    /**
     * Database table name for this object type.
     */
    public static final String DATABASE_TABLE_NAME = "student_sport";

    /**
     * This instance.
     * Singleton.
     */
    private static StudentSportManifest instance = null;

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
    public static StudentSportManifest getInstance() {
        // Create a singleton instance if it isn't instantiated yet
        if(instance == null)
            instance = new StudentSportManifest();

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
        return new StudentSportFields[]{
                StudentSportFields.STUDENT_ID,
                StudentSportFields.SPORT_ID
        };
    }

    @Override
    public Class<? extends EntityFieldsInterface> getFields() {
        return StudentSportFields.class;
    }

    @Override
    public Class<? extends AbstractEntity> getEntity() {
        return StudentSport.class;
    }

    @Override
    public Class<? extends AbstractEntityManager> getManager() {
        return StudentSportManager.class;
    }

    @Override
    public StudentSportManager getManagerInstance() {
        return Core.getInstance().getStudentSportCoupleManager();
    }

    @Override
    public boolean isCouple() {
        return true;
    }

    @Override
    public List<AbstractEntityCoupleManifest> getCouples() {
        return this.couples;
    }
}
