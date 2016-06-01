package me.childintime.childintime.database.object.parkour;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;

public class ParkourManager extends AbstractDatabaseObjectManager{


    @Override
    public String getTypeName() {
        return "Parkour";
    }

    @Override
    public String getTableName() {
        return ParkourFields.DATABASE_TABLE_NAME;
    }

    @Override
    public Class<? extends AbstractDatabaseObject> getObjectClass() {
        return Parkour.class;
    }
}
