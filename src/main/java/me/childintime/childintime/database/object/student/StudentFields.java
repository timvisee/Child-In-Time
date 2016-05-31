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
    public String getFieldName() {
        return this.fieldName;
    }
}
