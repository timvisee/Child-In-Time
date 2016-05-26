package me.childintime.childintime.database.object.group;

public enum GroupFields {

    ID("id"),
    NAME("name"),
    SCHOOL_ID("school_id");

    private String id;


    GroupFields(String id) {
        this.id = id;
    }
}
