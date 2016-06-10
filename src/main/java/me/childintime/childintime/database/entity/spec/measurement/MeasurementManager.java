package me.childintime.childintime.database.entity.spec.measurement;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class MeasurementManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return MeasurementManifest.getInstance();
    }
}
