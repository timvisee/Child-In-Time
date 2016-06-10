package me.childintime.childintime.database.object.spec.bodystate;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;

public class BodyStateManager extends AbstractDatabaseObjectManager {

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return BodyStateManifest.getInstance();
    }
}
