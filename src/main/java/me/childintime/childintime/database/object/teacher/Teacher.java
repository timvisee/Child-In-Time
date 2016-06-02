package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class Teacher extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Teacher";

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public Teacher(int id) {
        super(id);
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFieldsClass() {
        return TeacherFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManagerClass() {
        return TeacherManager.class;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
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
