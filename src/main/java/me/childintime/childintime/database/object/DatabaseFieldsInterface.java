package me.childintime.childintime.database.object;

public interface DatabaseFieldsInterface {

    /**
     * Get the table name.
     *
     * @return Table name.
     */
    String getTableName();

    /**
     * Get the field name in the database of this field.
     *
     * @return Field name in the database.
     */
    String getFieldName();
}
