package me.childintime.childintime.database.entity.spec.group;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class GroupManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return GroupManifest.getInstance();
    }
}
