package me.childintime.childintime.database.object.bodystate;

public enum BodyStateFields {

    STUDENT_ID("student_id"),
    DATE("date"),
    LENGTH("length"),
    WEIGHT("weight");

    private String student_id;

    BodyStateFields(String student_id) {
        this.student_id = student_id;
    }
}
