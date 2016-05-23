package me.childintime.childintime.database.configuration.gui.propertypanel;

import me.childintime.childintime.database.DatabaseType;
import me.childintime.childintime.database.configuration.AbstractDatabase;

import javax.swing.*;

public abstract class AbstractDatabasePropertyPanel extends JPanel {

    /**
     * Build the property panel UI.
     */
    public abstract void buildUi();

    public abstract void update(AbstractDatabase database);

    /**
     * Apply the properties in the panel to the given database.
     *
     * @param database Database to apply the properties to.
     *
     * @return True if anything was applied, false if not.
     * False will be returned if the given database instance isn't an instance of the database the current property panel is for.
     */
    public abstract boolean apply(AbstractDatabase database);

    /**
     * Get the database type.
     *
     * @return Database type.
     */
    public abstract DatabaseType getDatabaseType();
}
