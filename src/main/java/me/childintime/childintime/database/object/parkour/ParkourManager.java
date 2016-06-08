package me.childintime.childintime.database.object.parkour;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class ParkourManager extends AbstractDatabaseObjectManager{

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return ParkourManifest.getInstance();
    }
}