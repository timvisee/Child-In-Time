package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;

public class Student extends AbstractDatabaseObject {

    /**
     * Constructor.
     */
    public Student() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public Student(int id) {
        super(id);
    }

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
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
