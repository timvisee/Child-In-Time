package me.childintime.childintime.database;

import javax.swing.*;
import java.awt.*;

public class RemoteDatabasePropertyPanel extends AbstractDatabasePropertyPanel {

    /**
     * Host field.
     */
    private JTextField hostField;

    /**
     * Port field.
     */
    private JSpinner portField;

    /**
     * User field.
     */
    private JTextField userField;

    /**
     * Password field.
     */
    private JTextField passwordField;

    @Override
    public void buildUi() {
        // Set the layout
        setLayout(new GridBagLayout());

        // Create the grid bag constraints
        GridBagConstraints c = new GridBagConstraints();

        // Create and add the host label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Host:"), c);

        // Create the host field
        this.hostField = new JTextField("MY_HOST");

        // Add the host field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 16, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        add(this.hostField, c);

        // Create and add the port label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(8, 0, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Port:"), c);

        // Create the user field
        this.portField = new JSpinner(new SpinnerNumberModel(3306, 1, 65535, 1));

        // Add the port field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.insets = new Insets(8, 16, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        add(this.portField, c);

        // Create and add the user label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(8, 0, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("User:"), c);

        // Create the user field
        this.userField = new JTextField("USER");

        // Add the user field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 1;
        c.insets = new Insets(8, 16, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        add(this.userField, c);

        // Create and add the password label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(8, 0, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Password:"), c);

        // Create the user field
        this.passwordField = new JTextField("PASSWORD");

        // Add the user field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1;
        c.insets = new Insets(8, 16, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        add(this.passwordField, c);
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
