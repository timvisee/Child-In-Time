package me.childintime.childintime.database.object.parkour;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public enum ParkourFields implements DatabaseFieldsInterface{

    /**
     * ID.
     * Identifier of a parkour object.
     */
    ID("id", false, DataType.INTEGER, null),

    /**
     * Description.
     * The description of a parkour.
     */
    DESCRIPTION("description", true, DataType.STRING, null);

    /**
     * Database table name for this object type.
     * This constant is dynamically accessed by {@link AbstractDatabaseObject#getTableName()}.
     */
    public static final String DATABASE_TABLE_NAME = "parkour";

    private String databaseField;
    private boolean editable;
    private DataType dataType;
    private Class<? extends AbstractDatabaseObject> referenceType;

    ParkourFields(String databaseField, boolean editable, DataType dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
