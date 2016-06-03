package me.childintime.childintime.database.object;

public enum DataType {

    /**
     * String type.
     * Used for general strings and text-like data.
     */
    STRING,

    /**
     * Integer type.
     * Used to store integral numbers.
     */
    INTEGER,

    /**
     * Boolean type.
     * Used to store boolean types.
     */
    BOOLEAN,

    /**
     * Date type.
     * Used to store dates.
     */
    DATE,

    /**
     * Reference type.
     * Used to reference to a different database object by it's ID.
     */
    REFERENCE
}
