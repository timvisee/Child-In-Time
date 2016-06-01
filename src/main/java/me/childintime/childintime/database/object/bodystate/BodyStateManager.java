package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;

public class BodyStateManager extends AbstractDatabaseObjectManager {


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
