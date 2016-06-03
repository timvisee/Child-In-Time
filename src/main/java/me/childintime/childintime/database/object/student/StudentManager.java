package me.childintime.childintime.database.object.student;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class StudentManager extends AbstractDatabaseObjectManager{

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return StudentManifest.getInstance();
    }
}
