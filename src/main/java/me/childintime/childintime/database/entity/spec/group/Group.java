package me.childintime.childintime.database.entity.spec.group;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class Group extends AbstractEntity {

    /**
     * Constructor.
     */
    public Group() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public Group(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return GroupManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Build and return the display name
            return String.valueOf(getField(GroupFields.NAME));

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
