package me.childintime.childintime.database.entity.spec.sport;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class SportManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return SportManifest.getInstance();
    }
}
