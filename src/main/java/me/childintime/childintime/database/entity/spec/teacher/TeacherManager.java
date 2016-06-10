package me.childintime.childintime.database.entity.spec.teacher;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class TeacherManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return TeacherManifest.getInstance();
    }
}
