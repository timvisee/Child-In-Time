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

package me.childintime.childintime.ui.window;

import me.childintime.childintime.App;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {

    /**
     * Constructor.
     *
     * @param owner Owner window.
     * @param show True to immediately show the dialog.
     */
    public AboutDialog(Window owner, boolean show) {
        // Construct the super
        super(owner, App.APP_NAME);

        // Build the UI
        buildUi();

        // Pack the frame
        pack();

        // Make the dialog not resizable
        setResizable(false);

        // Set the location relative to the owner
        setLocationRelativeTo(owner);

        // Show
        if(show)
            setVisible(true);
    }

    /**
     * Build the UI.
     */
    private void buildUi() {
        // Create a new grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Set the layout
        setLayout(new BorderLayout());

        // Create a container
        final JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Create the main label
        final JLabel mainLabel = new JLabel("<html>" +
                "<b>" + App.APP_NAME + "</b><br>" +
                "Version: " + App.APP_VERSION_NAME + " (" + App.APP_VERSION_CODE + ")<br>" +
                "<br>" +
                "Developed by:<br>" +
                "<ul>" +
                "<li>Tim Vis&eacute;e, http://timvisee.com/</li>" +
                "<li>Nathan Bakhuijzen</li>" +
                "<li>Timo van den Boom</li>" +
                "<li>Jos van Gent</li>" +
                "</ul>" +
                "<br>" +
                "Copyright &copy; Child-In-Time 2016. All rights reserved."
        );

        // Add the main label
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 50);
        container.add(mainLabel, c);

        // Create and add a close button
        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(8, 0, 0, 0);
        container.add(closeButton, c);

        // Add the container to the frame
        add(container, BorderLayout.CENTER);
    }
}
