package me.childintime.childintime.database.object.window;

import me.childintime.childintime.App;
import me.childintime.childintime.database.configuration.AbstractDatabase;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManifest;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;
import me.childintime.childintime.gui.component.property.AbstractPropertyField;
import me.childintime.childintime.gui.component.property.DatePropertyField;
import me.childintime.childintime.gui.component.property.TextPropertyField;
import me.childintime.childintime.util.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DatabaseObjectModifyDialog extends JDialog {

    /**
     * Frame title.
     */
    // TODO: Put object type name in here?
    private static final String FORM_TITLE = App.APP_NAME;

    /**
     * The manifest of the source object.
     * If the source object is null, the manifest must be set manually.
     */
    private AbstractDatabaseObjectManifest sourceManifest;

    /**
     * The source database object instance.
     */
    private AbstractDatabaseObject source;

    /**
     * The result object instance.
     */
    private AbstractDatabaseObject result;

    /**
     * Defines whether the database changes have been discarded.
     */
    private boolean discarded = false;

    /**
     * Database fields cache.
     */
    private DatabaseFieldsInterface[] fieldsCache = null;

    /**
     * Hashmap containing all the fields.
     */
    private HashMap<DatabaseFieldsInterface, AbstractPropertyField> fields = new HashMap<>();

    /**
     * Constructor, to modify an existing database object.
     *
     * @param owner The parent window.
     * @param source The source database object.
     * @param show True to show the frame once it has been initialized.
     */
    public DatabaseObjectModifyDialog(Window owner, AbstractDatabaseObject source, boolean show) {
        this(owner, (Object) source, show);
    }

    /**
     * Constructor, to create a new database object.
     *
     * @param owner The parent window.
     * @param manifest The database object manifest to create a new object for.
     * @param show True to show the frame once it has been initialized.
     */
    public DatabaseObjectModifyDialog(Window owner, AbstractDatabaseObjectManifest manifest, boolean show) {
        this(owner, (Object) manifest, show);
    }

    /**
     * Constructor.
     *
     * @param owner The parent window.
     * @param source The database object manifest.
     * @param show True to show the frame once it has been initialized.
     */
    private DatabaseObjectModifyDialog(Window owner, Object source, boolean show) {
        // Construct the form
        super(owner, FORM_TITLE, ModalityType.DOCUMENT_MODAL);

        // Set the source and/or the source manifest based on the input source, make sure the source isn't null or invalid
        if(source == null)
            throw new NullPointerException("The source may not be null.");

        else if(source instanceof AbstractDatabaseObject) {
            this.source = (AbstractDatabaseObject) source;
            this.sourceManifest = this.source.getManifest();

        } else if(source instanceof AbstractDatabaseObjectManifest)
            this.sourceManifest = (AbstractDatabaseObjectManifest) source;

        else
            throw new IllegalArgumentException("Invalid source type, must be an AbstractDatabaseObject or" +
                    "AbstractDatabaseObjectManifest type.");

        // Set the result object
        if(this.source != null)
            // TODO: Clone the object!
            this.result = this.source;
        else {
            // TODO: Create a new object instance, and put it into the result thing!
        }

        // Create the form UI
        buildUi();

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
        configureSize();

        // Set the window location to the system's default
        this.setLocationByPlatform(true);
        this.setLocationRelativeTo(owner);

        // Update the title
        setTitle(FORM_TITLE + " - Modify " + this.sourceManifest.getTypeName(false, false));

        // Show the form
        this.setVisible(show);
    }

    /**
     * Create a new database object, and show the modification dialog.
     * The newly created object is returned when the dialog is closed.
     * Null is returned if the creation process is cancelled.
     *
     * @param owner Owner window.
     * @param manifest The manifest of the object to create.
     *
     * @return The created database object, or null.
     */
    public static AbstractDatabaseObject showCreate(Window owner, AbstractDatabaseObjectManifest manifest) {
        // Make sure the manifest object isn't null
        if(manifest == null)
            throw new NullPointerException("The database object manifest may not be null.");

        // Show the modify dialog with a new database object and return the result
        return showModify(owner, (Object) manifest);
    }

    /**
     * Show the modification dialog for the given database object.
     * The modified database object is returned when the modification dialog is closed.
     * The source is returned if the modification process is cancelled.
     * The source may be null, in order to create a new database configuration,
     * the source manifest must be specified in that case to determine the type of database object to create.
     *
     * @param owner Owner window.
     * @param source Source database object.
     *
     * @return A database object, or null.
     */
    public static AbstractDatabaseObject showModify(Window owner, AbstractDatabaseObject source) {
        // Make sure the source object isn't null
        if(source == null)
            throw new NullPointerException("The source object may not be null.");

        // Show the modify dialog and return the result
        return showModify(owner, (Object) source);
    }

    /**
     * Show the modification dialog for the given database object.
     * The modified database object is returned when the modification dialog is closed.
     * The source is returned if the modification process is cancelled.
     * The source may be null, in order to create a new database configuration,
     * the source manifest must be specified in that case to determine the type of database object to create.
     *
     * @param owner Owner window.
     * @param source Source database object or database object manifest.
     *
     * @return A database object, or null.
     */
    private static AbstractDatabaseObject showModify(Window owner, Object source) {
        // Create a new dialog instance
        DatabaseObjectModifyDialog dialog = new DatabaseObjectModifyDialog(owner, source, true);

        // TODO: Is this correct?
        // TODO: Use getters, instead of directly accessing the fields.

        // Return the source if the changes were discarded
        if(dialog.isDiscarded())
            return dialog.source;

        // Return the new database
        return dialog.result;
    }

    /**
     * Get the database object fields list.
     *
     * @return Database objects field list.
     */
    private DatabaseFieldsInterface[] getFields() {
        // Return the cached value if available
        if(this.fieldsCache != null)
            return this.fieldsCache;

        // Get the fields from the manifest
        try {
            this.fieldsCache = this.sourceManifest.getFieldValues();

        } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            // Show an error message to the user
            JOptionPane.showMessageDialog(this, "An error occurred while modifying the database object, the application will now quit.");

            // Throw an error
            throw new Error("Failed to access database object fields list.", e);
        }

        // Return the list of fields
        return this.fieldsCache;
    }

    /**
     * Properly configure the window sizes for the current content.
     */
    private void configureSize() {
        // TODO: Configure this method for this dialog!

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

        // Construct a grid bag constraints object to specify the placement of all components
        GridBagConstraints c = new GridBagConstraints();

        // Configure the placement of the questions label, and add it to the questions panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 16, 8);
        container.add(new JLabel("Modify " + this.sourceManifest.getTypeName(false, false) + ":"), c);

        // Get the list of fields
        DatabaseFieldsInterface[] fieldTypes = getFields();

        for(int i = 0; i < fieldTypes.length; i++) {
            // Get the field type
            DatabaseFieldsInterface fieldType = fieldTypes[i];

            // Create and add the name label
            c.fill = GridBagConstraints.NONE;
            c.gridx = 0;
            c.gridy = i + 1;
            c.gridwidth = 1;
            c.weightx = 0;
            c.insets = new Insets(0, 0, 8, 8);
            c.anchor = GridBagConstraints.WEST;
            container.add(new JLabel(fieldType.getDisplayName() + ":"), c);

            String value = "";
            if(this.source != null)
                try {
                    value = this.source.getField(fieldType).toString();
                } catch(Exception e) {
                    e.printStackTrace();
                }

            // Create and add the name label
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = i + 1;
            c.gridwidth = 1;
            c.weightx = 1;
            c.insets = new Insets(0, 8, 8, 0);
            c.anchor = GridBagConstraints.CENTER;

            // Create a variable for the property field instance
            AbstractPropertyField field;

            switch(fieldType.getExtendedDataType()) {
                case DATE:
                    field = new DatePropertyField(true);
                    break;

                case STRING:
                default:
                    field = new TextPropertyField(value, false);
                    break;
            }

            // Add the field
            container.add(field, c);
        }

        // Create the control button panel and add it to the main panel
        JPanel controlsPanel = createControlButtonPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = fieldTypes.length + 1;
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
        commitPanel.setLayout(new GridLayout(1, 3, 8, 8));

        // Create an action button panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1, 1, 8, 8));

        // Create the commit buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JButton applyButton = new JButton("Apply");
        okButton.addActionListener(e -> {
            // Apply the changes to the database
            if(!applyChanges())
                return;

            // Close the frame
            dispose();
        });
        cancelButton.addActionListener(e -> closeFrame());

        // Add the commit buttons to the panel, use the proper order based on the operating system
        if(!Platform.isMacOsX()) {
            commitPanel.add(okButton);
            commitPanel.add(cancelButton);
            commitPanel.add(applyButton);
        } else {
            commitPanel.add(applyButton);
            commitPanel.add(cancelButton);
            commitPanel.add(okButton);
        }

        // Create and add the test button
        JButton defaultsButton = new JButton("Defaults");
        defaultsButton.addActionListener(e -> {
            // Feature not yet implemented, show a dialog box
            featureNotImplemented();

//            // Create a progress dialog
//            ProgressDialog progressDialog = new ProgressDialog(this, "Testing database...", false, "Applying changes...", true);
//
//            // Apply the changes
//            applyChanges();
//
//            // TODO: Fix this!
//            // Get the database
//            AbstractDatabase database = getDatabase();
//
//            // Make sure the configuration is valid
//            progressDialog.setStatus("Validating configuration...");
//            if(!database.isConfigured()) {
//                // Show the option pane
//                JOptionPane.showMessageDialog(progressDialog, "The database configuration is missing some required properties.", "Database configuration incomplete", JOptionPane.ERROR_MESSAGE);
//
//                // Dispose the progress dialog and return
//                progressDialog.dispose();
//                return;
//            }
//
//            // Dispose the progress dialog
//            progressDialog.dispose();
//
//            // Test the database connection
//            if(database.test(this, progressDialog)) {
//                // Show a success message
//                JOptionPane.showMessageDialog(
//                        this,
//                        "Successfully connected to the database..",
//                        "Success",
//                        JOptionPane.INFORMATION_MESSAGE
//                );
//            }
        });
        actionPanel.add(defaultsButton);

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
     * Get the source database object.
     *
     * @return Source database object.
     */
    public AbstractDatabaseObject getSource() {
        return this.source;
    }

    /**
     * Get the result database object.
     *
     * @return Result database object.
     */
    public AbstractDatabaseObject getResult() {
        return this.result;
    }

    /**
     * Set the database that is shown.
     *
     * @param database Database.
     */
    public void updateComponents(AbstractDatabase database) {
//        // Update the used database type
//        this.currentType = database.getType();
//
//        // Set the question label
//        this.databaseNameField.setText(database.getName());
//
//        // Select the proper database type
//        this.databaseTypeBox.setSelectedItem(database.getType());
//
//        // TODO: Update the properties window
//
//        // Pack the frame
//        //pack();
//
//        // Force the whole frame to repaint, to prevent graphical artifacts on some operating systems
//        this.repaint();
    }

    /**
     * Update the database property panel.
     */
    public void updatePropertyPanel() {
//        // Apply the property panel values
//        applyPropertyPanel();
//
//        // Get the selected database type
//        DatabaseType selectedType = (DatabaseType) this.databaseTypeBox.getSelectedItem();
//
//        // Determine the panel class
//        Class<? extends AbstractDatabasePropertyPanel> propertyPanelClass = selectedType.getPropertyPanelClass();
//
//        // Reset the property panel if a different property panel should be shown
//        if(!propertyPanelClass.isInstance(this.propertyPanel)) {
//            // Instantiate the property panel for the new database type
//            try {
//                this.propertyPanel = selectedType.getPropertyPanelClass().newInstance();
//
//            } catch (InstantiationException | IllegalAccessException ex) {
//                ex.printStackTrace();
//                // TODO: Show an error message
//            }
//
//            // Build and update the property panel
//            this.propertyPanel.buildUi();
//            this.propertyPanel.update(getDatabase());
//
//            // Remove all current panels in the wrapper and add the new property panel
//            this.propertyPanelWrapper.removeAll();
//            this.propertyPanelWrapper.add(this.propertyPanel);
//
//            // Reconfigure the frame sizes
//            configureSize();
//
//        } else
//            // Update the property panel
//            this.propertyPanel.update(getDatabase());
    }

    /**
     * Apply the changes to the current database.
     *
     * @return True if the changes were valid, false if not.
     */
    public boolean applyChanges() {
//        // Make sure the database isn't null
//        if(this.getDatabase() == null)
//            // TODO: Create database instance to apply to?
//            return false;
//
//        // Apply the name
//        this.getDatabase().setName(this.databaseNameField.getText());
//
//        // Make sure the database type corresponds to the current type
//        // TODO: Should we 'apply' it instead of returning false
//        if(!this.currentType.equals(this.databaseTypeBox.getSelectedItem()))
//            return false;
//
//        // Apply the property panel values
//        applyPropertyPanel();

        // Save succeed, return the result
        return true;
    }

    /**
     * Apply the property panel values.
     */
    public void applyPropertyPanel() {
//        // Apply the properties to the database
//        if(this.propertyPanel != null)
//            this.propertyPanel.apply(getDatabase());
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
//        // Get the current database
//        AbstractDatabase database = getDatabase();
//
//        // Apply the changes
//        applyChanges();
//
//        // Compare the source database with the current database
//        if(this.source == null || database == null) {
//            // Check whether they are equal
//            if(this.source != database)
//                return true;
//
//        } else if(!this.source.equals(database))
//            return true;

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

    // TODO: This should be removed!
    @Deprecated
    public void featureNotImplemented() {
        JOptionPane.showMessageDialog(this, "Feature not implemented yet!", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
    }
}
