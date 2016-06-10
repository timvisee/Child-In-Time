package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataTypeBase;
import me.childintime.childintime.database.object.DataTypeExtended;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.school.School;

public enum TeacherFields implements DatabaseFieldsInterface {

    /**
     * ID.
     * Identifier of a teacher object.
     */
    ID("ID", "id", false, false, false, DataTypeExtended.ID, null),

    /**
     * Teacher first name.
     * The first name of a teacher.
     */
    FIRST_NAME("First name", "first_name", true, false, false, DataTypeExtended.STRING, null),

    /**
     * Teacher last name.
     * The last name of a teacher.
     */
    LAST_NAME("Last name", "last_name", true, false, false, DataTypeExtended.STRING, null),

    /**
     * Teacher gym.
     * Defines whether this teacher is a gymnastics teacher.
     * True if the teacher is a gymnastics teacher, false if not.
     */
    GENDER("Gender", "gender", true, false, false, DataTypeExtended.GENDER, null),

    /**
     * Teacher gym.
     * Defines whether this teacher is a gymnastics teacher.
     * True if the teacher is a gymnastics teacher, false if not.
     */
    IS_GYM("Gymnastic teacher", "is_gym", true, false, false, DataTypeExtended.BOOLEAN, null),

    /**
     * School ID.
     * The school instance a teacher works at.
     */
    SCHOOL_ID("School", "school_id", true, false, false, DataTypeExtended.REFERENCE, School.class);

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
    TeacherFields(String displayName, String databaseField, boolean editable, boolean nullAllowed, boolean emptyAllowed, DataTypeExtended dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
}
