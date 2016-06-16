package me.childintime.childintime.database.entity.spec.sport;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class Sport extends AbstractEntity {

    /**
     * Constructor.
     */
    public Sport() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public Sport(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return SportManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            return String.valueOf(getField(SportFields.NAME));

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
