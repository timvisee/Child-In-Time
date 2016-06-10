package me.childintime.childintime.database.entity.listener;

import java.util.EventListener;

public interface ChangeListener extends EventListener {

    /**
     * Called when the data of a entity manager is changed.
     * This is usually after an object has been created, modified, or when the data is refreshed.
     */
    void onChange();
}
