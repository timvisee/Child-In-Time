package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class BodyState extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Body state";

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public BodyState(int id) {
        super(id);
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFieldsClass() {
        return BodyStateFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManagerClass() {
        return BodyStateManager.class;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public String getDisplayName() {
        try {
            // Pre-fetch the required fields if not cached
            getFields(new BodyStateFields[]{
                    BodyStateFields.LENGTH,
                    BodyStateFields.WEIGHT
            });

            // Build and return the display name
            return String.valueOf(getField(BodyStateFields.LENGTH)) + " cm, " +
                    String.valueOf(getField(BodyStateFields.WEIGHT)) + " g";

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
