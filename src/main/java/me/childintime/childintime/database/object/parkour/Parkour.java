package me.childintime.childintime.database.object.parkour;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;

public class Parkour extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Parkour";

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
    public String getTypeName() {
        return TYPE_NAME;
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
