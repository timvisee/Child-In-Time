package me.childintime.childintime.database.entity.datatype;

public enum DataTypeExtended {

    /**
     * String type.
     * Used for general strings and text-like data.
     */
    STRING(DataTypeBase.STRING),

    /**
     * Password hash type.
     * Used for password hashes.
     */
    PASSWORD_HASH(DataTypeBase.STRING),

    /**
     * Integer type.
     * Used to store integral numbers.
     */
    INTEGER(DataTypeBase.INTEGER),

    /**
     * ID type.
     * Used to store object IDs.
     */
    ID(DataTypeBase.INTEGER),

    /**
     * Centimeter type.
     * Used to store centimeters.
     */
    CENTIMETER(DataTypeBase.INTEGER),

    /**
     * Gram (weight) type.
     * Used to store the weight in grams.
     */
    GRAM(DataTypeBase.INTEGER),

    /**
     * Milliseconds type.
     * Used to store time in milliseconds.
     */
    MILLISECONDS(DataTypeBase.INTEGER),

    /**
     * Permission level type.
     * Used to store the permission level.
     */
    PERMISSION_LEVEL(DataTypeBase.INTEGER),

    /**
     * Boolean type.
     * Used to store boolean types.
     */
    BOOLEAN(DataTypeBase.BOOLEAN),

    /**
     * Gender type.
     * Used to store gender states.
     */
    GENDER(DataTypeBase.BOOLEAN),

    /**
     * Date type.
     * Used to store dates.
     */
    DATE(DataTypeBase.DATE),

    /**
     * Birthday type.
     * Used to store dates.
     */
    BIRTHDAY(DataTypeBase.DATE),

    /**
     * Reference type.
     * Used to reference to a different entity by it's ID.
     */
    REFERENCE(DataTypeBase.REFERENCE);

    /**
     * The base data type that is used by this extended data type.
     */
    private DataTypeBase dataTypeBase;

    /**
     * Constructor.
     *
     * @param dataTypeBase Base data type.
     */
    DataTypeExtended(DataTypeBase dataTypeBase) {
        this.dataTypeBase = dataTypeBase;
    }

    /**
     * Get the base data type.
     *
     * @return Base data type.
     */
    public DataTypeBase getDataTypeBase() {
        return this.dataTypeBase;
    }
}
