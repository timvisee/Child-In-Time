package me.childintime.childintime.database.entity.spec.student;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class StudentManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return StudentManifest.getInstance();
    }
}
