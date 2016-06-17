package me.childintime.childintime.database.entity.spec.couple.studentsport;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class StudentSport extends AbstractEntity {

    /**
     * Constructor.
     */
    public StudentSport() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public StudentSport(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return StudentSportManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Prefetch the fields
            getFields(new StudentSportFields[]{
                    StudentSportFields.SPORT_ID,
                    StudentSportFields.SPORT_ID
            });

            // Build and return the display name
            return getFieldFormatted(StudentSportFields.STUDENT_ID) + " & " + getFieldFormatted(StudentSportFields.SPORT_ID);

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
