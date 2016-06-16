package me.childintime.childintime.database.entity;

import me.childintime.childintime.database.entity.datatype.DataTypeExtended;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityCoupleManifest extends AbstractEntityManifest {

    /**
     * Get the name of the current entity manager type.
     *
     * @param referenceManifest The reference manifest.
     * @param capital True to make the first character capital.
     * @param plural True to make the type name a plural.
     * @param other True to get the name of the other reference.
     *
     * @return Entity manager type name.
     */
    public String getReferenceTypeName(AbstractEntity referenceManifest, boolean capital, boolean plural, boolean other) {
        return getReferenceTypeName(referenceManifest, capital, plural, other);
    }

    /**
     * Get the name of the current entity manager type.
     *
     * @param referenceManifest The reference manifest.
     * @param capital True to make the first character capital.
     * @param plural True to make the type name a plural.
     * @param other True to get the name of the other reference.
     *
     * @return Entity manager type name.
     */
    public String getReferenceTypeName(AbstractEntityManifest referenceManifest, boolean capital, boolean plural, boolean other) {
        // Get the manifest to use
        AbstractEntityManifest manifest = referenceManifest;

        // Get the other manifest
        if(other)
            manifest = getOtherManifest(manifest);

        // Get the reference type name
        return (capital ? "C" : "c") + "oupled " + manifest.getTypeName(false, plural);
    }

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
