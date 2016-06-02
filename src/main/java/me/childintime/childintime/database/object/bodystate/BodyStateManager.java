package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class BodyStateManager extends AbstractDatabaseObjectManager {

    @Override
    public DatabaseFieldsInterface[] getDefaultFields() {
        return new BodyStateFields[]{
                BodyStateFields.LENGTH,
                BodyStateFields.WEIGHT
        };
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
    public AbstractDatabaseObjectManifest getManifest() {
        return BodyStateManifest.getInstance();
    }
}
