package me.childintime.childintime.database.entity.spec.measurement;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class Measurement extends AbstractEntity {

    /**
     * Constructor.
     */
    public Measurement() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public Measurement(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return MeasurementManifest.getInstance();
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
