package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public enum StudentFields implements DatabaseFieldsInterface {

    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    GENDER("gender"),
    BIRTHDATE("birthdate"),
    GROUP_ID("group_id");

    private String id;

    StudentFields(String id) {
        this.id = id;
    }

    public String getFieldName() {
        return id;
    }
}
