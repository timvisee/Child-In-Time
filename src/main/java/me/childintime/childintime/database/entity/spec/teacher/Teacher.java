package me.childintime.childintime.database.entity.spec.teacher;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.permission.PermissionLevel;

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
            // Make sure the user has permission to view the name
            if(PermissionLevel.VIEW.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
                // Pre-fetch the required fields if not cached
                getFields(new TeacherFields[]{
                        TeacherFields.FIRST_NAME,
                        TeacherFields.LAST_NAME
                });

                // Build and return the display name
                return String.valueOf(getField(TeacherFields.FIRST_NAME)) + " " + String.valueOf(getField(TeacherFields.LAST_NAME));

            } else
                // Return the ID instead
                return String.valueOf("Teacher " + getId());

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
