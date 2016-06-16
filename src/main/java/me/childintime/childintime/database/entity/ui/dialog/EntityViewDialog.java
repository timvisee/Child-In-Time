package me.childintime.childintime.database.entity.ui.dialog;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.EntityFieldsInterface;
import me.childintime.childintime.database.entity.datatype.DataTypeExtended;
import me.childintime.childintime.permission.PermissionLevel;
import me.childintime.childintime.ui.component.LinkLabel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

public class EntityViewDialog extends JDialog {

    // TODO: This dialog is based on the EntityModifyDialog, and has not fully been converted yet!

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
     * Database fields cache.
     */
    private EntityFieldsInterface[] fieldsCache = null;

    /**
     * Constructor, to modify an existing entity.
     *
     * @param owner The parent window.
     * @param source The source entity.
     * @param show True to show the frame once it has been initialized.
     */
    public EntityViewDialog(Window owner, AbstractEntity source, boolean show) {
        this(owner, (Object) source, show);
    }

    /**
     * Constructor, to create a new entity.
     *
     * @param owner The parent window.
     * @param manifest The entity manifest to create a new object for.
     * @param show True to show the frame once it has been initialized.
     */
    public EntityViewDialog(Window owner, AbstractEntityManifest manifest, boolean show) {
        this(owner, (Object) manifest, show);
    }

    /**
     * Constructor.
     *
     * @param owner The parent window.
     * @param source The entity manifest.
     * @param show True to show the frame once it has been initialized.
     */
    private EntityViewDialog(Window owner, Object source, boolean show) {
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
                dispose();
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
        setTitle(FORM_TITLE + " - View " + this.sourceManifest.getTypeName(false, false));

        // Show the form
        this.setVisible(show);
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
     */
    public static void showDialog(Window owner, AbstractEntity source) {
        // Create a new dialog instance
        EntityViewDialog dialog = new EntityViewDialog(owner, source, true);
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
        container.add(new JLabel("View a " + this.sourceManifest.getTypeName(false, false) + "."), c);

        // Get the list of fields
        EntityFieldsInterface[] fieldTypes = getFields();

        // Create the field index variable
        int fieldIndex = 0;

        // Loop through all the fields
        for(EntityFieldsInterface fieldType : fieldTypes) {
            // Skip password fields
            if(fieldType.getExtendedDataType().equals(DataTypeExtended.PASSWORD_HASH))
                continue;

            // Get the field value
            String valueFormatted = null;
            Object valueRaw = null;
            if(this.source != null)
                try {
                    valueFormatted = this.source.getFieldFormatted(fieldType);
                    valueRaw = this.source.getField(fieldType);

                } catch(Exception e) {
                    e.printStackTrace();
                }

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

            // Show a label
            if(!(valueRaw instanceof AbstractEntity))
                fieldsPanel.add(new JLabel(valueFormatted), c);

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
        }

        // Add the fields panel the container
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
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
        commitPanel.setLayout(new GridLayout(1, 1, 8, 8));

        // Create an action button panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1, 1, 8, 8));

        // Check whether the user has edit rights
        final boolean canEdit = PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel());

        // Create the commit buttons
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        commitPanel.add(closeButton);

        // Create and add the test button
        JButton modifyButton = new JButton("Modify");
        modifyButton.addActionListener(e -> {
            // Make sure the user can edit
            if(!canEdit)
                return;

            // Show the modify dialog for this entity
            EntityModifyDialog.showModify(this, this.source);

            // Dispose the current frame
            dispose();

            // Refresh the manager
            this.source.getManifest().getManagerInstance().refresh();
        });
        modifyButton.setEnabled(canEdit);
        actionPanel.add(modifyButton);

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
}
