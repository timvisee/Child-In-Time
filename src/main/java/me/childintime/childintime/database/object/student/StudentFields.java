package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public enum StudentFields implements DatabaseFieldsInterface {

    /**
     * Student ID.
     */
    ID ("id"),

    /**
     * First name of the student.
     */
    FIRST_NAME ("name");

    /**
     * Database table name.
     */
    public static final String DATABASE_TABLE_NAME = "student";

    /**
     * Field name in the database of the field.
     */
    private String fieldName;

    /**
     * Constructor.
     *
     * @param fieldName Field name.
     */
    StudentFields(String fieldName) {
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
