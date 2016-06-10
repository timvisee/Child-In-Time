package me.childintime.childintime.database.object;

public interface DatabaseFieldsInterface {

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
     * The field manifest is equal to the object's manifest, unless this field references a different database object.
     * The manifest of the referencing database object is returned in that case.
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
