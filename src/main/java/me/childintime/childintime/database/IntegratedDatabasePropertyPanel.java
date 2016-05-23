package me.childintime.childintime.database;

import javax.swing.*;
import java.awt.*;

public class IntegratedDatabasePropertyPanel extends AbstractDatabasePropertyPanel {

    /**
     * File box instance.
     */
    private JTextField fileBox;

    @Override
    public void buildUi() {
        // Set the layout
        setLayout(new GridBagLayout());

        // Create the grid bag constraints
        GridBagConstraints c = new GridBagConstraints();

        // Create and add the file label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        add(new JLabel("File:"), c);

        // Create the file box
        this.fileBox = new JTextField("MY_FILE_PATH");

        // Add the file box
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 16, 0, 0);
        add(this.fileBox, c);
    }

    @Override
    public void update(AbstractDatabase database) {
        // TODO: Update the fields to mimic the database!
    }

    @Override
    public void apply(AbstractDatabase database) {
        // TODO: Apply properties!
    }
}
