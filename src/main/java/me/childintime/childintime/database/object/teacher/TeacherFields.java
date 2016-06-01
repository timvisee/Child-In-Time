package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.school.School;

public enum TeacherFields implements DatabaseFieldsInterface {

    ID("id", false, DataType.INTEGER, null),
    FIRST_NAME("first_name", true, DataType.STRING, null),
    LAST_NAME("last_name", true, DataType.STRING, null),
    IS_GYM("is_gym", true, DataType.BOOLEAN, null),
    SCHOOL_ID("school_id", true, DataType.REFERENCE, School.class);

    public static final String DATABASE_TABLE_NAME = "teacher";

    private String databaseField;
    private boolean editable;
    private DataType dataType;
    private Class<? extends AbstractDatabaseObject> referenceType;

    TeacherFields(String databaseField, boolean editable, DataType dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
