/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package me.childintime.childintime.database.entity.spec.couple.studentsport;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityCoupleFieldsInterface;
import me.childintime.childintime.database.entity.datatype.DataTypeBase;
import me.childintime.childintime.database.entity.datatype.DataTypeExtended;
import me.childintime.childintime.database.entity.spec.sport.SportManifest;
import me.childintime.childintime.database.entity.spec.student.StudentManifest;
import me.childintime.childintime.permission.PermissionLevel;

import java.util.ArrayList;
import java.util.List;

public enum StudentSportFields implements EntityCoupleFieldsInterface {

    /**
     * ID.
     * Identifier of a object.
     */
    ID("ID", "id", PermissionLevel.VIEW_ANONYMOUS, false, false, false, false, DataTypeExtended.ID, null),

    /**
     * Student.
     * The student of this couple.
     */
    STUDENT_ID("Student", "student_id", PermissionLevel.VIEW_ANONYMOUS, true, true, false, false, DataTypeExtended.REFERENCE, StudentManifest.getInstance()),

    /**
     * Sport.
     * The sport of this couple.
     */
    SPORT_ID("Sport", "sport_id", PermissionLevel.VIEW_ANONYMOUS, true, true, false, false, DataTypeExtended.REFERENCE, SportManifest.getInstance());

    /**
     * The display name for this field.
     */
    private String displayName;

    /**
     * The name of the field in the database.
     */
    private String databaseField;

    /**
     * Minimum required permission level.
     */
    private PermissionLevel minimumPermission;

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
     * @param minimumPermission Minimum required permission level.
     * @param creatable True if this field is creatable by the user, false if not.
     * @param editable True if this field is editable by the user, false if not.
     * @param nullAllowed True if a NULL value is allowed for this property field.
     * @param emptyAllowed True if an empty value is allowed for this property field.
     * @param dataType Data type of the field.
     * @param referenceManifest Referenced class manifest if this field has the {@link DataTypeExtended#REFERENCE} type.
     */
    StudentSportFields(String displayName, String databaseField, PermissionLevel minimumPermission, boolean creatable, boolean editable, boolean nullAllowed, boolean emptyAllowed, DataTypeExtended dataType, AbstractEntityManifest referenceManifest) {
        this.displayName = displayName;
        this.databaseField = databaseField;
        this.minimumPermission = minimumPermission;
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
    public PermissionLevel getMinimumPermission() {
        return this.minimumPermission;
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
        return StudentSportManifest.getInstance();
    }

    @Override
    public EntityCoupleFieldsInterface getFieldA() {
        return STUDENT_ID;
    }

    @Override
    public EntityCoupleFieldsInterface getFieldB() {
        return SPORT_ID;
    }

    public static StudentSportFields[] valuesAllowed() {
        // Create a list of allowed values
        List<StudentSportFields> list = new ArrayList<>();

        // Get the users permission level
        final PermissionLevel permissionLevel = Core.getInstance().getAuthenticator().getPermissionLevel();

        // Loop through the values and put all values in the list the user has permission for
        for(StudentSportFields value : values())
            if(value.getMinimumPermission().orBetter(permissionLevel))
                list.add(value);

        // Return the values array
        return list.toArray(new StudentSportFields[]{});
    }
}
