package me.childintime.childintime.database;

public abstract class AbstractDatabasePropertyPanel {

    /**
     * Apply the properties in the panel to the given database.
     *
     * @param database Database to apply the properties to.
     */
    public abstract void apply(AbstractDatabase database);
}
