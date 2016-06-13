package me.childintime.childintime.database.entity.spec.parkour;

import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityFieldsInterface;
import me.childintime.childintime.database.entity.datatype.DataTypeBase;
import me.childintime.childintime.database.entity.datatype.DataTypeExtended;

public enum ParkourFields implements EntityFieldsInterface {

    /**
     * ID.
     * Identifier of a parkour object.
     */
    ID("ID", "id", false, false, false, false, DataTypeExtended.ID, null),

    /**
     * Description.
     * The description of a parkour.
     */
    DESCRIPTION("Parkour", "description", true, true, false, false, DataTypeExtended.STRING, null);

    /**
     * The display name for this field.
     */
    private String displayName;

    /**
     * The name of the field in the database.
     */
    private String databaseField;

    /**
     * Defines whether this field is creatable by the user.
     */
    private boolean creatable;

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
     * @param creatable True if this field is creatable by the user, false if not.
     * @param editable True if this field is editable by the user, false if not.
     * @param nullAllowed True if a NULL value is allowed for this property field.
     * @param emptyAllowed True if an empty value is allowed for this property field.
     * @param dataType Data type of the field.
     * @param referenceManifest Referenced class manifest if this field has the {@link DataTypeExtended#REFERENCE} type.
     */
    ParkourFields(String displayName, String databaseField, boolean creatable, boolean editable, boolean nullAllowed, boolean emptyAllowed, DataTypeExtended dataType, AbstractEntityManifest referenceManifest) {
        this.displayName = displayName;
        this.databaseField = databaseField;
        this.creatable = creatable;
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
    public boolean isCreatable() {
        return this.creatable;
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
        return ParkourManifest.getInstance();
    }
}
