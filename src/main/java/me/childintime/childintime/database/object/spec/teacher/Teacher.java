package me.childintime.childintime.database.object.spec.teacher;

import me.childintime.childintime.database.object.AbstractEntity;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class Teacher extends AbstractEntity {

    /**
     * Constructor.
     */
    public Teacher() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public Teacher(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return TeacherManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Pre-fetch the required fields if not cached
            getFields(new TeacherFields[]{
                    TeacherFields.FIRST_NAME,
                    TeacherFields.LAST_NAME
            });

            // Build and return the display name
            return String.valueOf(getField(TeacherFields.FIRST_NAME)) + " " + String.valueOf(getField(TeacherFields.LAST_NAME));

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
