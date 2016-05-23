package me.childintime.childintime.database;

import javax.swing.*;

public class IntegratedDatabasePropertyPanel extends AbstractDatabasePropertyPanel {

    @Override
    public void buildUi() {
        add(new JTextField("TextField"));
        add(new JButton("..."));
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
