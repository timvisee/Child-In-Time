package me.childintime.childintime.database.entity.ui.dialog;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityCoupleManifest;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityFieldsInterface;
import me.childintime.childintime.database.entity.datatype.DataTypeExtended;
import me.childintime.childintime.database.entity.ui.component.EntitySmallManagerComponent;
import me.childintime.childintime.hash.HashUtil;
import me.childintime.childintime.permission.PermissionLevel;
import me.childintime.childintime.ui.component.LinkLabel;
import me.childintime.childintime.ui.component.property.*;
import me.childintime.childintime.util.Platform;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

public class EntityModifyDialog extends JDialog {

    /**
     * Frame title.
     */
    private static final String FORM_TITLE = App.APP_NAME;

    /**
     * The manifest of the source object.
     * If the source object is null, the manifest must be set manually.
     */
    private AbstractEntityManifest sourceManifest;

    /**
     * The source entity instance.
     */
    private AbstractEntity source;

    /**
     * The result object instance.
     */
    private AbstractEntity result;

    /**
     * Defines whether the database changes have been discarded.
     */
    private boolean discarded = false;

    /**
     * Database fields cache.
     */
    private EntityFieldsInterface[] fieldsCache = null;

    /**
     * Hashmap containing all the fields.
     */
    private HashMap<EntityFieldsInterface, AbstractPropertyField> fields = new HashMap<>();

    /**
     * Couple panels.
     */
    private List<JPanel> couplePanels = new ArrayList<>();

    /**
     * Constructor, to modify an existing entity.
     *
     * @param owner The parent window.
     * @param source The source entity.
     * @param show True to show the frame once it has been initialized.
     */
    public EntityModifyDialog(Window owner, AbstractEntity source, boolean show) {
        this(owner, (Object) source, show);
    }

    /**
     * Constructor, to create a new entity.
     *
     * @param owner The parent window.
     * @param manifest The entity manifest to create a new object for.
     * @param show True to show the frame once it has been initialized.
     */
    public EntityModifyDialog(Window owner, AbstractEntityManifest manifest, boolean show) {
        this(owner, (Object) manifest, show);
    }

    /**
     * Constructor.
     *
     * @param owner The parent window.
     * @param source The entity manifest.
     * @param show True to show the frame once it has been initialized.
     */
    private EntityModifyDialog(Window owner, Object source, boolean show) {
        // Construct the form
        super(owner, FORM_TITLE, ModalityType.DOCUMENT_MODAL);

        // Set the source and/or the source manifest based on the input source, make sure the source isn't null or invalid
        if(source == null)
            throw new NullPointerException("The source may not be null.");

        else if(source instanceof AbstractEntity) {
            this.source = (AbstractEntity) source;
            this.sourceManifest = this.source.getManifest();

        } else if(source instanceof AbstractEntityManifest)
            this.sourceManifest = (AbstractEntityManifest) source;

        else
            throw new IllegalArgumentException("Invalid source type, must be an AbstractEntity or" +
                    "AbstractEntityManifest type.");

        // Clone the source to create a result object, or create a new instance if the source is unspecified
        if(this.source != null)
            try {
                this.result = this.source.clone();
            } catch(CloneNotSupportedException e) {
                e.printStackTrace();
            }
        else
            try {
                this.result = this.sourceManifest.getEntity().newInstance();
            } catch(InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
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
        setTitle(FORM_TITLE + " - " + (this.source != null ? "Modify" : "Create") + " " + this.sourceManifest.getTypeName(false, false));

        // Show the form
        this.setVisible(show);
    }

    /**
     * Create a new entity, and show the modification dialog.
     * The newly created object is returned when the dialog is closed.
     * Null is returned if the creation process is cancelled.
     *
     * @param owner Owner window.
     * @param manifest The manifest of the object to create.
     *
     * @return The created entity, or null.
     */
    public static AbstractEntity showCreate(Window owner, AbstractEntityManifest manifest) {
        // Make sure the manifest object isn't null
        if(manifest == null)
            throw new NullPointerException("The entity manifest may not be null.");

        // Show the modify dialog with a new entity and return the result
        return showModify(owner, manifest);
    }

    /**
     * Show the modification dialog for the given entity.
     * The modified entity is returned when the modification dialog is closed.
     * The source is returned if the modification process is cancelled.
     * The source may be null, in order to create a new database configuration,
     * the source manifest must be specified in that case to determine the type of entity to create.
     *
     * @param owner Owner window.
     * @param source Source entity.
     *
     * @return A entity, or null.
     */
    public static AbstractEntity showModify(Window owner, AbstractEntity source) {
        // Make sure the source object isn't null
        if(source == null)
            throw new NullPointerException("The source object may not be null.");

        // Show the modify dialog and return the result
        return showModify(owner, (Object) source);
    }

    /**
     * Show the modification dialog for the given entity.
     * The modified entity is returned when the modification dialog is closed.
     * The source is returned if the modification process is cancelled.
     * The source may be null, in order to create a new database configuration,
     * the source manifest must be specified in that case to determine the type of entity to create.
     *
     * @param owner Owner window.
     * @param source Source entity or entity manifest.
     *
     * @return A entity, or null.
     */
    private static AbstractEntity showModify(Window owner, Object source) {
        // Create a new dialog instance
        EntityModifyDialog dialog = new EntityModifyDialog(owner, source, true);

        // TODO: Is this correct?
        // TODO: Use getters, instead of directly accessing the fields.

        // Return the source if the changes were discarded
        if(dialog.isDiscarded())
            return dialog.source;

        // Return the new database
        return dialog.result;
    }

    /**
     * Get the entity fields list.
     *
     * @return Entitys field list.
     */
    private EntityFieldsInterface[] getFields() {
        // Return the cached value if available
        if(this.fieldsCache != null)
            return this.fieldsCache;

        // Get the fields from the manifest
        try {
            this.fieldsCache = this.sourceManifest.getFieldValues();

        } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            // Show an error message to the user
            JOptionPane.showMessageDialog(this, "An error occurred while modifying the entity, the application will now quit.");

            // Throw an error
            throw new Error("Failed to access entity fields list.", e);
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
        // Construct a grid bag constraints object to specify the placement of all components
        GridBagConstraints c = new GridBagConstraints();

        // Set the frame layout
        this.setLayout(new BorderLayout());

        // Create the main panel, to put the question and answers in
        final JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Create a fields panel
        final JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(this.sourceManifest.getTypeName(true, false)),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Configure the placement of the questions label, and add it to the questions panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 0);
        if(this.source != null)
            container.add(new JLabel("Modify a " + this.sourceManifest.getTypeName(false, false) + "."), c);
        else
            container.add(new JLabel("Create a new " + this.sourceManifest.getTypeName(false, false) + "."), c);

        // Get the list of fields
        EntityFieldsInterface[] fieldTypes = getFields();

        // Create the field index variable
        int fieldIndex = 0;

        // Check whether the user has rights to edit values
        final boolean canEdit = PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel());

        // Loop through all the fields
        for(EntityFieldsInterface fieldType : fieldTypes) {
            // Get the field value
            Object valueRaw = null;
            String valueFormatted = null;
            if(this.source != null)
                try {
                    valueRaw = this.source.getField(fieldType);
                    valueFormatted = this.source.getFieldFormatted(fieldType);
                } catch(Exception e) {
                    e.printStackTrace();
                }

            // Hide empty fields that aren't editable/creatable
            if((this.source != null ? !fieldType.isEditable() : !fieldType.isCreatable()) && valueRaw == null)
                continue;

            // Create and add the name label
            c.fill = GridBagConstraints.NONE;
            c.gridx = 0;
            c.gridy = fieldIndex;
            c.gridwidth = 1;
            c.weightx = 0;
            c.insets = new Insets(fieldIndex == 0 ? 0 : 8, 0, 0, 8);
            c.anchor = GridBagConstraints.WEST;
            fieldsPanel.add(new JLabel(fieldType.getDisplayName() + ":"), c);

            // Create and add the name label
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = fieldIndex;
            c.gridwidth = 1;
            c.weightx = 1;
            c.insets = new Insets(fieldIndex == 0 ? 0 : 8, 8, 0, 0);
            c.anchor = GridBagConstraints.CENTER;

            // Show a label if the field is not editable
            if(!canEdit || (this.source != null ? !fieldType.isEditable() : !fieldType.isCreatable())) {
                if(!(valueRaw instanceof AbstractEntity))
                    fieldsPanel.add(new JLabel(valueRaw != null ? valueRaw.toString() : "?"), c);

                else {
                    // Create a new link label
                    LinkLabel linkLabel = new LinkLabel(valueFormatted);

                    // Open the view dialog when the links is clicked
                    final AbstractEntity otherEntity = (AbstractEntity) valueRaw;
                    linkLabel.addActionListener(e -> EntityViewDialog.showDialog(this, otherEntity));

                    // Add the label
                    fieldsPanel.add(linkLabel, c);
                }

                // Increase the field index
                fieldIndex++;

                // Continue
                continue;
            }

            // Create a variable for the property field instance
            AbstractPropertyField field;

            // Create the proper field for the given field type
            switch(fieldType.getBaseDataType()) {
                case DATE:
                    // Create the date field
                    DatePropertyField dateField =  new DatePropertyField(valueRaw, true);

                    // Set the maximum selectable date if we're working with birthday fields
                    if(fieldType.getExtendedDataType().equals(DataTypeExtended.BIRTHDAY))
                        dateField.setMaximumDate(new Date());

                    // Set the field
                    field = dateField;
                    break;

                case BOOLEAN:
                    switch(fieldType.getExtendedDataType()) {
                        case GENDER:
                            field = new GenderPropertyField((Boolean) valueRaw, true);
                            break;

                        default:
                            field = new BooleanPropertyField((Boolean) valueRaw, "Is " + fieldType.getDisplayName().toLowerCase(), true);
                            break;
                    }
                    break;

                case INTEGER:
                    switch(fieldType.getExtendedDataType()) {
                        case MILLISECONDS:
                            field = new MillisecondPropertyField((Integer) valueRaw, true);
                            break;

                        case CENTIMETER:
                            field = new CentimeterPropertyField((Integer) valueRaw, true);
                            break;

                        case GRAM:
                            field = new GramPropertyField((Integer) valueRaw, true);
                            break;

                        case PERMISSION_LEVEL:
                            field = new PermissionLevelPropertyField((PermissionLevel) valueRaw, true);
                            break;

                        default:
                            field = new IntegerPropertyField((Integer) valueRaw, true);
                            break;
                    }
                    break;

                case REFERENCE:
                    if(valueRaw != null)
                        field = new EntityPropertyField((AbstractEntity) valueRaw, true);
                    else
                        field = new EntityPropertyField(fieldType.getFieldManifest().getManagerInstance(), true);
                    break;

                case STRING:
                default:
                    switch(fieldType.getExtendedDataType()) {
                        case PASSWORD_HASH:
                            field = new PasswordPropertyField(null, true);
                            break;

                        default:
                            field = new TextPropertyField(valueRaw != null ? valueRaw.toString() : null, true);
                            break;
                    }
            }

            // Put the field in the fields hash map
            this.fields.put(fieldType, field);

            // Add the field
            fieldsPanel.add(field, c);

            // Increase the field index
            fieldIndex++;
        }

        // Loop through the couples
        for(AbstractEntityCoupleManifest abstractEntityManifest : this.sourceManifest.getCouples()) {
            // Create a couple panel
            final JPanel couplePanel = new JPanel(new BorderLayout());
            couplePanel.setBorder(new CompoundBorder(
                    BorderFactory.createTitledBorder(abstractEntityManifest.getReferenceTypeName(this.sourceManifest, true, true, true)),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
            ));

            // Create a small manager component to show the couples
            EntitySmallManagerComponent coupleView = new EntitySmallManagerComponent(abstractEntityManifest.getManagerInstance(), this.source != null ? this.source : this.result);
            couplePanel.add(coupleView, BorderLayout.CENTER);

            // Hide the couple panel if the source is unknown
            couplePanel.setVisible(this.source != null);

            // Add the couple panel
            this.couplePanels.add(couplePanel);

            // Add the panel
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = fieldIndex;
            c.gridwidth = 2;
            c.weightx = 1;
            c.weighty = 1;
            c.insets = new Insets(fieldIndex == 0 ? 0 : 16, 0, 0, 0);
            c.anchor = GridBagConstraints.CENTER;
            fieldsPanel.add(couplePanel, c);

            // Increase the field index
            fieldIndex++;
        }

        // Add the fields panel the container
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(16, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        container.add(fieldsPanel, c);

        // Create the control button panel and add it to the main panel
        JPanel controlsPanel = createControlButtonPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
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
            if(!applyToDatabase())
                return;

            // Close the frame
            dispose();
        });
        cancelButton.addActionListener(e -> closeFrame());
        applyButton.addActionListener(e -> applyToDatabase());

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

        // Create and add the revert button
        JButton revertButton = new JButton("Revert");
        revertButton.addActionListener(e -> revert());
        actionPanel.add(revertButton);

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
     * Get the source entity.
     *
     * @return Source entity.
     */
    public AbstractEntity getSource() {
        return this.source;
    }

    /**
     * Get the result entity.
     *
     * @return Result entity.
     */
    public AbstractEntity getResult() {
        return this.result;
    }

    /**
     * Close the frame. Ask whether the user wants to save the changes.
     */
    public void closeFrame() {
        // Only ask to save if there are any unsaved changes
        if(hasUnsavedChanges()) {
            // Ask whether the user wants to save the questions
            switch(JOptionPane.showConfirmDialog(this, "Would you like to save the changes?", this.sourceManifest.getTypeName(true, false) + " changed", JOptionPane.YES_NO_CANCEL_OPTION)) {
                case JOptionPane.YES_OPTION:
                    // Apply the changes
                    if(!applyToDatabase())
                        return;

                    // Set the discarded flag
                    discarded = false;
                    break;

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
     * Check whether this entity has unsaved changes.
     *
     * @return True if this entity has unsaved changes, false if not.
     */
    public boolean hasUnsavedChanges() {
        // Make sure all input is valid
        if(!validateInput(false))
            return true;

        // Apply the changes
        applyInputToResult();

        // Return true if the source is null
        if(this.source == null)
            return true;

        // There are unsaved changes if the source doesn't equal the result
        return !this.source.isCacheEqual(this.result);
    }

    /**
     * Check whether the changes have been discarded.
     *
     * @return True if the changes have been discarded.
     */
    public boolean isDiscarded() {
        return this.discarded;
    }

    /**
     * Revert the input fields to their original state.
     */
    private void revert() {
        // Loop through the fields
        for(Map.Entry<EntityFieldsInterface, AbstractPropertyField> entry : this.fields.entrySet()) {
            // Get the field and it's property field
            final EntityFieldsInterface fieldType = entry.getKey();
            final AbstractPropertyField field = entry.getValue();

            // Update the field
            try {
                if(this.source != null)
                    // Fetch the field from the source if the entity is modified
                    field.setValue(this.source.getField(fieldType));
                else

                    // Clear the field if the entity is newly created
                    field.clear();

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        // Check whether the frame size has changed
        boolean sizeChanged = false;

        //  Determine whether to show the couple panels
        final boolean showCouplePanels = this.source != null;

        // Set the visibility state of the couple panels
        for(JPanel couplePanel : this.couplePanels) {
            // Determine whether to show the panel now
            if(couplePanel.isVisible() != showCouplePanels) {
                couplePanel.setVisible(showCouplePanels);
                sizeChanged = true;
            }
        }

        // Reconfigure the frame size
        if(sizeChanged)
            configureSize();
    }

    /**
     * Validate the input of all fields.
     *
     * @param showMessage True to show a message for the field that is invalid, false if not.
     *
     * @return True if valid, false if invalid.
     */
    public boolean validateInput(boolean showMessage) {
        // Make sure the hash map contains any fields
        if(this.fields.size() == 0)
            throw new RuntimeException("Failed to validate input. No fields available.");

        // Loop through all the
        for(Map.Entry<EntityFieldsInterface, AbstractPropertyField> entry : this.fields.entrySet()) {
            // Get the field specification and property field
            EntityFieldsInterface fieldSpec = entry.getKey();
            AbstractPropertyField field = entry.getValue();

            // Skip password fields
            if(fieldSpec.getExtendedDataType().equals(DataTypeExtended.PASSWORD_HASH))
                continue;

            // Check whether null is allowed if the field is null
            if(!fieldSpec.isNullAllowed() && field.isNull()) {
                // Show a message
                if(showMessage)
                    JOptionPane.showMessageDialog(this, "Please enter the " + fieldSpec.getDisplayName().toLowerCase() + ".", "Invalid input", JOptionPane.ERROR_MESSAGE);

                // Return the result
                return false;
            }

            // Check whether an empty value is allowed
            if(!fieldSpec.isEmptyAllowed() && field.isInputEmpty()) {
                // Show a message
                if(showMessage)
                    JOptionPane.showMessageDialog(this, "The '" + fieldSpec.getDisplayName() + "' field may not be empty.", "Invalid input", JOptionPane.ERROR_MESSAGE);

                // Return the result
                return false;
            }

            // Make sure the field is valid
            if(!field.isInputValid()) {
                // Show a message
                if(showMessage)
                    JOptionPane.showMessageDialog(this, "The '" + fieldSpec.getDisplayName() + "' field contains an invalid value.", "Invalid input", JOptionPane.ERROR_MESSAGE);

                // Return the result
                return false;
            }
        }

        // All fields seem to be valid, return the result
        return true;
    }

    /**
     * Apply the input to the result entity.
     */
    public void applyInputToResult() {
        // Make sure the hash map contains any fields
        if(this.fields.size() == 0)
            throw new RuntimeException("Failed to validate input. No fields available.");

        // Loop through all the
        for(Map.Entry<EntityFieldsInterface, AbstractPropertyField> entry : this.fields.entrySet()) {
            // Get the field specification and property field
            EntityFieldsInterface fieldSpec = entry.getKey();
            AbstractPropertyField field = entry.getValue();

            // Handle password fields
            if(fieldSpec.getExtendedDataType().equals(DataTypeExtended.PASSWORD_HASH)) {
                // Get the password field
                PasswordPropertyField passwordField = (PasswordPropertyField) field;

                // Skip null or empty fields
                if(passwordField.isNull() || passwordField.getText().length() == 0)
                    continue;

                try {
                    // Hash the password
                    String hash = HashUtil.hash(passwordField.getText());

                    // Put the hash in the list
                    this.result.getCachedFields().put(fieldSpec, hash);

                } catch(NoSuchAlgorithmException e) {
                    // Print the stack trace
                    e.printStackTrace();

                    // Show an error message
                    JOptionPane.showMessageDialog(this, "Failed to securely store the entered password. The password won't be changed.", "Password failure", JOptionPane.WARNING_MESSAGE);
                }

                // Continue
                continue;
            }

            // Put the field value into the entity cache
            this.result.getCachedFields().put(fieldSpec, field.getValue());
        }
    }

    /**
     * Apply the modifications to the database.
     * This method will also validate the input, and it will be cancelled if the input is invalid.
     *
     * @return True on success, false on failure.
     */
    public boolean applyToDatabase() {
        // Make sure all input is valid
        if(!validateInput(true))
            return false;

        // Show a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this, "Saving changes...", false, "Processing changes...", true);

        // Apply the changes to the result object
        applyInputToResult();

        // Apply the result to the database
        progressDialog.setStatus("Saving changes...");
        if(!this.result.applyToDatabase()) {
            // Show an error message, and return false
            JOptionPane.showMessageDialog(this, "Failed to store the " + this.sourceManifest.getTypeName(false, false) + " in the database.", "Database error", JOptionPane.ERROR_MESSAGE);
            progressDialog.dispose();
            return false;
        }

        // Refresh the managers
        for(AbstractEntityManifest abstractEntityManifest : this.result.getManifest().getReferencedManifests())
            abstractEntityManifest.getManagerInstance().refresh();
        this.result.getManifest().getManagerInstance().refresh();

        // Update the source object by cloning the result
        try {
            this.source = this.result.clone();

            // Revert the input fields
            revert();

        } catch(CloneNotSupportedException e) {
            // Print the stack trace
            e.printStackTrace();

            // Show an error message
            JOptionPane.showMessageDialog(this, App.APP_NAME + " recovered an error. The " + this.sourceManifest.getTypeName(false, false) + " has been stored in the database successfully, the modification dialog must however close.", "Application error", JOptionPane.ERROR_MESSAGE);

            // Set the discarded flag
            this.discarded = false;

            // Dispose the dialog
            this.dispose();
        }

        // Dispose the progress dialog
        progressDialog.dispose();

        // Return the result
        return true;
    }
}
