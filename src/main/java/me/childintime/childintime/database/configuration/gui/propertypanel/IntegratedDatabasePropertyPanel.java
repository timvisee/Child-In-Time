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
import me.childintime.childintime.database.configuration.IntegratedDatabase;
import me.childintime.childintime.ui.component.property.FilePropertyField;

import javax.swing.*;
import java.awt.*;

public class IntegratedDatabasePropertyPanel extends AbstractDatabasePropertyPanel {

    /**
     * File box instance.
     */
    private FilePropertyField fileField;

    @Override
    public void buildUi() {
        // Set the layout
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Create the grid bag constraints
        GridBagConstraints c = new GridBagConstraints();

        // Create and add the file label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("File:"), c);

        // Create the file field
        this.fileField = new FilePropertyField((String) null, true);
        this.fileField.setEmptyAllowed(false);

        // Add the file box
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 16, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        add(this.fileField, c);
    }

    @Override
    public void update(AbstractDatabase database) {
        // Make sure the object isn't null
        if(database == null)
            return;

        // Make sure we're using the same type
        if(!database.getType().equals(getDatabaseType()))
            return;

        // Cast the database instance
        IntegratedDatabase integrated = (IntegratedDatabase) database;

        // Update the fields
        if(integrated.hasFile())
            this.fileField.setText(integrated.getFile().toString());
        else
            this.fileField.setText(null);
    }

    @Override
    public boolean apply(AbstractDatabase database) {
        // Make sure we're working with the correct kind of database
        if(!(database instanceof IntegratedDatabase))
            return false;

        // Get the proper instance
        IntegratedDatabase integrated = (IntegratedDatabase) database;

        // Apply the file
        integrated.setFile(this.fileField.getFile());

        // Return the result
        return true;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.INTEGRATED;
    }
}
