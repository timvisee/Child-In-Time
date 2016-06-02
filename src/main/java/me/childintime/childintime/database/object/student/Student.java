package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.util.ArrayList;
import java.util.List;

public class Student extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Student";

    /**
     * Constructor.
     *
     * @param id Database object id.
     */
    public Student(int id) {
        super(id);
    }

    @Override
    public boolean hasFields(DatabaseFieldsInterface[] fields) {

        for(DatabaseFieldsInterface field : fields) {
            if(!(field instanceof StudentFields))
                return false;

            if(this.cachedFields.containsKey(field))
                return false;
        }

        return true;
    }

    @Override
    protected String getTableName() {
        return StudentFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends DatabaseFieldsInterface> getFieldsClass() {
        return StudentFields.class;
    }

    @Override
    public List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception {

        List<Object> list = new ArrayList<>();

        for (DatabaseFieldsInterface field : fields) {
            if(!(field instanceof StudentFields))
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
}
