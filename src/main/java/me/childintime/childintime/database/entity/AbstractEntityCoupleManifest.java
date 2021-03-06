/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

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

    /**
     * Get the field that references the given manifest.
     *
     * @return Reference field.
     */
    public EntityCoupleFieldsInterface getFieldByReferenceManifest(AbstractEntityManifest referenceManifest) {
        try {
            // Loop through the fields
            for(EntityFieldsInterface entityFieldsInterface : getFieldValues()) {
                // Only process reference fields
                if(!entityFieldsInterface.getExtendedDataType().equals(DataTypeExtended.REFERENCE))
                    continue;

                // Return the field if the manifests are equal
                if(entityFieldsInterface.getReferenceManifest().equals(referenceManifest))
                    return (EntityCoupleFieldsInterface) entityFieldsInterface;
            }

            // Failed to get field, return null
            return null;

        } catch(Exception ex) {
            // Print the stack trace
            ex.printStackTrace();

            // Return null
            return null;
        }
    }
}
