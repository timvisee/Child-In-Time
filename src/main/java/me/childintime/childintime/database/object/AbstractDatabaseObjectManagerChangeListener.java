package me.childintime.childintime.database.object;

import java.util.EventListener;

public interface AbstractDatabaseObjectManagerChangeListener extends EventListener {

    /**
     * Called when the data of a database object manager is changed.
     * This is usually after an object has been created, modified, or when the data is refreshed.
     */
    void onChange();
}
