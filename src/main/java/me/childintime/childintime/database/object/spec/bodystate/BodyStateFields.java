package me.childintime.childintime.database.object.spec.bodystate;

import me.childintime.childintime.database.object.AbstractEntityManifest;
import me.childintime.childintime.database.object.EntityFieldsInterface;
import me.childintime.childintime.database.object.datatype.DataTypeBase;
import me.childintime.childintime.database.object.datatype.DataTypeExtended;
import me.childintime.childintime.database.object.spec.student.StudentManifest;

public enum BodyStateFields implements EntityFieldsInterface {

    /**
     * ID.
     * Identifier of a body state object.
     */
    ID("ID", "id", false, false, false, DataTypeExtended.ID, null),

    /**
     * Student ID.
     * The student of a body state instance.
     */
    STUDENT_ID("Student", "student_id", false, false, false, DataTypeExtended.REFERENCE, StudentManifest.getInstance()),

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
     * The referenced manifest of the type for fields of the {@link DataTypeExtended#REFERENCE} type.
     * Must be null if the data type is different.
     */
    private AbstractEntityManifest referenceManifest;

    /**
     * Constructor.
     *
     * @param displayName Display name.
     * @param databaseField Database field name.
     * @param editable True if this field is editable by the user, false if not.
     * @param nullAllowed True if a null value is allowed for this property.
     * @param emptyAllowed True if an empty value is allowed for this property field.
     * @param dataType Data type of the field.
     * @param referenceManifest Referenced class manifest if this field has the {@link DataTypeBase#REFERENCE} type.
     */
    BodyStateFields(String displayName, String databaseField, boolean editable, boolean nullAllowed, boolean emptyAllowed, DataTypeExtended dataType, AbstractEntityManifest referenceManifest) {
        this.displayName = displayName;
        this.databaseField = databaseField;
        this.editable = editable;
        this.nullAllowed = nullAllowed;
        this.emptyAllowed = emptyAllowed;
        this.dataType = dataType;
        this.referenceManifest = referenceManifest;
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

    public AbstractEntityManifest getReferenceManifest() {
        return this.referenceManifest;
    }

    @Override
    public AbstractEntityManifest getFieldManifest() {
        return getReferenceManifest() != null ? getReferenceManifest() : getManifest();
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return BodyStateManifest.getInstance();
    }
}