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

package me.childintime.childintime.database.configuration.gui.window;

import me.childintime.childintime.App;
import me.childintime.childintime.database.DatabaseType;
import me.childintime.childintime.database.configuration.AbstractDatabase;
import me.childintime.childintime.database.configuration.IntegratedDatabase;
import me.childintime.childintime.database.configuration.gui.propertypanel.AbstractDatabasePropertyPanel;
import me.childintime.childintime.util.Platform;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

public class DatabaseModifyDialog extends JDialog {

    /**
     * Frame title.
     */
    private static final String FORM_TITLE = App.APP_NAME + " - Edit database configuration";

    /**
     * The source database instance.
     */
    private AbstractDatabase source;

    /**
     * The database type that is currently used.
     */
    private DatabaseType currentType;

    /**
     * .An array with all the different kinds of databases
     */
    private AbstractDatabase[] databases = new AbstractDatabase[DatabaseType.values().length];

    /**
     * The database name field.
     */
    private JTextField databaseNameField;

    /**
     * The database type box.
     */
    private JComboBox<DatabaseType> databaseTypeBox;

    /**
     * Database property panel wrapper.
     */
    private JPanel propertyPanelWrapper;

    /**
     * Database property panel.
     */
    private AbstractDatabasePropertyPanel propertyPanel;

    /**
     * Defines whether the database changes have been discarded.
     */
    private boolean discarded = false;

    /**
     * Constructor.
     *
     * @param owner The parent window.
     * @param source The source.
     * @param show True to show the frame once it has been initialized.
     */
    public DatabaseModifyDialog(Window owner, AbstractDatabase source, boolean show) {
        // Construct the form
        super(owner, FORM_TITLE, ModalityType.DOCUMENT_MODAL);

        // Set the source
        this.source = source;

        // Set the current database instance and it's type if the source isn't null
        if(source != null) {
            this.currentType = source.getType();
            this.databases[this.currentType.getIndex()] = source.clone();

        } else {
            // Set the database type
            this.currentType = DatabaseType.INTEGRATED;

            // Create an integrated database
            IntegratedDatabase newDatabase = new IntegratedDatabase();
            newDatabase.setName("");

            // Set the database
            this.databases[this.currentType.getIndex()] = newDatabase;
        }

        // Create the form UI
        buildUi();

        // Update the property panel
        updatePropertyPanel();

        // Set the database
        updateComponents(this.databases[this.currentType.getIndex()]);

        // Make the frame resizable
        this.setResizable(true);

        // Do not close the window when pressing the red cross, execute the close method instead
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }

            @Override
            public void windowClosed(WindowEvent e) { }

            @Override
            public void windowIconified(WindowEvent e) { }

            @Override
            public void windowDeiconified(WindowEvent e) { }

            @Override
            public void windowActivated(WindowEvent e) { }

            @Override
            public void windowDeactivated(WindowEvent e) { }
        });

        // Set the window location to the system's default
        this.setLocationByPlatform(true);
        this.setLocationRelativeTo(owner);

        // Show the form
        this.setVisible(show);
    }

    /**
     * Create a new database configuration, and show the modification dialog.
     * The created database is returned.
     * Null is returned if the creation process is cancelled.
     *
     * @param owner Owner window.
     *
     * @return The created database configuration, or null.
     */
    public static AbstractDatabase showCreate(Window owner) {
        return showModify(owner, null);
    }

    /**
     * Show the modification dialog for the given database configuration.
     * The modified database configuration is returned.
     * The source is returned if the modification process is cancelled.
     * The source may be null, in order to create a new database configuration.
     *
     * @param owner Owner window.
     * @param source Source database configuration.
     *
     * @return A database configuration, or null.
     */
    public static AbstractDatabase showModify(Window owner, AbstractDatabase source) {
        // Create a dialog instance and show it
        DatabaseModifyDialog dialog = new DatabaseModifyDialog(owner, source, true);

        // Return the source if the changes were discarded
        if(dialog.isDiscarded())
            return source;

        // Return the new database
        return dialog.getDatabase();
    }

    /**
     * Properly configure the window sizes for the current content.
     */
    private void configureSize() {
        // Reset the sizes
        setMinimumSize(null);
        setPreferredSize(null);
        setMaximumSize(null);

        // Store the frame width
        int width = getSize().getSize().width;

        // Pack everything
        pack();

        // Get the maximum height
        final int maxHeight = getSize().height;

        // Determine the width to use, if the minimum size of the frame is wider, use the new width
        width = Math.max(getSize().width + 100, width);

        // Configure the sizes
        setMinimumSize(new Dimension(getMinimumSize()));
        setMaximumSize(new Dimension(9999, maxHeight));
        setPreferredSize(new Dimension(width, maxHeight));
        setSize(new Dimension(width, maxHeight));
    }

    /**
     * Create all UI components for the frame.
     */
    private void buildUi() {
        // Set the frame layout
        this.setLayout(new BorderLayout());

        // Create the main panel, to put the question and answers in
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Create the property panel wrapper
        this.propertyPanelWrapper = new JPanel();
        this.propertyPanelWrapper.setLayout(new BorderLayout());
        this.propertyPanelWrapper.setBorder(BorderFactory.createTitledBorder("Properties"));

        // Construct a grid bag constraints object to specify the placement of all components
        GridBagConstraints c = new GridBagConstraints();

        // Configure the placement of the questions label, and add it to the questions panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 16, 8);
        container.add(new JLabel("Edit database."), c);

        // Create and add the name label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        container.add(new JLabel("Name:"), c);

        // Create and add the type label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 16, 8);
        c.anchor = GridBagConstraints.WEST;
        container.add(new JLabel("Type:"), c);

        // Create the database name field
        this.databaseNameField = new JTextField("Database");

        // Add the database name field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 8, 8);
        c.weightx = 1;
        container.add(this.databaseNameField, c);

        // Create the database type field
        this.databaseTypeBox = new JComboBox<>(DatabaseType.values());
        this.databaseTypeBox.addActionListener(e -> {
            // Apply the properties
            applyPropertyPanel();

            // Get the new selected database type
            DatabaseType selectedType = (DatabaseType) this.databaseTypeBox.getSelectedItem();

            // Update the current database type
            this.currentType = selectedType;

            // Create a new database type if it doesn't exist
            if(this.databases[this.currentType.getIndex()] == null)
                try {
                    // Create a new instance of the class for the selected database type, clone the source database as far as that's possible
                    this.databases[selectedType.getIndex()] = selectedType.getTypeClass().getDeclaredConstructor(AbstractDatabase.class).newInstance(this.source);

                } catch(InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    // Print the stack trace
                    ex.printStackTrace();
                }

            // Update the database property panel
            updatePropertyPanel();
        });

        // Add the database name field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 16, 8);
        c.weightx = 1;
        container.add(this.databaseTypeBox, c);

        // Configure the answers panel placement and add it to the main panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 0);
        container.add(this.propertyPanelWrapper, c);

        // Create the control button panel and add it to the main panel
        JPanel controlsPanel = createControlButtonPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(8, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        container.add(controlsPanel, c);

        // Add the container to the frame
        this.add(container);
    }

    /**
     * Create the button panel to control the form.
     *
     * @return Button panel.
     */
    public JPanel createControlButtonPanel() {
        // Create a grid bag constraints instance
        GridBagConstraints c = new GridBagConstraints();

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        // Create a commit button panel
        JPanel commitPanel = new JPanel();
        commitPanel.setLayout(new GridLayout(1, 2, 8, 8));

        // Create an action button panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1, 1, 8, 8));

        // Create the commit buttons
        JButton saveButton = new JButton("Save");
        JButton closeButton = new JButton("Close");
        saveButton.addActionListener(e -> {
            // Apply the changes to the database
            if(!applyChanges())
                return;

            // Close the frame
            dispose();
        });
        closeButton.addActionListener(e -> closeFrame());

        // Add the commit buttons to the panel, use the proper order based on the operating system
        if(!Platform.isMacOsX()) {
            commitPanel.add(saveButton);
            commitPanel.add(closeButton);
        } else {
            commitPanel.add(closeButton);
            commitPanel.add(saveButton);
        }

        // Create and add the test button
        JButton testButton = new JButton("Test");
        testButton.addActionListener(e -> {
            // Create a progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this, "Testing database...", false, "Applying changes...", true);

            // Apply the changes
            applyChanges();

            // Get the database
            AbstractDatabase database = getDatabase();

            // Make sure the configuration is valid
            progressDialog.setStatus("Validating configuration...");
            if(!database.isConfigured()) {
                // Show the option pane
                JOptionPane.showMessageDialog(progressDialog, "The database configuration is missing some required properties.", "Database configuration incomplete", JOptionPane.ERROR_MESSAGE);

                // Dispose the progress dialog and return
                progressDialog.dispose();
                return;
            }

            // Dispose the progress dialog
            progressDialog.dispose();

            // Test the database connection
            if(database.test(this, progressDialog)) {
                // Show a success message
                JOptionPane.showMessageDialog(
                        this,
                        "Successfully connected to the database..",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        actionPanel.add(testButton);

        // Add the button panels
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        buttonPanel.add(actionPanel, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 32, 0, 0);
        buttonPanel.add(commitPanel, c);

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Get the source database.
     *
     * @return Source database.
     */
    public AbstractDatabase getSource() {
        return this.source;
    }

    /**
     * Get the current database.
     *
     * @return Database.
     */
    public AbstractDatabase getDatabase() {
        return this.databases[currentType.getIndex()];
    }

    /**
     * Set the database that is shown.
     *
     * @param database Database.
     */
    public void updateComponents(AbstractDatabase database) {
        // Update the used database type
        this.currentType = database.getType();

        // Set the question label
        this.databaseNameField.setText(database.getName());

        // Select the proper database type
        this.databaseTypeBox.setSelectedItem(database.getType());

        // Force the whole frame to repaint, to prevent graphical artifacts on some operating systems
        this.repaint();
    }

    /**
     * Update the database property panel.
     */
    public void updatePropertyPanel() {
        // Apply the property panel values
        applyPropertyPanel();

        // Get the selected database type
        DatabaseType selectedType = (DatabaseType) this.databaseTypeBox.getSelectedItem();

        // Determine the panel class
        Class<? extends AbstractDatabasePropertyPanel> propertyPanelClass = selectedType.getPropertyPanelClass();

        // Reset the property panel if a different property panel should be shown
        if(!propertyPanelClass.isInstance(this.propertyPanel)) {
            try {
                // Instantiate the property panel for the new database type
                this.propertyPanel = selectedType.getPropertyPanelClass().newInstance();

            } catch (InstantiationException | IllegalAccessException ex) {
                // Show a stack trace
                ex.printStackTrace();
            }

            // Build and update the property panel
            this.propertyPanel.buildUi();
            this.propertyPanel.update(getDatabase());

            // Remove all current panels in the wrapper and add the new property panel
            this.propertyPanelWrapper.removeAll();
            this.propertyPanelWrapper.add(this.propertyPanel);

            // Reconfigure the frame sizes
            configureSize();

        } else
            // Update the property panel
            this.propertyPanel.update(getDatabase());
    }

    /**
     * Apply the changes to the current database.
     *
     * @return True if the changes were valid, false if not.
     */
    public boolean applyChanges() {
        // Make sure the database isn't null
        if(this.getDatabase() == null)
            return false;

        // Apply the name
        this.getDatabase().setName(this.databaseNameField.getText());

        // Make sure the database type corresponds to the current type
        if(!this.currentType.equals(this.databaseTypeBox.getSelectedItem()))
            return false;

        // Apply the property panel values
        applyPropertyPanel();

        // Save succeed, return the result
        return true;
    }

    /**
     * Apply the property panel values.
     */
    public void applyPropertyPanel() {
        // Apply the properties to the database
        if(this.propertyPanel != null)
            this.propertyPanel.apply(getDatabase());
    }

    /**
     * Close the frame. Ask whether the user wants to save the changes.
     */
    public void closeFrame() {
        // Only ask to save if there are any unsaved changes
        if(hasUnsavedChanges()) {
            // Ask whether the user wants to save the questions
            switch(JOptionPane.showConfirmDialog(this, "Would you like to save the changes?", "Database changed", JOptionPane.YES_NO_CANCEL_OPTION)) {
                case JOptionPane.CANCEL_OPTION:
                    // Whoops, we don't want to close the frame
                    return;

                case JOptionPane.NO_OPTION:
                    // Set the discarded flag
                    this.discarded = true;
                    break;
            }

            // Dispose the frame
            this.dispose();

        } else {
            // Set the discarded flag
            this.discarded = true;

            // Dispose the frame
            this.dispose();
        }
    }

    /**
     * Check whether this database has unsaved changes.
     *
     * @return True if this database has unsaved changes, false if not.
     */
    public boolean hasUnsavedChanges() {
        // Get the current database
        AbstractDatabase database = getDatabase();

        // Apply the changes
        applyChanges();

        // Compare the source database with the current database
        if(this.source == null || database == null) {
            // Check whether they are equal
            if(this.source != database)
                return true;

        } else if(!this.source.equals(database))
            return true;

        // No differences, return false
        return false;
    }

    /**
     * Check whether the changes have been discarded.
     *
     * @return True if the changes have been discarded.
     */
    public boolean isDiscarded() {
        return this.discarded;
    }
}
