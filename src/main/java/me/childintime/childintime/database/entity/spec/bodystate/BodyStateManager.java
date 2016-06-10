package me.childintime.childintime.database.entity.spec.bodystate;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class BodyStateManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return BodyStateManifest.getInstance();
    }
}
