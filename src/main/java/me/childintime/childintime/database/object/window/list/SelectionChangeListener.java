package me.childintime.childintime.database.object.window.list;

import java.util.EventListener;

public interface SelectionChangeListener extends EventListener {

    /**
     * Called when the selection changed.
     */
    void onSelectionChange();
}
