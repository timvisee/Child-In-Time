package me.childintime.childintime.database.object;

import me.childintime.childintime.Core;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractDatabaseObject implements Cloneable {

    /**
     * Database object ID'or negative one if the ID is unspecified.
     * A new object should be added to the database when the object is applied to the database while the ID is negative one.
     */
    protected final int id;

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
                parseField(field, result.getString(field.getDatabaseField()));

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

                // Attach the parameters to the prepared statement
                updateStatement.setObject(1, value);
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

        // Successfully updated database object, return the result
        return true;
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
    void parseField(DatabaseFieldsInterface field, String rawField) {
        switch(field.getExtendedDataType().getDataTypeBase()) {
            case STRING:
                this.cachedFields.put(field, rawField);
                break;

            case BOOLEAN:
                this.cachedFields.put(field, !rawField.equals("0"));
                break;

            case INTEGER:
                this.cachedFields.put(field, Integer.parseInt(rawField));
                break;

            case DATE:
                // Split the raw date string
                String[] rawDateSplitted = rawField.split("-");

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
                final int objectId = Integer.parseInt(rawField);

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
