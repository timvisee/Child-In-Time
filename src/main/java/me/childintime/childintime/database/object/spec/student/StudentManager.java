package me.childintime.childintime.database.object.spec.student;

import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class StudentManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return StudentManifest.getInstance();
    }
}
