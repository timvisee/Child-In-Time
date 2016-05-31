package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public enum TeacherFields implements DatabaseFieldsInterface {

    // TODO: Put actual teacher fields here!
    ;

    /**
     * Database table name.
     */
    public static final String DATABASE_TABLE_NAME = "teacher";

    /**
     * Field name in the database of the field.
     */
    private String fieldName;

    /**
     * Constructor.
     *
     * @param fieldName Database field name.
     */
    TeacherFields(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getTableName() {
        return DATABASE_TABLE_NAME;
    }

    @Override
    public String getFieldName() {
        return this.fieldName;
    }
}
