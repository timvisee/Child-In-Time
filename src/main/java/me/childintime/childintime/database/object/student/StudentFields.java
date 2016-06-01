package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.group.Group;

public enum StudentFields implements DatabaseFieldsInterface {

    ID("databaseField", false, DataType.INTEGER, null),
    FIRST_NAME("first_name", true, DataType.STRING, null),
    LAST_NAME("last_name", true, DataType.STRING, null),
    GENDER("gender", true, DataType.BOOLEAN, null),
    BIRTHDATE("birthdate", true, DataType.DATE, null),
    GROUP_ID("group_id", true, DataType.REFERENCE, Group.class);

    public static final String DATABASE_TABLE_NAME = "student";

    private String databaseField;
    private boolean editable;
    private DataType dataType;
    private Class<? extends AbstractDatabaseObject> referenceType;

    StudentFields(String databaseField, boolean editable, DataType dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
