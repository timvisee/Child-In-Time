package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataTypeBase;
import me.childintime.childintime.database.object.DataTypeExtended;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.student.Student;

public enum BodyStateFields implements DatabaseFieldsInterface{

    /**
     * ID.
     * Identifier of a body state object.
     */
    ID("ID", "id", false, false, false, DataTypeExtended.ID, null),

    /**
     * Student ID.
     * The student of a body state instance.
     */
    STUDENT_ID("Student", "student_id", false, false, false, DataTypeExtended.REFERENCE, Student.class),

    /**
     * Measurement date.
     * The date a body state has been measured on.
     */
    DATE("Measurement date", "date", true, false, false, DataTypeExtended.DATE, null),

    /**
     * Body state length.
     * The body length in centimeters.
     */
    LENGTH("Length", "length", true, false, false, DataTypeExtended.CENTIMETER, null),

    /**
     * Body state weight.
     * The body weight in grams.
     */
    WEIGHT("Weight", "weight", true, false, false, DataTypeExtended.GRAM, null);

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
     * @param nullAllowed True if a null value is allowed for this property.
     * @param emptyAllowed True if an empty value is allowed for this property field.
     * @param dataType Data type of the field.
     * @param referenceType Referenced class if this field has the {@link DataTypeBase#REFERENCE} type.
     */
    BodyStateFields(String displayName, String databaseField, boolean editable, boolean nullAllowed, boolean emptyAllowed, DataTypeExtended dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
