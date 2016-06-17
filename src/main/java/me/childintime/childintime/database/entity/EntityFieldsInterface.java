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

import me.childintime.childintime.database.entity.datatype.DataTypeBase;
import me.childintime.childintime.database.entity.datatype.DataTypeExtended;
import me.childintime.childintime.permission.PermissionLevel;

public interface EntityFieldsInterface {

    /**
     * Get the display name for this field.
     *
     * @return Display name.
     */
    String getDisplayName();

    /**
     * Returns the field name in the database.
     *
     * @return The database field name in a String.
     */
    String getDatabaseField();

    /**
     * Get the minimum permission for this field.
     *
     * @return Minimum field permission.
     */
    PermissionLevel getMinimumPermission();

    /**
     * Check whether this field is creatable by the user.
     *
     * @return True if creatable, false if not.
     */
    boolean isCreatable();

    /**
     * Check whether this field is editable by the user.
     *
     * @return True if editable, false if not.
     */
    boolean isEditable();

    /**
     * Check whether a NULL valid is allowed.
     *
     * @return True if null is allowed, false if not.
     */
    boolean isNullAllowed();

    /**
     * Check whether an empty property is allowed.
     *
     * @return true if empty properties are allowed, false if not.
     */
    boolean isEmptyAllowed();

    /**
     * Returns the extended data type of the field in the database.
     *
     * @return Extended data type.
     */
    DataTypeExtended getExtendedDataType();

    /**
     * Returns the base data type of the field in the database.
     *
     * @return Base data type.
     */
    DataTypeBase getBaseDataType();

    /**
     * Returns a reference manifest of the referenced class.
     *
     * @return Reference manifest.
     */
    AbstractEntityManifest getReferenceManifest();

    /**
     * Get the manifest instance for the current field.
     * The field manifest is equal to the object's manifest, unless this field references a different entity.
     * The manifest of the referencing entity is returned in that case.
     *
     * @return Field's manifest instance.
     */
    AbstractEntityManifest getFieldManifest();

    /**
     * Get the manifest instance of the class this fields class if for.
     *
     * @return Object's manifest instance.
     */
    AbstractEntityManifest getManifest();
}
