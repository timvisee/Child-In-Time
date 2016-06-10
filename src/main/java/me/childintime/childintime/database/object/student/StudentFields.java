package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.*;
import me.childintime.childintime.database.object.group.Group;

public enum StudentFields implements DatabaseFieldsInterface {

    /**
     * ID.
     * Identifier of a student object.
     */
    ID("ID", "id", false, false, false, DataTypeExtended.ID, null),

    /**
     * Student first name.
     * The first name of a student.
     */
    FIRST_NAME("First name", "first_name", true, false, false, DataTypeExtended.STRING, null),

    /**
     * Student last name.return StudentManifest.getInstance();
     * The last name of a student.
     */
    LAST_NAME("Last name", "last_name", true, false, false, DataTypeExtended.STRING, null),

    /**
     * Student gender.return StudentManifest.getInstance();
     * The gender of a student.
     * True defines a man, false defines a woman.
     */
    GENDER("Gender", "gender", true, false, false, DataTypeExtended.GENDER, null),

    /**
     * Student birthdate.return StudentManifest.getInstance();
     * The birthdate of a student.
     */
    BIRTHDAY("Birthdate", "birthdate", true, false, false, DataTypeExtended.BIRTHDAY, null),

    /**
     * Group ID.return StudentManifest.getInstance();
     * The group instance a student is part of.
     */
    GROUP_ID("Group", "group_id", true, false, false, DataTypeExtended.REFERENCE, Group.class);

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
     * Defines whether a NULL value is allowed for this property.
     */
    private boolean nullAllowed;

    /**
     * Defines whether an empty value is allowed for this property field.
     */
    private boolean emptyAllowed;

    /**
     * The data type of the field.
     */
    private DataTypeExtended dataType;

    /**
     * The referenced type for fields of the {@link DataTypeExtended#REFERENCE} type.
     * Must be null if the data type is different.
     */
    private Class<? extends AbstractDatabaseObject> referenceType;

    /**
     * Constructor.
     *
     * @param displayName Display name.
     * @param databaseField Database field name.
     * @param editable True if this field is editable by the user, false if not.
     * @param nullAllowed True if a NULL value is allowed for this property field.
     * @param emptyAllowed True if an empty value is allowed for this property field.
     * @param dataType Data type of the field.
     * @param referenceType Referenced class if this field has the {@link DataTypeExtended#REFERENCE} type.
     */
    StudentFields(String displayName, String databaseField, boolean editable, boolean nullAllowed, boolean emptyAllowed, DataTypeExtended dataType, Class<? extends AbstractDatabaseObject> referenceType) {
        this.displayName = displayName;
        this.databaseField = databaseField;
        this.editable = editable;
        this.nullAllowed = nullAllowed;
        this.emptyAllowed = emptyAllowed;
        this.dataType = dataType;
        this.referenceType = referenceType;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getDatabaseField() {
        return databaseField;
    }

    @Override
    public DataTypeExtended getExtendedDataType() {
        return this.dataType;
    }

    @Override
    public DataTypeBase getBaseDataType() {
        return this.dataType.getDataTypeBase();
    }

    @Override
    public boolean isEditable() {
        return this.editable;
    }

    @Override
    public boolean isNullAllowed() {
        return this.nullAllowed;
    }

    @Override
    public boolean isEmptyAllowed() {
        return this.emptyAllowed;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getReferenceType() {
        return this.referenceType;
    }

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return StudentManifest.getInstance();
    }
}
