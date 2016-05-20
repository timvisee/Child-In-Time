package me.childintime.childintime.database;

public enum DatabaseType {
    /**
     * Integrated (local) database.
     */
    INTEGRATED(1, "Integrated database", IntegratedDatabase.class),

    /**
     * Remote database.
     */
    REMOTE(2, "Remote database", RemoteDatabase.class);

    /**
     * Database type ID.
     */
    private int typeId;

    /**
     * Description.
     */
    private String description;

    /**
     * Type class.
     */
    private Class<? extends AbstractDatabase> typeClass;

    /**
     * Constructor.
     *
     * @param typeId Database type ID.
     * @param description Description.
     */
    DatabaseType(int typeId, String description, Class<? extends AbstractDatabase> typeClass) {
        this.typeId = typeId;
        this.description = description;
        this.typeClass = typeClass;
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

    /**
     * Get the type class.
     *
     * @return Type class.
     */
    public Class<? extends AbstractDatabase> getTypeClass() {
        return this.typeClass;
    }

    /**
     * Determine the index of the current database type.
     *
     * @return Database type index.
     */
    public int getIndex() {
        // Loop through the list of values, and compare it to this one, to determine the index
        for(int i = 0; i < values().length; i++)
            if(values()[i].equals(this))
                return i;

        // Unable to find the index, return zero
        return 0;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
