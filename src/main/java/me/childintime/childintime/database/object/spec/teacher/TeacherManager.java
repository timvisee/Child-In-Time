package me.childintime.childintime.database.object.spec.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;

public class TeacherManager extends AbstractDatabaseObjectManager{

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return TeacherManifest.getInstance();
    }
}
