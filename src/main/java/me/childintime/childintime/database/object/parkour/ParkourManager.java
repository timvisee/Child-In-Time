package me.childintime.childintime.database.object.parkour;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class ParkourManager extends AbstractDatabaseObjectManager{

    @Override
    public DatabaseFieldsInterface[] getDefaultFields() {
        return new ParkourFields[]{
                ParkourFields.DESCRIPTION
        };
    }

    @Override
    public String getTypeName() {
        return "Parkour";
    }

    @Override
    public String getTableName() {
        return ParkourFields.DATABASE_TABLE_NAME;
    }

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return ParkourManifest.getInstance();
    }
}
