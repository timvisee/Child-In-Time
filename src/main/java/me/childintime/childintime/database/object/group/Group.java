package me.childintime.childintime.database.object.group;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class Group extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Group";

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public Group(int id) {
        super(id);
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFieldsClass() {
        return GroupFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManagerClass() {
        return GroupManager.class;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
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
