package me.childintime.childintime.database;

import javax.swing.*;

public abstract class AbstractDatabasePropertyPanel extends JPanel {

    /**
     * Apply the properties in the panel to the given database.
     *
     * @param database Database to apply the properties to.
     */
    public abstract void apply(AbstractDatabase database);
}
