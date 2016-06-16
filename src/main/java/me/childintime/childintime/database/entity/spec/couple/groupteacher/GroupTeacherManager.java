package me.childintime.childintime.database.entity.spec.couple.groupteacher;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class GroupTeacherManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return GroupTeacherManifest.getInstance();
    }
}
