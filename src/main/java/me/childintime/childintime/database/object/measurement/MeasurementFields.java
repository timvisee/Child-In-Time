package me.childintime.childintime.database.object.measurement;

public enum MeasurementFields {

    STUDENT_ID("student_id"),
    DATE("date"),
    TIME("time"),
    PARKOUR_ID("parkour_id");

    private String student_id;

    MeasurementFields(String student_id) {
        this.student_id = student_id;
    }
}
