package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.parkour.Parkour;
import me.childintime.childintime.database.object.student.Student;

public enum MeasurementFields implements DatabaseFieldsInterface{

    /**
     * ID.
     * Identifier of a measurement object.
     */
    ID("id", false, DataType.INTEGER, null),

    /**
     * Student ID.
     * The student instance a measurement is for.
     */
    STUDENT_ID("student_id", false, DataType.INTEGER, Student.class),

    /**
     * Measurement date.
     * The date a measurement was tracked on.
     */
    DATE("date", true, DataType.DATE, null),

    /**
     * Measurement time.
     * The time in milliseconds of a measurement.
     */
    TIME("time", true, DataType.INTEGER, null),

    /**
     * Parkour ID.
     * The parkour instance a measurement is tracked on.
     */
    PARKOUR_ID("parkour_id", false, DataType.REFERENCE, Parkour.class);

    /**
     * The name of the field in the database.
     */
    private String databaseField;

    /**
     * Defines whether this field is editable by the user.
     */
    private boolean editable;

    /**
     * The data type of the field.
     */
    private DataType dataType;

    /**
     * The referenced type for fields of the {@link DataType#REFERENCE} type.
     * Must be null if the data type is different.
     */
    private Class<? extends AbstractDatabaseObject> referenceType;

    /**
     * Constructor.
     *
     * @param databaseField Database field name.
     * @param editable True if this field is editable by the user, false if not.
     * @param dataType Data type of the field.
     * @param referenceType Referenced class if this field has the {@link DataType#REFERENCE} type.
     */
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
