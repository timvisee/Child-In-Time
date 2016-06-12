package me.childintime.childintime.database.entity;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.listener.ChangeListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityManager {

    /**
     * List of entities loaded in this manager.
     */
    private List<AbstractEntity> entities = new ArrayList<>();

    /**
     * Change listeners.
     */
    private List<ChangeListener> changeListeners = new ArrayList<>();

    /**
     * Fetch all entities from the database.
     * The given fields will be cached into the entities itself, to prevent further queries from being executed.
     * The fetched list of entities will be cached in this manager for further usage.
     *
     * @return List of fetched entities.
     */
    @SuppressWarnings("unused")
    public List<AbstractEntity> fetchEntities() {
        return fetchEntities(getManifest().getDefaultFields());
    }

    /**
     * Fetch all entities from the database.
     * The given fields will be cached into the entities itself, to prevent further queries from being executed.
     * The fetched list of entities will be cached in this manager for further usage.
     *
     * @param fields Entity fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of fetched entities.
     */
    @SuppressWarnings("WeakerAccess")
    public List<AbstractEntity> fetchEntities(EntityFieldsInterface fields[]) {
        // Join the string, comma separated
        StringBuilder fieldsToFetch = new StringBuilder("id");
        for (EntityFieldsInterface field : fields)
            fieldsToFetch.append("`, `").append(field.getDatabaseField());

        // Create a list with fetched entities
        List<AbstractEntity> newEntities = new ArrayList<>();

        // Fetch the entities and their data from the database
        try {
            // Get the database connection
            final Connection connection = Core.getInstance().getDatabaseConnector().getConnection();

            // Create a statement to fetch the entities
            PreparedStatement fetchStatement = connection.prepareStatement(
                    "SELECT `" + fieldsToFetch.toString() + "` " +
                    "FROM `" + getManifest().getTableName() + "`"
            );

            // Fetch the data
            ResultSet result = fetchStatement.executeQuery();

            // Parse all data
            while(result.next()) {
                // Get the entity ID
                int id = result.getInt("id");

                // Create the entity instance
                AbstractEntity entity = getManifest().getEntity().getConstructor(int.class).newInstance(id);

                // Parse and cache the fields
                for (EntityFieldsInterface field : fields)
                    entity.parseField(field, result.getString(field.getDatabaseField()));

                // Add the entity to the list
                newEntities.add(entity);
            }

        } catch(Exception e){
            // Print the stack trace
            e.printStackTrace();

            // Return null
            return null;
        }

        // Set the list of entities
        this.entities.addAll(newEntities);

        // Fire the change event
        fireChangeEvent();

        // Return the list of entities
        return this.entities;
    }

    /**
     * Get the list of entities.
     * The list of entities will be fetched automatically from the database if they aren't cached yet.
     *
     * @return List of entities.
     */
    @SuppressWarnings("unused")
    public List<AbstractEntity> getEntities() {
        return getEntities(getManifest().getDefaultFields());
    }

    /**
     * Get the list of entities.
     * The list of entities will be fetched automatically from the database if they aren't cached yet.
     *
     * @param fields Entity fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of entities.
     */
    @SuppressWarnings("WeakerAccess")
    public List<AbstractEntity> getEntities(EntityFieldsInterface[] fields) {
        // Return the entities if cached
        if(hasCache())
            return this.entities;

        // Fetch the entities first, then return
        return fetchEntities(fields);
    }

    /**
     * Get a clone of the list of entities.
     * The list of entities will be fetched automatically from the database if they aren't cached yet.
     *
     * @return Clone of the list of entities.
     */
    public List<AbstractEntity> getEntitiesClone() {
        return getEntitiesClone(getManifest().getDefaultFields());
    }

    /**
     * Get a clone of the list of entities.
     * The list of entities will be fetched automatically from the database if they aren't cached yet.
     *
     * @param fields Entity fields to fetch and cache (using the same query, to improve performance).
     *
     * @return List of entities.
     */
    @SuppressWarnings("WeakerAccess")
    public List<AbstractEntity> getEntitiesClone(EntityFieldsInterface[] fields) {
        // Fetch the entities if they aren't fetched yet
        if(!hasCache())
            fetchEntities(fields);

        // Create a list with clones
        List<AbstractEntity> clones = new ArrayList<>();

        // Loop through each entity, and clone it
        for(AbstractEntity entity : this.entities)
            try {
                clones.add(entity.clone());

            } catch(CloneNotSupportedException e) {
                e.printStackTrace();
            }

        // Return the list of cloned entities
        return clones;
    }

    /**
     * Get the number of entities in the database.
     *
     * @return Number of entities, returns zero if an error occurred.
     */
    @SuppressWarnings("unused")
    public int getEntityCount() {
        // Return the number of entities from cache, if cached
        if(hasCache())
            return this.entities.size();

        // Query the database to get the entity count
        try {
            // Get the database connection
            final Connection connection = Core.getInstance().getDatabaseConnector().getConnection();

            // Prepare a query to count the number of entities
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
     * Check whether any entitys are cached.
     *
     * @return True if cached, false if not.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean hasCache() {
        return this.entities != null;
    }

    /**
     * Refresh the entities, reloading them from the database.
     */
    // TODO: Add parameter to define what fields to fetch with the first query?
    @SuppressWarnings("unused")
    public void refresh() {
        // Clear the list of entities
        this.entities.clear();

        // Fetch the entities
        fetchEntities();
    }

    /**
     * Get the entity manifest.
     *
     * @return Entity manifest.
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
