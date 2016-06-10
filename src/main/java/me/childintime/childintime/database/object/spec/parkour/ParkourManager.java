package me.childintime.childintime.database.object.spec.parkour;

import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class ParkourManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return ParkourManifest.getInstance();
    }
}
