package me.childintime.childintime.database.object.spec.group;

import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.AbstractEntityManifest;

public class GroupManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return GroupManifest.getInstance();
    }
}
