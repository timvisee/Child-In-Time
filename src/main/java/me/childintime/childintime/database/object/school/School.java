package me.childintime.childintime.database.object.school;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;

public class School extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "School";

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public School(int id) {
        super(id);
    }

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return SchoolManifest.getInstance();
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public String getDisplayName() {
        try {
            // Pre-fetch the required fields if not cached
            getFields(new SchoolFields[]{
                    SchoolFields.NAME,
                    SchoolFields.COMMUNE
            });

            // Build and return the display name
            return String.valueOf(getField(SchoolFields.NAME)) + ", " +
                    String.valueOf(getField(SchoolFields.COMMUNE));

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
