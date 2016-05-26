package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public enum TeacherFields implements DatabaseFieldsInterface {

    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    IS_GYM("is_gym"),
    SCHOOL_ID("school_id");

    private String id;

    TeacherFields(String id) {
        this.id = id;
    }

    @Override
    public String getFieldName() {
        return null;
    }
    // TODO: implement this
}
