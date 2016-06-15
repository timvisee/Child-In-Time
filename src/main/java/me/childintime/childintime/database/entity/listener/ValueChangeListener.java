package me.childintime.childintime.database.entity.listener;

import java.util.EventListener;

public interface ValueChangeListener extends EventListener {

    /**
     * Called when the value changed.
     *
     * @param newValue New value.
     */
    void onValueChange(Object newValue);
}
