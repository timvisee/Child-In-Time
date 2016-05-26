package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public enum StudentFields implements DatabaseFieldsInterface {

    ID ("id"),
    FIRST_NAME ("name");

    private String fieldname;

    StudentFields(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getFieldName() {
        return fieldname;
    }
}
