package me.childintime.childintime.database.entity.spec.student;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.permission.PermissionLevel;

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
            // Make sure the user has permission to view the name
            if(PermissionLevel.VIEW.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
                // Pre-fetch the required fields if not cached
                getFields(new StudentFields[]{
                        StudentFields.FIRST_NAME,
                        StudentFields.LAST_NAME
                });

                // Build and return the display name
                return String.valueOf(getField(StudentFields.FIRST_NAME)) + " " + String.valueOf(getField(StudentFields.LAST_NAME));

            } else
                // Return the ID instead
                return String.valueOf("Student " + getId());

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
