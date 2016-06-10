package me.childintime.childintime.database.entity.spec.school;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class SchoolManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return SchoolManifest.getInstance();
    }
}
