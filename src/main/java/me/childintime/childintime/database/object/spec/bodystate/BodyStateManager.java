package me.childintime.childintime.database.object.spec.bodystate;

import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class BodyStateManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return BodyStateManifest.getInstance();
    }
}
