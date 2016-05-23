package me.childintime.childintime.database;

import javax.swing.*;
import java.awt.*;

public class RemoteDatabasePropertyPanel extends AbstractDatabasePropertyPanel {

    @Override
    public void buildUi() {
        setLayout(new GridLayout(4, 1, 8, 8));

        add(new JTextField("A"));
        add(new JTextField("B"));
        add(new JTextField("C"));
        add(new JTextField("D"));
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
