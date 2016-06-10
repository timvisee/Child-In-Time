package me.childintime.childintime.database.object.spec.measurement;

import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class MeasurementManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return MeasurementManifest.getInstance();
    }
}
