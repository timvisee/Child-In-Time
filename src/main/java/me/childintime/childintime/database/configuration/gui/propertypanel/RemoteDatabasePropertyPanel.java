package me.childintime.childintime.database.configuration.gui.propertypanel;

import me.childintime.childintime.database.DatabaseType;
import me.childintime.childintime.database.configuration.AbstractDatabase;
import me.childintime.childintime.database.configuration.RemoteDatabase;
import me.childintime.childintime.gui.component.property.PasswordPropertyField;
import me.childintime.childintime.gui.component.property.TextPropertyField;

import javax.swing.*;
import java.awt.*;

public class RemoteDatabasePropertyPanel extends AbstractDatabasePropertyPanel {

    /**
     * Host field.
     */
    private TextPropertyField hostField;

    /**
     * Port field.
     */
    private JSpinner portField;

    /**
     * User field.
     */
    private TextPropertyField userField;

    /**
     * Password field.
     */
    private PasswordPropertyField passwordField;

    @Override
    public void buildUi() {
        // Set the layout
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

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
        this.hostField = new TextPropertyField("MY_HOST", true);
        this.hostField.setEmptyAllowed(false);

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

        // Hide the comma in the port field
        this.portField.setEditor(new JSpinner.NumberEditor(this.portField, "#"));

        // Align the port field text to the left
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) this.portField.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);

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
        this.userField = new TextPropertyField("USER", true);

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

        // Create the password field
        this.passwordField = new PasswordPropertyField("PASSWORD", true);

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
        // Make sure the object isn't null
        if(database == null)
            return;

        // Make sure we're using the same type
        if(!database.getType().equals(getDatabaseType()))
            return;

        // Cast the database instance
        RemoteDatabase remote = (RemoteDatabase) database;

        // Update the fields
        this.hostField.setText(remote.getHost());
        this.portField.setValue(remote.getPort());
        this.userField.setText(remote.getUser());
        this.passwordField.setText(remote.getPassword());
    }

    @Override
    public boolean apply(AbstractDatabase database) {
        // Make sure we're working with the correct kind of database
        if(!(database instanceof RemoteDatabase))
            return false;

        // Get the proper instance
        RemoteDatabase remote = (RemoteDatabase) database;

        // Apply properties
        remote.setHost(this.hostField.getText());
        remote.setPort((Integer) this.portField.getValue());
        remote.setUser(this.userField.getText());
        remote.setPassword(this.passwordField.getText());

        // Return the result
        return true;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.REMOTE;
    }
}
