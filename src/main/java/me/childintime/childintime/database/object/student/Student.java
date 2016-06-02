package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class Student extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Student";

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public Student(int id) {
        super(id);
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFieldsClass() {
        return StudentFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManagerClass() {
        return StudentManager.class;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
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
