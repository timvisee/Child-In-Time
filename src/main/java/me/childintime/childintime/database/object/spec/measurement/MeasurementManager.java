package me.childintime.childintime.database.object.spec.measurement;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;

public class MeasurementManager extends AbstractDatabaseObjectManager {

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return MeasurementManifest.getInstance();
    }
}
