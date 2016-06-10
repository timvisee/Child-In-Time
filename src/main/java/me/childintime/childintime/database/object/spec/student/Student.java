package me.childintime.childintime.database.object.spec.student;

import me.childintime.childintime.database.object.AbstractEntity;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class Student extends AbstractEntity {

    /**
     * Constructor.
     */
    public Student() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public Student(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return StudentManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Pre-fetch the required fields if not cached
            getFields(new StudentFields[]{
                    StudentFields.FIRST_NAME,
                    StudentFields.LAST_NAME
            });

            // Build and return the display name
            return String.valueOf(getField(StudentFields.FIRST_NAME)) + " " + String.valueOf(getField(StudentFields.LAST_NAME));

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
