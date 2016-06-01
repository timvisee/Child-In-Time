package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;


public class MeasurementManager extends AbstractDatabaseObjectManager{

    @Override
    public String getTypeName() {
        return "Measurement";
    }

    @Override
    public String getTableName() {
        return MeasurementFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObjectClass() {
        return Measurement.class;
    }
}
