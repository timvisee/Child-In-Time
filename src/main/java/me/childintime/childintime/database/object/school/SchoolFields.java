package me.childintime.childintime.database.object.school;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public enum SchoolFields implements DatabaseFieldsInterface{

    /**
     * ID.
     * Identifier of a school object.
     */
    ID("id", false, DataType.INTEGER, null),

    /**
     * School name.
     * The name of a school.
     */
    NAME("name", true, DataType.STRING, null),

    /**
     * School commune.
     * The commune a school is located in.
     */
    COMMUNE("commune", true, DataType.STRING, null);

    public static final String DATABASE_TABLE_NAME = "school";

    private String databaseField;
    private boolean editable;
    private DataType dataType;
    private Class<? extends AbstractDatabaseObject> referenceType;

    SchoolFields(String databaseField, boolean editable, DataType dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
