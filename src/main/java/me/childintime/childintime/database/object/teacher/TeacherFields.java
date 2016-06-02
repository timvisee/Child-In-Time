package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.school.School;

public enum TeacherFields implements DatabaseFieldsInterface {

    /**
     * ID.
     * Identifier of a teacher object.
     */
    ID("id", false, DataType.INTEGER, null),

    /**
     * Teacher first name.
     * The first name of a teacher.
     */
    FIRST_NAME("first_name", true, DataType.STRING, null),

    /**
     * Teacher last name.
     * The last name of a teacher.
     */
    LAST_NAME("last_name", true, DataType.STRING, null),

    /**
     * Teacher gym.
     * Defines whether this teacher is a gymnastics teacher.
     * True if the teacher is a gymnastics teacher, false if not.
     */
    IS_GYM("is_gym", true, DataType.BOOLEAN, null),

    /**
     * School ID.
     * The school instance a teacher works at.
     */
    SCHOOL_ID("school_id", true, DataType.REFERENCE, School.class);

    /**
     * Database table name for this object type.
     * This constant is dynamically accessed by {@link AbstractDatabaseObject#getTableName()}.
     */
    public static final String DATABASE_TABLE_NAME = "teacher";

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

    TeacherFields(String databaseField, boolean editable, DataType dataType, Class<? extends AbstractDatabaseObject> referenceType) {
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
