package me.childintime.childintime.database.object;

public enum DataTypeExtended {

    /**
     * String type.
     * Used for general strings and text-like data.
     */
    STRING(DataType.STRING),

    /**
     * Integer type.
     * Used to store integral numbers.
     */
    INTEGER(DataType.INTEGER),

    /**
     * Boolean type.
     * Used to store boolean types.
     */
    BOOLEAN(DataType.BOOLEAN),

    /**
     * Gender type.
     * Used to store gender states.
     */
    GENDER(DataType.BOOLEAN),

    /**
     * Date type.
     * Used to store dates.
     */
    DATE(DataType.DATE),

    /**
     * Birthday type.
     * Used to store dates.
     */
    BIRTHDAY(DataType.DATE),

    /**
     * Reference type.
     * Used to reference to a different database object by it's ID.
     */
    REFERENCE(DataType.REFERENCE);

    /**
     * The base data type that is used by this extended data type.
     */
    private DataType dataType;

    /**
     * Constructor.
     *
     * @param dataType Base data type.
     */
    DataTypeExtended(DataType dataType) {
        this.dataType = dataType;
    }

    /**
     * Get the base data type.
     *
     * @return Base data type.
     */
    public DataType getDataType() {
        return this.dataType;
    }
}
