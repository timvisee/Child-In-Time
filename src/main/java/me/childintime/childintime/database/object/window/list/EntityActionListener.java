package me.childintime.childintime.database.object.window.list;

import me.childintime.childintime.database.object.AbstractDatabaseObject;

import java.util.EventListener;
import java.util.List;

public interface EntityActionListener extends EventListener {

    /**
     * Called when an action should be performed for the given entities.
     *
     * @param entities Entities.
     */
    void onEntityAction(List<AbstractDatabaseObject> entities);
}
