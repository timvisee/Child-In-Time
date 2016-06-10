package me.childintime.childintime.database.object;

import me.childintime.childintime.Core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityManager {

    /**
     * List of database objects loaded in this manager.
     */
    private List<AbstractEntity> objects = new ArrayList<>();

    /**
     * Change listeners.
     */
    private List<ChangeListener> changeListeners = new ArrayList<>();

    /**
     * Fetch all objects from the database.
     * The given fields will be cached into the objects itself, to prevent further queries from being executed.
     * The fetched list of objects will be cached in this manager for further usage.
     *
     * @return List of fetched objects.
     */
    @SuppressWarnings("unused")
    public List<AbstractEntity> fetchObjects() {
        return fetchObjects(getManifest().getDefaultFields());
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
    @SuppressWarnings("WeakerAccess")
    public List<AbstractEntity> fetchObjects(DatabaseFieldsInterface fields[]) {
        // Join the string, comma separated
        StringBuilder fieldsToFetch = new StringBuilder("id");
        for (DatabaseFieldsInterface field : fields)
            fieldsToFetch.append("`, `").append(field.getDatabaseField());

        // Create a list with fetched objects
        List<AbstractEntity> newObjects = new ArrayList<>();

        // Fetch the objects and their data from the database
        try {
            // Get the database connection
            final Connection connection = Core.getInstance().getDatabaseConnector().getConnection();

            // Create a statement to fetch the objects
            PreparedStatement fetchStatement = connection.prepareStatement(
                    "SELECT `" + fieldsToFetch.toString() + "` " +
                    "FROM `" + getManifest().getTableName() + "`"
            );

            // Fetch the data
            ResultSet result = fetchStatement.executeQuery();

            // Parse all data
            while(result.next()) {
                // Get the object ID
                int id = result.getInt("id");

                // Create the database object instance
                AbstractEntity databaseObject = getManifest().getObject().getConstructor(int.class).newInstance(id);

                // Parse and cache the fields
                for (DatabaseFieldsInterface field : fields)
                    databaseObject.parseField(field, result.getString(field.getDatabaseField()));

                // Add the object to the list
                newObjects.add(databaseObject);
            }

        } catch(Exception e){
            // Print the stack trace
            e.printStackTrace();

            // Return null
            return null;
        }

        // Set the list of objects
        this.objects.addAll(newObjects);

        // Fire the change event
        fireChangeEvent();

        // Return the list of objects
        return this.objects;
    }

    /**
     * Get the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @return List of objects.
     */
    @SuppressWarnings("unused")
    public List<AbstractEntity> getObjects() {
        return getObjects(getManifest().getDefaultFields());
    }

    /**
     * Get the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @param fields Database object fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of objects.
     */
    @SuppressWarnings("WeakerAccess")
    public List<AbstractEntity> getObjects(DatabaseFieldsInterface[] fields) {
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
    public List<AbstractEntity> getObjectsClone() {
        return getObjectsClone(getManifest().getDefaultFields());
    }

    /**
     * Get a clone of the list of objects.
     * The list of objects will be fetched automatically from the database if they aren't cached yet.
     *
     * @param fields Database object fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of objects.
     */
    @SuppressWarnings("WeakerAccess")
    public List<AbstractEntity> getObjectsClone(DatabaseFieldsInterface[] fields) {
        // Fetch the objects if they aren't fetched yet
        if(!hasCache())
            fetchObjects(fields);

        // Create a list with clones
        List<AbstractEntity> clones = new ArrayList<>();

        // Loop through each database object, and clone it
        for(AbstractEntity object : this.objects)
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
     * @return Number of objects, returns zero if an error occurred.
     */
    @SuppressWarnings("unused")
    public int getObjectCount() {
        // Return the number of objects from cache, if cached
        if(hasCache())
            return this.objects.size();

        // Query the database to get the object count
        try {
            // Get the database connection
            final Connection connection = Core.getInstance().getDatabaseConnector().getConnection();

            // Prepare a query to count the number of objects
            PreparedStatement countQuery = connection.prepareStatement("SELECT count(`id`) FROM `" + getManifest().getTableName() + "`");

            // Execute the query, and return the results
            ResultSet result = countQuery.executeQuery();
            return result.getInt(1);

        } catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Check whether any database objects are cached.
     *
     * @return True if cached, false if not.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean hasCache() {
        return this.objects != null;
    }

    /**
     * Refresh the objects, reloading them from the database.
     */
    // TODO: Add parameter to define what fields to fetch with the first query?
    @SuppressWarnings("unused")
    public void refresh() {
        // Clear the list of objects
        this.objects.clear();

        // Fetch the objects
        fetchObjects();
    }

    /**
     * Get the database object manifest.
     *
     * @return Database object manifest.
     */
    public abstract AbstractEntityManifest getManifest();

    /**
     * Add an change listener.
     *
     * @param listener Listener.
     */
    public void addChangeListener(ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    /**
     * Get all the registered change listeners.
     *
     * @return List of change listeners.
     */
    public List<ChangeListener> getChangeListeners() {
        return this.changeListeners;
    }

    /**
     * Remove the given change listener.
     *
     * @param listener Listener to remove.
     *
     * @return True if any listener was removed, false if not.
     */
    public boolean removeChangeListener(ChangeListener listener) {
        return this.changeListeners.remove(listener);
    }

    /**
     * Remove all change listeners.
     *
     * @return Number of change listeners that were removed.
     */
    public int removeAllChangeListeners() {
        // Remember the number of change listeners
        final int listenerCount = this.changeListeners.size();

        // Clear the list of listeners
        this.changeListeners.clear();

        // Return the number of cleared listeners
        return listenerCount;
    }

    /**
     * Fire the change listener event.
     */
    public void fireChangeEvent() {
        // Fire each registered listener
        this.changeListeners.forEach(ChangeListener::onChange);
    }
}
