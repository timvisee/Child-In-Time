package me.childintime.childintime.database.entity.spec.couple.studentsport;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class StudentSportManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return StudentSportManifest.getInstance();
    }
}
