package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.database.object.school.SchoolFields;

import java.util.ArrayList;
import java.util.List;

public class Measurement extends AbstractDatabaseObject {

    /**
     * Database object type name.
     */
    private static final String TYPE_NAME = "Measurement";

    @Override
    public boolean hasFields(DatabaseFieldsInterface[] fields) {

        for (DatabaseFieldsInterface field : fields) {
            if(!(field instanceof MeasurementFields))
                return false;

            if(this.cachedFields.containsKey(field))
                return false;
        }

        return true;
    }

    @Override
    public boolean fetchFields(DatabaseFieldsInterface[] fields) {
        return false;
        // TODO: Implement this
    }

    @Override
    public List<Object> getFields(DatabaseFieldsInterface[] fields) throws Exception {

        List<Object> list = new ArrayList<>();

        for (DatabaseFieldsInterface field : fields) {
            if(!(field instanceof MeasurementFields))
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