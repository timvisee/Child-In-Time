package me.childintime.childintime.database.entity.spec.user;

import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class UserManager extends AbstractEntityManager {

    @Override
    public AbstractEntityManifest getManifest() {
        return UserManifest.getInstance();
    }
}
