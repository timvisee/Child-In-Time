package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class TeacherManager extends AbstractDatabaseObjectManager{

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return TeacherManifest.getInstance();
    }
}
