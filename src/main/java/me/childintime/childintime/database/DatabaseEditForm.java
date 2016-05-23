package me.childintime.childintime.database;

import me.childintime.childintime.App;
import me.childintime.childintime.util.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

public class DatabaseEditForm extends JDialog {

    /**
     * Frame title.
     */
    private static final String FORM_TITLE = App.APP_NAME + " - Edit database";

    /**
     * The source database instance, which is the source.
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
    public DatabaseEditForm(Window owner, AbstractDatabase source, boolean show) {
        // Construct the form
        super(owner, FORM_TITLE, ModalityType.DOCUMENT_MODAL);

        // Set the source
        this.source = source;

        // Set the current database instance and it's type if the source isn't null
        if(source != null) {
            this.currentType = source.getType();
            this.databases[this.currentType.getIndex()] = source.clone();

        } else {
            // Create a new integrated database instance, and set it's type
            this.currentType = DatabaseType.INTEGRATED;
            this.databases[this.currentType.getIndex()] = new IntegratedDatabase();
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

        // Configure the window size
        // FIXME: Already done?
        //configureSize();

        // Set the window location to the system's default
        this.setLocationByPlatform(true);
        this.setLocationRelativeTo(owner);

        // Show the form
        this.setVisible(show);
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
                    ex.printStackTrace();
                    // TODO: Show an error message
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
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(8, 0, 0, 0);
        c.anchor = GridBagConstraints.EAST;
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
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 8, 8));

        // Create the buttons to add to the panel
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

        // Add the buttons to the panel
        if(!Platform.isMacOsX()) {
            buttonPanel.add(saveButton);
            buttonPanel.add(closeButton);
        } else {
            buttonPanel.add(closeButton);
            buttonPanel.add(saveButton);
        }

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

        // TODO: Update the properties window

        // Pack the frame
        //pack();

        // Force the whole frame to repaint, to prevent graphical artifacts on some operating systems
        this.repaint();
    }

    /**
     * Apply the changes to the current database.
     *
     * @return True if the changes were valid, false if not.
     */
    public boolean applyChanges() {
        // Make sure the database isn't null
        if(this.getDatabase() == null)
            // TODO: Create database instance to apply to?
            return false;

        // Apply the name
        this.getDatabase().setName(this.databaseNameField.getText());

        // Make sure the database type corresponds to the current type
        // TODO: Should we 'apply' it instead of returning false
        if(!this.currentType.equals(this.databaseTypeBox.getSelectedItem()))
            return false;

        // Apply the property panel values
        applyPropertyPanel();

        // Save succeed, return the result
        return true;
    }

    /**
     * Close the frame. Ask whether the user wants to save the changes.
     */
    public void closeFrame() {
        // TODO: Stop the closing process, when we fail!

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



    // TODO: Properly configure these methods!

    public static AbstractDatabase createNew(Window owner) {
        return use(owner, null);
    }

    public static AbstractDatabase use(Window owner, AbstractDatabase source) {
        // Create a dialog instance and show it
        DatabaseEditForm dialog = new DatabaseEditForm(owner, source, true);

        // Return the source if the changes were discarded
        if(dialog.isDiscarded())
            return source;

        // Return the new database
        return dialog.getDatabase();
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
            // Instantiate the property panel for the new database type
            try {
                this.propertyPanel = selectedType.getPropertyPanelClass().newInstance();

            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
                // TODO: Show an error message
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
     * Apply the property panel values.
     */
    public void applyPropertyPanel() {
        // Apply the properties to the database
        if(this.propertyPanel != null)
            this.propertyPanel.apply(getDatabase());
    }
}
