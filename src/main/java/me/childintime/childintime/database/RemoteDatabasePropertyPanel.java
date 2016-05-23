package me.childintime.childintime.database;

import javax.swing.*;

public class RemoteDatabasePropertyPanel extends AbstractDatabasePropertyPanel {

    @Override
    public void buildUi() {
        add(new JTextField("A"));
        add(new JTextField("B"));
        add(new JTextField("C"));
        add(new JTextField("D"));
    }

    @Override
    public void apply(AbstractDatabase database) {
        // TODO: Apply properties!
    }
}
