package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataTypeBase;
import me.childintime.childintime.database.object.DataTypeExtended;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.parkour.Parkour;
import me.childintime.childintime.database.object.student.Student;

public enum MeasurementFields implements DatabaseFieldsInterface{

    /**
     * ID.
     * Identifier of a measurement object.
     */
    ID("ID", "id", false, false, false, DataTypeExtended.ID, null),

    /**
     * Student ID.
     * The student instance a measurement is for.
     */
    STUDENT_ID("Student", "student_id", false, false, false, DataTypeExtended.REFERENCE, Student.class),

    /**
     * Measurement date.
     * The date a measurement was tracked on.
     */
    DATE("Measurement date", "date", true, false, false, DataTypeExtended.DATE, null),

    /**
     * Measurement time.
     * The time in milliseconds of a measurement.
     */
    TIME("Time", "time", true, false, false, DataTypeExtended.MILLISECONDS, null),

    /**
     * Parkour ID.
     * The parkour instance a measurement is tracked on.
     */
    PARKOUR_ID("Parkour", "parkour_id", false, false, false, DataTypeExtended.REFERENCE, Parkour.class);

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
    MeasurementFields(String displayName, String databaseField, boolean editable, boolean nullAllowed, boolean emptyAllowed, DataTypeExtended dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
