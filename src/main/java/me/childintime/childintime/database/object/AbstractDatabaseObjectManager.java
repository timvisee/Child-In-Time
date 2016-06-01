package me.childintime.childintime.database.object;

import me.childintime.childintime.Core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        return fetchObjects(getDefaultFields());
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
    public List<AbstractDatabaseObject> fetchObjects(DatabaseFieldsInterface fields[]) {
        // Join the string, comma separated
        StringBuilder fieldsToFetch = new StringBuilder("id");
        for (DatabaseFieldsInterface field : fields)
            fieldsToFetch.append(", ").append(field.getDatabaseField());

        // Create a list with fetched objects
        List<AbstractDatabaseObject> objects = new ArrayList<>();

        // Fetch the objects and their data from the database
        try {
            // Create a statement to fetch the objects
            PreparedStatement fetchStatement = Core.getInstance().getDatabaseConnector().getConnection()
                    .prepareStatement("SELECT " + fieldsToFetch.toString() + " FROM " + getTableName());

            // Fetch the data
            ResultSet result = fetchStatement.executeQuery();

            // Parse all data
            while(result.next()) {
                // Get the object ID
                int id = result.getInt("id");

                // Create the database object instance
                AbstractDatabaseObject databaseObject = getObjectClass().getConstructor(Integer.class).newInstance(id);

                // Parse and cache the fields
                for (DatabaseFieldsInterface field : fields)
                    databaseObject.parseField(field, result.getString(field.getDatabaseField()));

                // Add the object to the list
                objects.add(databaseObject);
            }
        } catch(Exception e){
            System.out.println(e.toString());
        }

        // Set the list of objects
        this.objects = objects;

        // Return the list of objects
        return this.objects;
    }

    /**
     * Get the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @return List of objects.
     */
    public List<AbstractDatabaseObject> getObjects() {
        return getObjects(getDefaultFields());
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
        return getObjectsClone(getDefaultFields());
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
    public int getObjectCount() {
        int objectCount = 0;

        try{
            PreparedStatement countQuery = Core.getInstance().getDatabaseConnector().getConnection().prepareStatement("SELECT count(id) FROM " + getTableName());
            ResultSet result = countQuery.executeQuery();
            objectCount = result.getInt("count(id)");
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return objectCount;
    }

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
     * Get the default database object fields to fetch.
     *
     * @return Default object fields to fetch.
     */
    public abstract DatabaseFieldsInterface[] getDefaultFields();

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

    /**
     * Get the class of the database objects this manager is managing.
     *
     * @return Object class.
     */
    public abstract Class<? extends AbstractDatabaseObject> getObjectClass();
}
