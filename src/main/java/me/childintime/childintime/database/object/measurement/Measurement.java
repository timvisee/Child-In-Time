package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class Measurement extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Measurement";

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public Measurement(int id) {
        super(id);
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFieldsClass() {
        return MeasurementFields.class;
    }

    @Override
    public Class<? extends AbstractDatabaseObjectManager> getManagerClass() {
        return MeasurementManager.class;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public String getDisplayName() {
        try {
            // Build and return the display name
            return String.valueOf(getField(MeasurementFields.TIME)) + " ms";

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
