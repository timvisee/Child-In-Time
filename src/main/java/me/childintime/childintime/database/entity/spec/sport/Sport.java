package me.childintime.childintime.database.entity.spec.sport;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.permission.PermissionLevel;

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
            // Make sure the user has permission to view the name
            if(PermissionLevel.VIEW.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
                // Pre-fetch the required fields if not cached
                getFields(new SportFields[]{
                        SportFields.FIRST_NAME,
                        SportFields.LAST_NAME
                });

                // Build and return the display name
                return String.valueOf(getField(SportFields.FIRST_NAME)) + " " + String.valueOf(getField(SportFields.LAST_NAME));

            } else
                // Return the ID instead
                return String.valueOf("Sport " + getId());

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
