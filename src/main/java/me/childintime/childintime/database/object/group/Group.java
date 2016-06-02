package me.childintime.childintime.database.object.group;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.bodystate.BodyStateFields;
import me.childintime.childintime.database.object.school.SchoolFields;

import java.util.ArrayList;
import java.util.List;

public class Group extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Group";

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public Group(int id) {
        super(id);
    }

    @Override
    public boolean hasFields(DatabaseFieldsInterface[] fields) {

        for (DatabaseFieldsInterface field : fields) {
            if(!(field instanceof GroupFields))
                return false;

            if(!this.cachedFields.containsKey(field))
                return false;
        }

        return true;
    }

    @Override
    protected String getTableName() {
        return GroupFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFieldsClass() {
        return GroupFields.class;
    }

    @Override
    public List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception {

        List<Object> list = new ArrayList<>();

        for (DatabaseFieldsInterface field : fields) {
            if(!(field instanceof GroupFields))
                throw new Exception("Invalid field");

            if(!hasField(field))
                if(!fetchField(field))
                    throw new Exception("Failed to fetch field");

            list.add(this.cachedFields.get(field));
        }

        return list;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public String getDisplayName() {
        try {
            // Build and return the display name
            return String.valueOf(getField(GroupFields.NAME));

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
