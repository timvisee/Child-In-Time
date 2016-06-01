package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.parkour.Parkour;
import me.childintime.childintime.database.object.student.Student;

public enum MeasurementFields implements DatabaseFieldsInterface{

    ID("id", false, DataType.INTEGER, null),
    STUDENT_ID("student_id", false, DataType.INTEGER, Student.class),
    DATE("date", true, DataType.DATE, null),
    TIME("time", true, DataType.INTEGER, null),
    PARKOUR_ID("parkour_id", false, DataType.REFERENCE, Parkour.class);

    public static final String DATABASE_TABLE_NAME = "measurement";

    private String databaseField;
    private boolean editable;
    private DataType dataType;
    private Class<? extends AbstractDatabaseObject> referenceType;

    MeasurementFields(String databaseField, boolean editable, DataType dataType, Class<? extends AbstractDatabaseObject> referenceType) {
        this.databaseField = databaseField;
        this.editable = editable;
        this.dataType = dataType;
        this.referenceType = referenceType;
    }

    @Override
    public String getDatabaseField() {
        return databaseField;
    }

    @Override
    public DataType getDataType() {
        return this.dataType;
    }

    @Override
    public boolean isEditable() {
        return this.editable;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getReferenceType() {
        return this.referenceType;
    }
}
