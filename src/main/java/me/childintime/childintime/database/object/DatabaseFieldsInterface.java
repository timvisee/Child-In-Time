package me.childintime.childintime.database.object;

public interface DatabaseFieldsInterface {

    /**
     * Get the display name for this field.
     *
     * @return Display name.
     */
    String getDisplayName();

    /**
     * Returns the fieldname in the database.
     *
     * @return The database fieldname in a String.
     */
    String getDatabaseField();

    /**
     * Check whether this field is editable by the user.
     *
     * @return True if editable, false if not.
     */
    boolean isEditable();

    /**
     * Returns the DataType of the field in the database.
     *
     * @return INTEGER, DATE, STRING, BOOLEAN, REFERENCE.
     */
    DataType getDataType();

    /**
     * Returns a Class Type.
     *
     * @return Class Type.
     */
    Class<? extends AbstractDatabaseObject> getReferenceType();
}
