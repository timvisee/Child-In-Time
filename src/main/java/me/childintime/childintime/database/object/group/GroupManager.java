package me.childintime.childintime.database.object.group;

import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

public class GroupManager extends AbstractDatabaseObjectManager {

    @Override
    public AbstractDatabaseObjectManifest getManifest() {
        return GroupManifest.getInstance();
    }
}
