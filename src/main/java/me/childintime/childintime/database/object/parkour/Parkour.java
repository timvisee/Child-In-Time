package me.childintime.childintime.database.object.parkour;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;

public class Parkour extends AbstractDatabaseObject {

    /**
     * Constructor.
     */
    public Parkour() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public Parkour(int id) {
        super(id);
    }

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
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
