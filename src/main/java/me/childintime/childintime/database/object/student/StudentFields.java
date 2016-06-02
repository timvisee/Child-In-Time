package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.group.Group;

public enum StudentFields implements DatabaseFieldsInterface {

    /**
     * ID.
     * Identifier of a student object.
     */
    ID("id", false, DataType.INTEGER, null),

    /**
     * Student first name.
     * The first name of a student.
     */
    FIRST_NAME("first_name", true, DataType.STRING, null),

    /**
     * Student last name.
     * The last name of a student.
     */
    LAST_NAME("last_name", true, DataType.STRING, null),

    /**
     * Student gender.
     * The gender of a student.
     * True defines a man, false defines a woman.
     */
    GENDER("gender", true, DataType.BOOLEAN, null),

    /**
     * Student birthdate.
     * The birthdate of a student.
     */
    BIRTHDATE("birthdate", true, DataType.DATE, null),

    /**
     * Group ID.
     * The group instance a student is part of.
     */
    GROUP_ID("group_id", true, DataType.REFERENCE, Group.class);

    /**
     * Database table name for this object type.
     * This constant is dynamically accessed by {@link AbstractDatabaseObject#getTableName()}.
     */
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
