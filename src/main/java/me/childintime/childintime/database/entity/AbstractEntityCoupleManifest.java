package me.childintime.childintime.database.entity;

import me.childintime.childintime.database.entity.datatype.DataTypeExtended;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityCoupleManifest extends AbstractEntityManifest {

    /**
     * Get the manifest of the field that isn't of the same entity type as the given.
     * If you have the entity couple fields A and B with a reference to their entity types, and A is given,
     * the manifest of B is returned. The same goes the other way around, if B is given, A is returned.
     *
     * @param entityManifest Entity manifest.
     *
     * @return The other entity manifest, or null if an error occurred.
     */
    public AbstractEntityManifest getOtherManifest(AbstractEntityManifest entityManifest) {
        // Create a list of available manifests
        List<AbstractEntityManifest> manifests = new ArrayList<>();

        try {
            // Fill the list of manifests
            for(EntityFieldsInterface entityFieldsInterface : getFieldValues()) {
                // Only process reference fields
                if(!entityFieldsInterface.getExtendedDataType().equals(DataTypeExtended.REFERENCE))
                    continue;

                // Add the manifest
                manifests.add(entityFieldsInterface.getReferenceManifest());
            }

            // Remove the manifest of the current entity
            // TODO: Make sure this works!
            manifests.remove(entityManifest);

            // Make sure a manifest is left
            if(manifests.size() == 0)
                return null;

            // Return the first manifest that is left
            return manifests.get(0);

        } catch(Exception ex) {
            // Print the stack trace
            ex.printStackTrace();

            // Return null
            return null;
        }
    }

    /**
     * Get the manifest of the field that isn't of the same entity type as the given.
     * If you have the entity couple fields A and B with a reference to their entity types, and A is given,
     * the manifest of B is returned. The same goes the other way around, if B is given, A is returned.
     *
     * @param entity Entity.
     *
     * @return The other entity manifest, or null if an error occurred.
     */
    public AbstractEntityManifest getOtherManifest(AbstractEntity entity) {
        return getOtherManifest(entity.getManifest());
    }
}
