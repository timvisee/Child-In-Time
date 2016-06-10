package me.childintime.childintime.database.object.spec.school;

import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class SchoolManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return SchoolManifest.getInstance();
    }
}
