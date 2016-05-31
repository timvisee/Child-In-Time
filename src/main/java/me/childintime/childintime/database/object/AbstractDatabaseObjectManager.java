package me.childintime.childintime.database.object;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatabaseObjectManager {

    /**
     * List of database objects loaded in this manager.
     */
    protected List<AbstractDatabaseObject> objects = new ArrayList<>();

    // TODO: Create the body of this object!
    // TODO: Shorten the name of this object?

    /**
     * Fetch all objects from the database.
     * The given fields will be cached into the objects itself, to prevent further queries from being executed.
     * The fetched list of objects will be cached in this manager for further usage.
     *
     * @param fields Database object fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of fetched objects.
     */
    public abstract List<AbstractDatabaseObject> fetchObjects(DatabaseFieldsInterface fields);

    /**
     * Get the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @param fields Database object fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of objects.
     */
    public abstract List<AbstractDatabaseObject> getObjects(DatabaseFieldsInterface[] fields);

    /**
     * Get the number of objects in the database.
     *
     * @return Number of objects.
     */
    public abstract int getObjectCount();

    /**
     * Check whether any database objects are cached.
     *
     * @return True if cached, false if not.
     */
    public abstract boolean hasCache();

    /**
     * Flush the cached database objects.
     */
    public abstract void flushCache();
}
