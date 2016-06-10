package me.childintime.childintime.database.object.listener;

import java.util.EventListener;

public interface SelectionChangeListener extends EventListener {

    /**
     * Called when the selection changed.
     */
    void onSelectionChange();
}
