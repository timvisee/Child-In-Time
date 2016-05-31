package me.childintime.childintime.database.object;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatabaseObjectManager {

    /**
     * List of database objects loaded in this manager.
     */
    protected List<AbstractDatabaseObject> objects = null;

    // TODO: Create the body of this object!
    // TODO: Shorten the name of this object?

    /**
     * Fetch all objects from the database.
     * The given fields will be cached into the objects itself, to prevent further queries from being executed.
     * The fetched list of objects will be cached in this manager for further usage.
     *
     * @return List of fetched objects.
     */
    public List<AbstractDatabaseObject> fetchObjects() {
        // TODO: Use the default fields here!
        return fetchObjects(null);
    }

    /**
     * Fetch all objects from the database.
     * The given fields will be cached into the objects itself, to prevent further queries from being executed.
     * The fetched list of objects will be cached in this manager for further usage.
     *
     * @param fields Database object fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of fetched objects.
     */
    public abstract List<AbstractDatabaseObject> fetchObjects(DatabaseFieldsInterface[] fields);

    /**
     * Get the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @return List of objects.
     */
    public List<AbstractDatabaseObject> getObjects() {
        // TODO: Use the default fields here!
        return getObjects(null);
    }

    /**
     * Get the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @param fields Database object fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of objects.
     */
    public List<AbstractDatabaseObject> getObjects(DatabaseFieldsInterface[] fields) {
        // Return the objects if cached
        if(hasCache())
            return this.objects;

        // Fetch the objects first, then return
        return fetchObjects(fields);
    }

    /**
     * Get a clone of the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @return Clone of the list of objects.
     */
    public List<AbstractDatabaseObject> getObjectsClone() {
        // TODO: Use the default fields here!
        return getObjectsClone(null);
    }

    /**
     * Get a clone of the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @param fields Database object fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of objects.
     */
    public List<AbstractDatabaseObject> getObjectsClone(DatabaseFieldsInterface[] fields) {
        // Fetch the objects if they aren't fetched yet
        if(!hasCache())
            fetchObjects(fields);

        // Create a list with clones
        List<AbstractDatabaseObject> clones = new ArrayList<>();

        // Loop through each database object, and clone it
        for(AbstractDatabaseObject object : this.objects)
            try {
                clones.add(object.clone());

            } catch(CloneNotSupportedException e) {
                e.printStackTrace();
            }

        // Return the list of cloned objects
        return clones;
    }

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
    public boolean hasCache() {
        return this.objects != null;
    }

    /**
     * Flush the cached database objects.
     */
    public void flushCache() {
        // Clear the list of objects
        this.objects.clear();

        // Reset the cache
        this.objects = null;
    }

    /**
     * Get the name of the current database object manager type.
     *
     * @return Database object manager type name.
     */
    public abstract String getTypeName();

    /**
     * Get the database table name for this object manager.
     *
     * @return Database table name.
     */
    public abstract String getTableName();
}