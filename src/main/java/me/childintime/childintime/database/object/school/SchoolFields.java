package me.childintime.childintime.database.object.school;

public enum SchoolFields {

    ID("id"),
    NAME("name"),
    COMMUNE("commune");

    private String id;

    SchoolFields(String id) {
        this.id = id;
    }
}
