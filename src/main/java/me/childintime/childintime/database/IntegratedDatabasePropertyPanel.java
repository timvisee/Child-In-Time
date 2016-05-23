package me.childintime.childintime.database;

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
        this.fileField = new FilePropertyField("MY_FILE_PATH", true);
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
