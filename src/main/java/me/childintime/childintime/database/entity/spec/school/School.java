package me.childintime.childintime.database.entity.spec.school;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class School extends AbstractEntity {

    /**
     * Constructor.
     */
    public School() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public School(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return SchoolManifest.getInstance();
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
