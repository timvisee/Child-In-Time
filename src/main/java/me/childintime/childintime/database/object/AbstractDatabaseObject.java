package me.childintime.childintime.database.object;

import com.sun.istack.internal.NotNull;

import me.childintime.childintime.Core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public abstract class AbstractDatabaseObject implements Cloneable {

    /**
     * Database object ID.
     */
    @NotNull
    protected final int id;

    /**
     * Hashmap containing cached fields from the database object.
     */
    protected HashMap<DatabaseFieldsInterface, Object> cachedFields = new HashMap<>();

    /**
     * Get a hashmap of cached fields.
     *
     * @return Hashmap of cached fields.
     */
    public HashMap<DatabaseFieldsInterface, Object> getCachedFields() {
        return this.cachedFields;
    }

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public AbstractDatabaseObject(@NotNull int id) {
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
     * Clear the cached database object fields.
     */
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
     */
    public abstract boolean hasFields(DatabaseFieldsInterface[] fields);

    /**
     * Check whether the given database object field is cached.
     *
     * @param field The field.
     *
     * @return True if the given field is cached, false if not.
     */
    public boolean hasField(DatabaseFieldsInterface field) {
        return hasFields(new DatabaseFieldsInterface[]{field});
    }

    /**
     * Fetch the given database fields.
     *
     * @param fields The fields to fetch.
     *
     * @return True if the given fields were fetched successfully.
     */
    public boolean fetchFields(DatabaseFieldsInterface[] fields) {

        List<String> fieldNames = new ArrayList<>();

        for (DatabaseFieldsInterface field : fields) {
            if (!getFieldsClass().isInstance(field))
                return false;

            // Add the fieldname to the list
            fieldNames.add(field.getDatabaseField());
        }

        String fieldsToFetch = String.join(", ", fieldNames);

        try {
            PreparedStatement fetchStatement = Core.getInstance().getDatabaseConnector().getConnection()
                    .prepareStatement("SELECT " + fieldsToFetch.toString() + " FROM " + getTableName() + "" +
                            " WHERE `id` = " + String.valueOf(getId()));

            ResultSet result = fetchStatement.executeQuery();

            if(!result.next()) {
                throw new Exception("Failed to fetch object data");
            }

            for (DatabaseFieldsInterface field : fields) {
                parseField(field, result.getString(field.getDatabaseField()));
            }

            return true;

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return false;
    }

    protected abstract String getTableName();

    public abstract Class<? extends DatabaseFieldsInterface> getFieldsClass();

    /**
     * Fetch the given database field.
     *
     * @param field The field to fetch.
     *
     * @return True if the given field was fetched successfully.
     */
    public boolean fetchField(DatabaseFieldsInterface field) {
        return fetchFields(new DatabaseFieldsInterface[]{field});
    }

    /**
     * Get the given fields. All fields will be returned from cache when possible, fields that aren't cached are fetched
     * from the database automatically.
     *
     * @param fields Fields to get.
     *
     * @return List of field values.
     *
     * @throws Exception Throws if an error occurred.
     */
    public abstract List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception;

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
    public Object getField(DatabaseFieldsInterface field) throws Exception {
        return getFields(new DatabaseFieldsInterface[]{field}).get(0);
    }

    /**
     * Get the name of the current database object type.
     *
     * @return Database object type name.
     */
    public abstract String getTypeName();

    @Override
    protected AbstractDatabaseObject clone() throws CloneNotSupportedException {
        // Clone through the super
        return (AbstractDatabaseObject) super.clone();
    }

    /**
     * Parse a raw database field.
     * This parses the field into it's proper data type.
     * The parsed fields are added to the hashmap.
     *
     * @param field Database field type.
     * @param rawField Raw field data.
     */
    public void parseField(DatabaseFieldsInterface field, String rawField) {
        switch(field.getDataType()) {
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
//                // Split de datum string
//                String[] rawDateSplitted = geb_datumField.getText().split("-");
//
//                // Haal het jaar, de maand en de dag op uit de string
//                int dateYear = Integer.valueOf(rawDateSplitted[0]);
//                int dateMonth = Integer.valueOf(rawDateSplitted[1]);
//                int dateDay = Integer.valueOf(rawDateSplitted[2]);
//
//                // Maak een kalender object met de opgehaalde datum
//                Calendar calendar = new GregorianCalendar(dateYear, dateMonth, dateDay);
//
//                // Zet de datum om naar een SQL datum (met een timestamp)
//                java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());

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
}
