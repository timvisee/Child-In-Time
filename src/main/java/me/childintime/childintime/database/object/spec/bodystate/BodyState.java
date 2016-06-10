package me.childintime.childintime.database.object.spec.bodystate;

import me.childintime.childintime.database.object.AbstractEntity;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class BodyState extends AbstractEntity {

    /**
     * Constructor.
     */
    public BodyState() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public BodyState(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return BodyStateManifest.getInstance();
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
