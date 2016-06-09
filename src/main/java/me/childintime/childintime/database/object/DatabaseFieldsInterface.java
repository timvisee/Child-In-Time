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
     * Returns a Class Type.
     *
     * @return Class Type.
     */
    Class<? extends AbstractDatabaseObject> getReferenceType();
}
