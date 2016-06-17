/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

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
