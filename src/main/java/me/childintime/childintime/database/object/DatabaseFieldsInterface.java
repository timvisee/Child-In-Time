package me.childintime.childintime.database.object;

public interface DatabaseFieldsInterface {

    String getDatabaseField();

    /**
     * Check whether this field is editable by the user.
     *
     * @return True if editable, false if not.
     */
    boolean isEditable();

    DataType getDataType();

    Class<? extends AbstractDatabaseObject> getReferenceType();
}
