package me.childintime.childintime.database.object.spec.parkour;

import me.childintime.childintime.database.object.AbstractEntity;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class Parkour extends AbstractEntity {

    /**
     * Constructor.
     */
    public Parkour() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public Parkour(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return ParkourManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Build and return the display name
            return String.valueOf(getField(ParkourFields.DESCRIPTION));

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
