package me.childintime.childintime.database;

public enum DatabaseType {
    /**
     * Integrated (local) database.
     */
    INTEGRATED(1, "Integrated database"),

    /**
     * Remote database.
     */
    REMOTE(2, "Remote database");

    /**
     * Database type ID.
     */
    private int typeId;

    /**
     * Description.
     */
    private String description;

    /**
     * Constructor.
     *
     * @param typeId Database type ID.
     * @param description Description.
     */
    DatabaseType(int typeId, String description) {
        this.typeId = typeId;
        this.description = description;
    }

    /**
     * Get the database type by it's type ID.
     *
     * @param typeId Database type ID.
     *
     * @return Database type, or null if the type ID is unknown.
     */
    public static DatabaseType fromTypeId(int typeId) {
        // Loop through all types, and compare them
        for(DatabaseType type : values())
            if(type.getTypeId() == typeId)
                return type;

        // Failed to find the database type, return null
        return null;
    }

    /**
     * Get the database type ID.
     *
     * @return Type ID.
     */
    public int getTypeId() {
        return this.typeId;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
