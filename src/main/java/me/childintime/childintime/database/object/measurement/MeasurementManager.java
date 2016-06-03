package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class MeasurementManager extends AbstractDatabaseObjectManager {

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return MeasurementManifest.getInstance();
    }
}
