package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class BodyStateManager extends AbstractDatabaseObjectManager {

    @Override
    public DatabaseFieldsInterface[] getDefaultFields() {
        return new DatabaseFieldsInterface[0];
    }

    @Override
    public String getTypeName() {
        return "Body state";
    }

    @Override
    public String getTableName() {
        return BodyStateFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObjectClass() {
        return BodyState.class;
    }
}
