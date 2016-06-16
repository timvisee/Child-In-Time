package me.childintime.childintime.database.entity.spec.user;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class User extends AbstractEntity {

    /**
     * Constructor.
     */
    public User() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public User(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return UserManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Build and return the display name
            return getFieldFormatted(UserFields.USERNAME);

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
