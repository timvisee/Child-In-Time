package me.childintime.childintime.database.object;

import me.childintime.childintime.Core;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public abstract class AbstractDatabaseObject implements Cloneable {

    /**
     * Database object ID'or negative one if the ID is unspecified.
     * A new object should be added to the database when the object is applied to the database while the ID is negative one.
     */
    protected int id;

    /**
     * Hash map containing cached fields from the database object.
     */
    protected HashMap<DatabaseFieldsInterface, Object> cachedFields = new HashMap<>();

    /**
     * Constructor.
     */
    public AbstractDatabaseObject() {
        this.id = -1;
    }

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public AbstractDatabaseObject(int id) {
        this.id = id;
    }

    /**
     * Get the database object ID.
     *
     * @return Database object ID.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the hash map containing the cached fields.
     *
     * @return Hash map containing the cached fields.
     */
    public HashMap<DatabaseFieldsInterface, Object> getCachedFields() {
        return this.cachedFields;
    }

    /**
     * Set the hash map containing the cached fields.
     *
     * @param cachedFields Hash map containing the cached fields.
     */
    public void setCachedFields(HashMap<DatabaseFieldsInterface, Object> cachedFields) {
        this.cachedFields = cachedFields;
    }

    /**
     * Clear/flush the cached database object fields.
     */
    @SuppressWarnings("unused")
    public void flushCache() {
        this.cachedFields.clear();
    }

    /**
     * Check whether the given database object fields are cached.
     *
     * @param fields The fields to check.
     *
     * @return True if all given fields are cached, false if not.
     * False is also returned if just one of the given fields isn't cached.
     * If no fields are given, true will be returned.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean hasFields(DatabaseFieldsInterface[] fields) {
        // Loop through all given fields
        for(DatabaseFieldsInterface field : fields) {
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
     * Check whether the given database object field is cached.
     *
     * @param field The field.
     *
     * @return True if the given field is cached, false if not.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean hasField(DatabaseFieldsInterface field) {
        return hasFields(new DatabaseFieldsInterface[]{field});
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
    public boolean fetchFields(DatabaseFieldsInterface[] fields) {
        // Make sure at least one field is fetched
        if(fields.length == 0)
            return true;

        // Create a string list to put all fields name to fetch in
        List<String> fieldNames = new ArrayList<>();

        // Put the fields name into the field names list
        for(DatabaseFieldsInterface field : fields) {
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
            for (DatabaseFieldsInterface field : fields)
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
     * Get the database table name for the database object.
     *
     * @return Database table name.
     */
    @Deprecated
    private String getTableName() {
        return getManifest().getTableName();
    }

    /**
     * Get the database object manifest.
     *
     * @return Database object manifest.
     */
    public abstract AbstractDatabaseObjectManifest getManifest();

    /**
     * Fetch the given database field.
     *
     * @param field The field to fetch.
     *
     * @return True if the given field was fetched successfully.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean fetchField(DatabaseFieldsInterface field) {
        return fetchFields(new DatabaseFieldsInterface[]{field});
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
    public List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception {
        // Create a list to put the field values into
        List<Object> fieldValues = new ArrayList<>();

        // Create a list of fields that need to be fetched
        List<DatabaseFieldsInterface> fieldsToFetch = new ArrayList<>();
        for(DatabaseFieldsInterface field : fields) {
            // Make sure the field enum that is used is for the current class
            if(!getManifest().getFields().isInstance(field))
                throw new Exception("Invalid database object fields configuration class used, not compatible with" +
                        "current database object type.");

            // Fetch the field if we haven't cached it yet
            if(!hasField(field))
                fieldsToFetch.add(field);
        }

        // Fetch the given fields
        if(!fetchFields(fieldsToFetch.toArray(new DatabaseFieldsInterface[]{})))
            throw new Exception("Failed to fetch database fields.");

        // Get all fields values, add it to the result list
        for(DatabaseFieldsInterface field : fields)
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
    public Object getField(DatabaseFieldsInterface field) throws Exception {
        return getFields(new DatabaseFieldsInterface[]{field}).get(0);
    }

    /**
     * Apply the properties stored by this database object, to the actual database.
     *
     * @return True on success, false on failure.
     */
    // FIXME: Method not fully made yet, complete it!
    public boolean applyToDatabase() {
//        // TODO: Loop through the cached fields that are in the hash map. (See this.cacedFields)
//        this.cachedFields.forEach((DatabaseFieldsInterface, o) -> {
//            // TODO: If the current field is an ID field, skip it and continue; to the next field in the list
//            if(DatabaseFieldsInterface.getExtendedDataType().equals(DataTypeExtended.ID))
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
//                                "SET `" + DatabaseFieldsInterface.getDatabaseField() + "` = ?" +
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
            final List<DatabaseFieldsInterface> fields = new ArrayList<>();
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
                    final DatabaseFieldsInterface field = fields.get(i);
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
                                    // Determine the SQL date
                                    java.sql.Date sqlDate;

                                    // Convert date objects
                                    if(value instanceof Date)
                                        sqlDate = new java.sql.Date(((Date) value).getTime());
                                    else {
                                        // Create a date format instance to parse the date
                                        // TODO: Define the date format somewhere global!
                                        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                        // Try to parse the value to an SQL date
                                        sqlDate = new java.sql.Date(dateFormat.parse(value.toString()).getTime());
                                    }

                                    // Attach the date
                                    insertStatement.setDate(i + 1, sqlDate);
                                    break;

                                case INTEGER:
                                    insertStatement.setInt(i + 1, (Integer) value);
                                    break;

                                case REFERENCE:
                                    // Determine the reference ID
                                    int id;

                                    // Get the ID from abstract database object instances
                                    if(value instanceof AbstractDatabaseObject)
                                        id = ((AbstractDatabaseObject) value).getId();
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

                // Execute the query to insert the database object into the database
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
                    throw new RuntimeException("Failed to create database object in database.");

                // Get the auto increment value, and set the object's ID
                this.id = result.getInt(1);

            } catch(SQLException e) {
                e.printStackTrace();
            }

        } else {
            // Loop through the hash map with cached fields
            for(Map.Entry<DatabaseFieldsInterface, Object> entry : this.cachedFields.entrySet()) {
                // Get the field and it's value
                final DatabaseFieldsInterface field = entry.getKey();
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
                                // Determine the SQL date
                                java.sql.Date sqlDate;

                                // Convert date objects
                                if(value instanceof Date)
                                    sqlDate = new java.sql.Date(((Date) value).getTime());
                                else {
                                    // Create a date format instance to parse the date
                                    // TODO: Define the date format somewhere global!
                                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                    // Try to parse the value to an SQL date
                                    sqlDate = new java.sql.Date(dateFormat.parse(value.toString()).getTime());
                                }

                                // Attach the date
                                updateStatement.setDate(1, sqlDate);
                                break;

                            case INTEGER:
                                updateStatement.setInt(1, (Integer) value);
                                break;

                            case REFERENCE:
                                // Determine the reference ID
                                int id;

                                // Get the ID from abstract database object instances
                                if(value instanceof AbstractDatabaseObject)
                                    id = ((AbstractDatabaseObject) value).getId();
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

                    // Attach the ID of the database object
                    updateStatement.setInt(2, getId());

                    // Make sure one database object is updated
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

        // Successfully updated database object, return the result
        return true;
    }

    /**
     * Delete this database object from the database.
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
    public AbstractDatabaseObject clone() throws CloneNotSupportedException {
        // Create a variable to put the new abstract database object in
        AbstractDatabaseObject clone;

        // Clone the database object class
        try {
            clone = getClass().getConstructor(int.class).newInstance(getId());

        } catch(InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            // Try to clone using Java
            clone = (AbstractDatabaseObject) super.clone();
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
     * The parsed fields are added to the hashmap.
     *
     * @param field Database field type.
     * @param rawField Raw field data.
     */
    void parseField(DatabaseFieldsInterface field, Object rawField) {
        switch(field.getExtendedDataType().getDataTypeBase()) {
            case STRING:
                this.cachedFields.put(field, rawField);
                break;

            case BOOLEAN:
                this.cachedFields.put(field, !rawField.equals("0"));
                break;

            case INTEGER:
                this.cachedFields.put(field, Integer.parseInt(String.valueOf(rawField)));
                break;

            case DATE:
                // Split the raw date string
                String[] rawDateSplitted = rawField.toString().split("-");

                // Parse the year, month and day values
                int dateYear = Integer.valueOf(rawDateSplitted[0]);
                int dateMonth = Integer.valueOf(rawDateSplitted[1]);
                int dateDay = Integer.valueOf(rawDateSplitted[2]);

                // Create a calender object with the proper date
                Calendar calendar = new GregorianCalendar(dateYear, dateMonth, dateDay);

                // Put the date into the cached fields
                this.cachedFields.put(field, calendar.getTime());
                break;

            case REFERENCE:
                // Get the object ID
                final int objectId = Integer.parseInt(String.valueOf(rawField));

                // Parse the referenced object
                try {
                    // Find the proper constructor of the referenced class, and instantiate the object with the fetched object ID
                    AbstractDatabaseObject object = field.getReferenceType().getDeclaredConstructor(int.class).newInstance(objectId);

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
     * Check whether the cached properties of this database object are equal to the cached fields of another database object.
     * The ID of both objects must be the same, or false will be returned.
     *
     * @param other Other database object instance.
     *
     * @return True if the cache is equal, false if not.
     */
    // TODO: This doesn't always seem to work. (Maybe because the ID field is included?)
    public boolean isCacheEqual(AbstractDatabaseObject other) {
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
        for(Map.Entry<DatabaseFieldsInterface, Object> entry : this.cachedFields.entrySet()) {
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
        return this.id == ((AbstractDatabaseObject) other).id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
