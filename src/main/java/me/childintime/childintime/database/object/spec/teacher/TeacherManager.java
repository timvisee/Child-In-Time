package me.childintime.childintime.database.object.spec.teacher;

import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class TeacherManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return TeacherManifest.getInstance();
    }
}
