package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.group.Group;

import java.util.Locale;

public enum StudentFields implements DatabaseFieldsInterface {

    /**
     * ID.
     * Identifier of a student object.
     */
    ID("ID", "id", false, DataType.INTEGER, null),

    /**
     * Student first name.
     * The first name of a student.
     */
    FIRST_NAME("First name", "first_name", true, DataType.STRING, null),

    /**
     * Student last name.
     * The last name of a student.
     */
    LAST_NAME("Last name", "last_name", true, DataType.STRING, null),

    /**
     * Student gender.
     * The gender of a student.
     * True defines a man, false defines a woman.
     */
    GENDER("Gender", "gender", true, DataType.BOOLEAN, null),

    /**
     * Student birthdate.
     * The birthdate of a student.
     */
    BIRTHDATE("Birthdate", "birthdate", true, DataType.DATE, null),

    /**
     * Group ID.
     * The group instance a student is part of.
     */
    GROUP_ID("Group", "group_id", true, DataType.REFERENCE, Group.class);

    /**
     * The display name for this field.
     */
    private String displayName;

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
     * @param displayName Display name.
     * @param databaseField Database field name.
     * @param editable True if this field is editable by the user, false if not.
     * @param dataType Data type of the field.
     * @param referenceType Referenced class if this field has the {@link DataType#REFERENCE} type.
     */
    StudentFields(String displayName, String databaseField, boolean editable, DataType dataType, Class<? extends AbstractDatabaseObject> referenceType) {
        this.displayName = displayName;
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

    @Override
    public String getDisplayName() {
        return toString().replace("_", " ").toLowerCase().toUpperCase(Locale.US);
    }
}
