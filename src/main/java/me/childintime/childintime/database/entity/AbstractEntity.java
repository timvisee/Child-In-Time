package me.childintime.childintime.database.entity;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.datatype.DataTypeExtended;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public abstract class AbstractEntity implements Cloneable {

    /**
     * Entity ID or negative one if the ID is unspecified.
     * A new object should be added to the database when the object is applied to the database while the ID is negative one.
     */
    protected int id;

    /**
     * Hash map containing cached fields from the entity.
     */
    protected HashMap<EntityFieldsInterface, Object> cachedFields = new HashMap<>();

    /**
     * Constructor.
     */
    public AbstractEntity() {
        this.id = -1;
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public AbstractEntity(int id) {
        this.id = id;
    }

    /**
     * Get the entity ID.
     *
     * @return Entity ID.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the hash map containing the cached fields.
     *
     * @return Hash map containing the cached fields.
     */
    public HashMap<EntityFieldsInterface, Object> getCachedFields() {
        return this.cachedFields;
    }

    /**
     * Set the hash map containing the cached fields.
     *
     * @param cachedFields Hash map containing the cached fields.
     */
    public void setCachedFields(HashMap<EntityFieldsInterface, Object> cachedFields) {
        this.cachedFields = cachedFields;
    }

    /**
     * Clear/flush the cached entity fields.
     */
    @SuppressWarnings("unused")
    public void flushCache() {
        this.cachedFields.clear();
    }

    /**
     * Check whether the given entity fields are cached.
     *
     * @param fields The fields to check.
     *
     * @return True if all given fields are cached, false if not.
     * False is also returned if just one of the given fields isn't cached.
     * If no fields are given, true will be returned.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean hasFields(EntityFieldsInterface[] fields) {
        // Loop through all given fields
        for(EntityFieldsInterface field : fields) {
            // Make sure the proper fields enum is used for this object
            if(!getManifest().getFields().isInstance(field))
                return false;

            // Make sure the field exists in cache
            if(!this.cachedFields.containsKey(field))
                return false;
        }

        // All keys exist, return the result
        return true;
    }

    /**
     * Check whether the given entity field is cached.
     *
     * @param field The field.
     *
     * @return True if the given field is cached, false if not.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean hasField(EntityFieldsInterface field) {
        return hasFields(new EntityFieldsInterface[]{field});
    }

    /**
     * Fetch the given database fields.
     * True will be returned if the given fields were fetched successfully.
     * True will also be returned if no fields are fetched, because the given fields array was empty.
     *
     * @param fields The fields to fetch.
     *
     * @return True on success, false on failure.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean fetchFields(EntityFieldsInterface[] fields) {
        // Make sure at least one field is fetched
        if(fields.length == 0)
            return true;

        // Create a string list to put all fields name to fetch in
        List<String> fieldNames = new ArrayList<>();

        // Put the fields name into the field names list
        for(EntityFieldsInterface field : fields) {
            // Make sure the field is of the correct instance
            if(!getManifest().getFields().isInstance(field))
                return false;

            // Add the field name to the list
            fieldNames.add(field.getDatabaseField());
        }

        // Join all the field names together to use it in the database query
        String fieldsToFetch = String.join("`, `", fieldNames);

        try {
            // Get the database connection
            final Connection connection = Core.getInstance().getDatabaseConnector().getConnection();

            // Prepare a statement to fetch the fields
            PreparedStatement fetchStatement = connection.prepareStatement(
                    "SELECT `" + fieldsToFetch.toString() + "` " +
                    "FROM `" + getTableName() + "` " +
                    "WHERE `id` = ? " +
                    "LIMIT 1"
            );

            // Set the prepared statement parameters
            fetchStatement.setInt(1, getId());

            // Execute the query
            ResultSet result = fetchStatement.executeQuery();

            // Throw an exception if no data is returned from the database
            if(!result.next())
                throw new Exception("Failed to fetch object data for [" + getClass().getSimpleName() + ";id=" + getId() + "], empty result received from the database.");

            // Get the raw data for each field, and parse it
            for (EntityFieldsInterface field : fields)
                parseField(field, result.getObject(field.getDatabaseField()));

            // Return the result
            return true;

        } catch (Exception e) {
            // Print the stack trace, and return the result
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get the database table name for the entity.
     *
     * @return Database table name.
     */
    @Deprecated
    private String getTableName() {
        return getManifest().getTableName();
    }

    /**
     * Get the entity manifest.
     *
     * @return Entity manifest.
     */
    public abstract AbstractEntityManifest getManifest();

    /**
     * Fetch the given database field.
     *
     * @param field The field to fetch.
     *
     * @return True if the given field was fetched successfully.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean fetchField(EntityFieldsInterface field) {
        return fetchFields(new EntityFieldsInterface[]{field});
    }

    /**
     * Get the given fields. All fields will be returned from cache when possible, fields that aren't cached are fetched
     * from the database automatically.
     * If an empty list of fields is given, an empty list will be returned.
     *
     * @param fields List of fields to get.
     *
     * @return List of field values, in the same order as the given fields list.
     *
     * @throws Exception Throws if an error occurred.
     */
    public List<Object> getFields(EntityFieldsInterface[] fields) throws Exception {
        // Create a list to put the field values into
        List<Object> fieldValues = new ArrayList<>();

        // Create a list of fields that need to be fetched
        List<EntityFieldsInterface> fieldsToFetch = new ArrayList<>();
        for(EntityFieldsInterface field : fields) {
            // Make sure the field enum that is used is for the current class
            if(!getManifest().getFields().isInstance(field))
                throw new Exception("Invalid entity fields configuration class used, not compatible with" +
                        "current entity type.");

            // Fetch the field if we haven't cached it yet
            if(!hasField(field))
                fieldsToFetch.add(field);
        }

        // Fetch the given fields
        if(!fetchFields(fieldsToFetch.toArray(new EntityFieldsInterface[]{})))
            throw new Exception("Failed to fetch database fields.");

        // Get all fields values, add it to the result list
        for(EntityFieldsInterface field : fields)
            fieldValues.add(this.cachedFields.get(field));

        // Return the list of field values
        return fieldValues;
    }

    /**
     * Get the given field. The field will be returned from cache when possible. If the field isn't available in cache,
     * it will be fetched from the database automatically.
     *
     * @param field The field to get.
     *
     * @return The field value.
     *
     * @throws Exception Throws if an error occurred.
     */
    @SuppressWarnings("WeakerAccess")
    public Object getField(EntityFieldsInterface field) throws Exception {
        return getFields(new EntityFieldsInterface[]{field}).get(0);
    }

    /**
     * Apply the properties stored by this entity, to the actual database.
     *
     * @return True on success, false on failure.
     */
    // FIXME: Method not fully made yet, complete it!
    public boolean applyToDatabase() {
//        // TODO: Loop through the cached fields that are in the hash map. (See this.cacedFields)
//        this.cachedFields.forEach((EntityFieldsInterface, o) -> {
//            // TODO: If the current field is an ID field, skip it and continue; to the next field in the list
//            if(EntityFieldsInterface.getExtendedDataType().equals(DataTypeExtended.ID))
//                return;
//
//            // TODO: Create a prepared statement (see line 126)
//            //
//            //       UPDATE: current field (See: currentField.getFieldName() )
//            //       NEW VALUES: current field value (which is inside the this.cachedFields hash map)
//            //       WHERE: id = getId()
//            //
//            //       The new value, and the ID should be a '?' in the prepared statement. Their proper values will
//            //       be attached/set later on
//
//            // TODO: Attach the parameters/values to the prepared statement (attach the new value, and the ID)
//
//            // TODO: Execute the prepared statement, with the attached parameters
//
//            try {
//                // Get the database connection
//                final Connection connection = Core.getInstance().getDatabaseConnector().getConnection();
//
//                // Prepare a statement to update
//                PreparedStatement updateStatement = connection.prepareStatement(
//                        "UPDATE `" + getTableName() + "` " +
//                                "SET `" + EntityFieldsInterface.getDatabaseField() + "` = ?" +
//                                "WHERE `id` = ?"
//                );
//
//                updateStatement.setObject(1, o);
//                updateStatement.setInt(2, getId());
//
//                int updateCount = updateStatement.executeUpdate();
//
//                if(updateCount == 1)
//                    return true;
//
//            } catch (Exception e) {
//                // Print the stack trace, and return the result
//                e.printStackTrace();
//            }
//        });
//
//        // TODO: Return true, because everything seems to be fine.
//        return false;

        // Make sure the this object has any cached fields
        if(this.cachedFields.size() == 0)
            return true;

        // Get the database connection
        final Connection connection;
        try {
            connection = Core.getInstance().getDatabaseConnector().getConnection();
        } catch(SQLException e) {
            throw new RuntimeException("Failed to connect to the database.", e);
        }

        // Check whether a new object should be added, or whether the object should be updated
        if(getId() < 0) {
            // Create a list of fields, field names and field values
            final List<EntityFieldsInterface> fields = new ArrayList<>();
            final List<String> fieldNames = new ArrayList<>();
            final List<String> fieldValues = new ArrayList<>();
            this.cachedFields.forEach((field, obj) -> {
                fields.add(field);
                fieldNames.add(field.getDatabaseField());
                fieldValues.add("?");
            });

            // Combine the field names
            final String fieldNamesQuery = "`" + String.join("`, `", fieldNames) + "`";
            final String fieldValuesQuery = String.join(", ", fieldValues);

            try {
                // Prepare a statement to insert
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO `" + getTableName() + "` " +
                        "(" + fieldNamesQuery + ") " +
                        "VALUES (" + fieldValuesQuery + ")"
                );

                // Loop through the hash map with cached fields
                for(int i = 0; i < fields.size(); i++) {
                    // Get the field and it's value
                    final EntityFieldsInterface field = fields.get(i);
                    final Object value = this.cachedFields.get(field);

                    // Set the ID field to null
                    if(field.getExtendedDataType().equals(DataTypeExtended.ID)) {
                        insertStatement.setNull(i + 1, Types.INTEGER);
                        continue;
                    }

                    // Set the parameter to null if the value is null, or properly attach the value
                    if(value != null)
                        try {
                            switch(field.getBaseDataType()) {
                                case STRING:
                                default:
                                    insertStatement.setString(i + 1, String.valueOf(value));
                                    break;

                                case BOOLEAN:
                                    insertStatement.setInt(i + 1, Boolean.parseBoolean(String.valueOf(value)) ? 1 : 0);
                                    break;

                                case DATE:
                                    // Create a string to store the formatted ISO date in
                                    String dateString;

                                    // Create a date formatter
                                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                    // Store the value in
                                    if(!(value instanceof Date))
                                        // TODO: Does this work properly for strings
                                        dateString = dateFormat.format(dateFormat.parse(String.valueOf(value)));
                                    else
                                        // Format the date string to an ISO date
                                        dateString = dateFormat.format(value);

                                    // Attach the date
                                    insertStatement.setString(i + 1, dateString);
                                    break;

                                case INTEGER:
                                    insertStatement.setInt(i + 1, (Integer) value);
                                    break;

                                case REFERENCE:
                                    // Determine the reference ID
                                    int id;

                                    // Get the ID from abstract entity instances
                                    if(value instanceof AbstractEntity)
                                        id = ((AbstractEntity) value).getId();
                                    else
                                        id = (Integer) value;

                                    // Put it's ID in the database
                                    insertStatement.setInt(i + 1, id);
                                    break;
                            }
                        } catch(ParseException e) {
                            e.printStackTrace();
                        }

                    else
                        switch(field.getBaseDataType()) {
                            case STRING:
                            default:
                                insertStatement.setNull(i + 1, Types.VARCHAR);
                                break;

                            case BOOLEAN:
                            case INTEGER:
                            case REFERENCE:
                                insertStatement.setNull(i + 1, Types.INTEGER);
                                break;

                            case DATE:
                                insertStatement.setNull(i + 1, Types.DATE);
                                break;
                        }
                }

                // Execute the query to insert the entity into the database
                insertStatement.execute();

                // Fetch the last auto increment value from the database
                ResultSet result = null;
                switch(Core.getInstance().getDatabaseConnector().getDialect()) {
                    case MYSQL:
                        result = insertStatement.executeQuery("SELECT LAST_INSERT_ID();");
                        break;
                    case SQLITE:
                        result = insertStatement.getGeneratedKeys();
                        break;
                }

                // Make sure at least one result is available
                if(!result.next())
                    throw new RuntimeException("Failed to create entity in database.");

                // Get the auto increment value, and set the object's ID
                this.id = result.getInt(1);

            } catch(SQLException e) {
                e.printStackTrace();
            }

        } else {
            // Loop through the hash map with cached fields
            for(Map.Entry<EntityFieldsInterface, Object> entry : this.cachedFields.entrySet()) {
                // Get the field and it's value
                final EntityFieldsInterface field = entry.getKey();
                final Object value = entry.getValue();

                // Skip cached ID fields, because the ID field may never change
                if(field.getExtendedDataType().equals(DataTypeExtended.ID))
                    continue;

                try {
                    // Prepare a statement to update
                    PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE `" + getTableName() + "` " +
                                    "SET `" + field.getDatabaseField() + "` = ? " +
                                    "WHERE `id` = ?"
                    );

                    // Set the parameter to null if the value is null, or properly attach the value
                    if(value != null)
                        switch(field.getBaseDataType()) {
                            case STRING:
                            default:
                                updateStatement.setString(1, String.valueOf(value));
                                break;

                            case BOOLEAN:
                                updateStatement.setInt(1, Boolean.parseBoolean(String.valueOf(value)) ? 1 : 0);
                                break;

                            case DATE:
                                // Create a string to store the formatted ISO date in
                                String dateString;

                                // Create a date formatter
                                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                // Store the value in
                                if(!(value instanceof Date))
                                    // TODO: Does this work properly for strings
                                    dateString = dateFormat.format(dateFormat.parse(String.valueOf(value)));
                                else
                                    // Format the date string to an ISO date
                                    dateString = dateFormat.format(value);

                                // Attach the date
                                updateStatement.setString(1, dateString);
                                break;

                            case INTEGER:
                                updateStatement.setInt(1, (Integer) value);
                                break;

                            case REFERENCE:
                                // Determine the reference ID
                                int id;

                                // Get the ID from abstract entity instances
                                if(value instanceof AbstractEntity)
                                    id = ((AbstractEntity) value).getId();
                                else
                                    id = (Integer) value;

                                // Put it's ID in the database
                                updateStatement.setInt(1, id);
                                break;
                        }

                    else
                        switch(field.getBaseDataType()) {
                            case STRING:
                            default:
                                updateStatement.setNull(1, Types.VARCHAR);
                                break;

                            case BOOLEAN:
                            case INTEGER:
                            case REFERENCE:
                                updateStatement.setNull(1, Types.INTEGER);
                                break;

                            case DATE:
                                updateStatement.setNull(1, Types.DATE);
                                break;
                        }

                    // Attach the ID of the entity
                    updateStatement.setInt(2, getId());

                    // Make sure one entity is updated
                    if(updateStatement.executeUpdate() != 1)
                        return false;

                } catch (Exception e) {
                    // Print the stack trace, and return the result
                    e.printStackTrace();

                    // Return false
                    return false;
                }
            }
        }

        // Successfully updated entity, return the result
        return true;
    }

    /**
     * Delete this entity from the database.
     *
     * @return True on success, false on failure.
     */
    public boolean deleteFromDatabase() {
        // Return false if the ID is negative
        if(getId() < 0)
            return false;

        try {
            // Get the database connection
            final Connection connection = Core.getInstance().getDatabaseConnector().getConnection();

            // Create a prepared statement
            PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM `" + getTableName() + "`" +
                    "WHERE `id`=?"
            );

            // Attach the ID
            deleteStatement.setInt(1, getId());

            // Execute the query
            deleteStatement.execute();

            // Return the result
            return true;

        } catch(SQLException e) {
            throw new RuntimeException("Failed to connect to the database.", e);
        }
    }

    @Override
    public AbstractEntity clone() throws CloneNotSupportedException {
        // Create a variable to put the new abstract entity in
        AbstractEntity clone;

        // Clone the entity class
        try {
            clone = getClass().getConstructor(int.class).newInstance(getId());

        } catch(InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            // Try to clone using Java
            clone = (AbstractEntity) super.clone();
        }

        // Create a new hash map, and put it into the cloned object
        // TODO: Should we properly clone all hash map entries?
        clone.setCachedFields(new HashMap<>(this.cachedFields));

        // Return the cloned object
        return clone;
    }

    /**
     * Parse a raw database field.
     * This parses the field into it's proper data type.
     * The parsed fields are added to the hash map.
     *
     * @param field Database field type.
     * @param rawField Raw field data.
     */
    void parseField(EntityFieldsInterface field, Object rawField) {
        switch(field.getExtendedDataType().getDataTypeBase()) {
            case STRING:
                this.cachedFields.put(field, rawField);
                break;

            case BOOLEAN:
                this.cachedFields.put(field, Integer.valueOf(String.valueOf(rawField)) != 0);
                break;

            case INTEGER:
                this.cachedFields.put(field, Integer.parseInt(String.valueOf(rawField)));
                break;

            case DATE:
                // Create a date formatter to parse the date
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // Parse the date and put it in the cached fields
                try {
                    this.cachedFields.put(field, dateFormat.parse(String.valueOf(rawField)));
                } catch(ParseException e) {
                    e.printStackTrace();
                }
                break;

            case REFERENCE:
                // Get the object ID
                final int objectId = Integer.parseInt(String.valueOf(rawField));

                // Parse the referenced object
                try {
                    // Find the proper constructor of the referenced class, and instantiate the object with the fetched object ID
                    AbstractEntity object = field.getReferenceManifest().getEntity().getDeclaredConstructor(int.class).newInstance(objectId);

                    // Put the reference into the cached fields
                    this.cachedFields.put(field, object);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                throw new Error("Invalid database field data type.");
        }
    }

    /**
     * Get the display name for this object.
     * This can be used to show the object in a list.
     *
     * @return Display name.
     */
    public abstract String getDisplayName();

    @Override
    public String toString() {
        // Return the display name
        return getDisplayName();
    }

    /**
     * Check whether the cached properties of this entity are equal to the cached fields of another entity.
     * The ID of both objects must be the same, or false will be returned.
     *
     * @param other Other entity instance.
     *
     * @return True if the cache is equal, false if not.
     */
    // TODO: This doesn't always seem to work. (Maybe because the ID field is included?)
    public boolean isCacheEqual(AbstractEntity other) {
        // Compare the ID
        if(getId() != other.getId())
            return false;

        // Return true if both objects are perfectly equal
        if(this == other)
            return true;

        // Return true if both objects don't have any cached fields
        if(getCachedFields().size() == 0 && other.getCachedFields().size() == 0)
            return true;

        // Return false if the number of cached fields is different
        if(getCachedFields().size() != other.getCachedFields().size())
            return false;

        // Loop through the hash map to compare
        for(Map.Entry<EntityFieldsInterface, Object> entry : this.cachedFields.entrySet()) {
            // Skip ID fields
            if(entry.getKey().getExtendedDataType().equals(DataTypeExtended.ID))
                continue;

            // Make sure the other has this key
            if(!other.getCachedFields().containsKey(entry.getKey())) {
                System.out.println("MISSING KEY: " + entry.getKey());
                return false;
            }

            // Make sure the value is equal
            if(!other.getCachedFields().get(entry.getKey()).equals(entry.getValue())) {
                System.out.println("A: " + entry.getValue());
                System.out.println("B: " + other.getCachedFields().get(entry.getKey()));
                return false;
            }
        }

        // Everything seems to be all right, return true
        return true;
    }

    @Override
    public boolean equals(Object other) {
        // Return true if the object is the same instance
        if(this == other)
            return true;

        // Return false if the other object is null
        if(other == null || getClass() != other.getClass())
            return false;

        // If the ID is negative, use Java's default equals method
        if(this.id < 0)
            return super.equals(other);

        // Compare the ID's
        return this.id == ((AbstractEntity) other).id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
