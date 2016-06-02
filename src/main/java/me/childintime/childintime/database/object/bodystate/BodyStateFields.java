package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.student.Student;

public enum BodyStateFields implements DatabaseFieldsInterface{

    /**
     * ID.
     * Identifier of a body state object.
     */
    ID("id", false, DataType.INTEGER, null),

    /**
     * Student ID.
     * The student of a body state instance.
     */
    STUDENT_ID("student_id", false, DataType.INTEGER, Student.class),

    /**
     * Measurement date.
     * The date a body state has been measured on.
     */
    DATE("date", true, DataType.DATE, null),

    /**
     * Body state length.
     * The body length in centimeters.
     */
    LENGTH("length", true, DataType.INTEGER, null),

    /**
     * Body state weight.
     * The body weight in grams.
     */
    WEIGHT("weight", true, DataType.INTEGER, null);

    /**
     * Database table name for the body state objects.
     */
    public static final String DATABASE_TABLE_NAME = "bodystate";

    private String databaseField;
    private boolean editable;
    private DataType dataType;
    private Class<? extends AbstractDatabaseObject> referenceType;

    BodyStateFields(String databaseField, boolean editable, DataType dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
