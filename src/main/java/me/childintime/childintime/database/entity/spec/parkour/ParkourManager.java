package me.childintime.childintime.database.entity.spec.parkour;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class ParkourManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return ParkourManifest.getInstance();
    }
}
