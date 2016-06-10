package me.childintime.childintime.database.entity.listener;

import java.util.EventListener;

public interface SelectionChangeListener extends EventListener {

    /**
     * Called when the selection changed.
     */
    void onSelectionChange();
}
