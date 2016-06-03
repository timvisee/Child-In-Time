package me.childintime.childintime.database.object.school;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class SchoolManager extends AbstractDatabaseObjectManager{

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return SchoolManifest.getInstance();
    }
}
