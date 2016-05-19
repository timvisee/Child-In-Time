package me.childintime.childintime;

public enum DatabaseType {
    /**
     * Integrated (local) database.
     */
    INTEGRATED("Integrated database"),

    /**
     * Remote database.
     */
    REMOTE("Remote database");

    /**
     * Description.
     */
    private String description;

    /**
     * Constructor.
     *
     * @param description Description.
     */
    DatabaseType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
