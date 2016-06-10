package me.childintime.childintime.database.object.ui.dialog;

import me.childintime.childintime.App;
import me.childintime.childintime.database.object.AbstractEntity;
import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.ui.component.EntityListComponent;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import java.awt.*;

public class EntityManagerDialog extends JDialog {

    /**
     * The entity manager this dialog is used for.
     */
    private AbstractEntityManager manager;

    /**
     * The list component.
     */
    public EntityListComponent list;

    /**
     * Add button instance.
     */
    private JButton addButton;

    /**
     * Edit button instance.
     */
    private JButton editButton;

    /**
     * Remove button instance.
     */
    private JButton removeButton;

    /**
     * Refresh button instance.
     */
    private JButton refreshButton;

    /**
     * Filters button instance.
     */
    private JButton filtersButton;

    /**
     * Columns button instance.
     */
    private JButton columnsButton;

    /**
     * Constructor.
     *
     * @param owner Owner dialog.
     * @param manager Entity manager instance.
     * @param show True to show the frame once it has been initialized.
     */
    public EntityManagerDialog(Window owner, AbstractEntityManager manager, boolean show) {
        // Construct the form
        super(owner, App.APP_NAME + " - " + manager.getManifest().getTypeName(true, true), ModalityType.APPLICATION_MODAL);

        // Set the entity manager
        this.manager = manager;

        // Create the form UI
        buildUi();

        // Do not close the window when pressing the red cross, execute the close method instead
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Configure the frame size
        configureSize();

        // Make the explicitly frame resizable
        this.setResizable(true);

        // Set the window location to the system's default
        this.setLocationByPlatform(true);
        this.setLocationRelativeTo(owner);

        // Show the form
        if(show)
            this.setVisible(true);
    }

    /**
     * Properly configure the window sizes for the current content.
     */
    private void configureSize() {
        // Pack everything
        pack();

        // Get the minimum width and size
        final int minWidth = getMinimumSize().width;
        final int minHeight = getMinimumSize().height;

        // Configure the sizes
        setMinimumSize(new Dimension(minWidth + 50, minHeight + 50));
        setPreferredSize(new Dimension(minWidth + 400, minHeight + 200));
        setSize(new Dimension(minWidth + 400, minHeight + 200));
    }

    /**
     * Build the UI components for this window.
     */
    private void buildUi() {
        // Construct a grid bag constraints object to specify the placement of all components
        GridBagConstraints c = new GridBagConstraints();

        // Set the frame layout
        this.setLayout(new GridBagLayout());

        // Create the main panel, to put the question and answers in
        JPanel mainPanel = new JPanel(new GridBagLayout());

        // Create the main label
        final JLabel instructionLabel = new JLabel("Add, edit or delete " + this.manager.getManifest().getTypeName(false, true).toLowerCase() + ".");

        // Add the instruction label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(instructionLabel, c);

        // Add the objects management panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(buildUiObjectManagementPanel(), c);

        // Add the commit buttons panel
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10, 10, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        mainPanel.add(buildUiCommitButtonsPanel(), c);

        // Add the main panel to the frame
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(10, 10, 10, 10);
        this.add(mainPanel, c);

        // Update the button panel
        updateUiButtons();
    }

    /**
     * Build the object management panel.
     *
     * @return Object management panel.
     */
    private JPanel buildUiObjectManagementPanel() {
        // Construct a grid bag constraints object to specify the placement of all components
        GridBagConstraints c = new GridBagConstraints();

        // Create the object table panel, and give it a titled border
        JPanel objectPanel = new JPanel(new GridBagLayout());
        objectPanel.setBorder(BorderFactory.createTitledBorder(this.manager.getManifest().getTypeName(true, true)));

        // Create the manage button panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 4, 4, 10);
        c.anchor = GridBagConstraints.NORTH;
        objectPanel.add(buildUiManageButtonsPanel(), c);

        // Create the manage button panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 4, 4, 10);
        c.anchor = GridBagConstraints.SOUTH;
        objectPanel.add(buildUiViewSelectionButtonsPanel(), c);

        // Create the database manager list and add it to the main panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 2;
        c.insets = new Insets(0, 0, 4, 4);
        objectPanel.add(buildUiTable(), c);

        // Return the created panel
        return objectPanel;
    }

    /**
     * Create a table to manage the entitys in.
     *
     * @return Scroll pane with table.
     */
    private EntityListComponent buildUiTable() {
        // Create a new list view for the manager
        this.list = new EntityListComponent(this.manager);

        // Update the button panel on selection change
        this.list.addSelectionChangeListenerListener(this::updateUiButtons);

        // Edit a table item when it's double clicked
        this.list.addEntityActionListener(entities -> {
            // Make sure only one entity is selected
            if(entities.size() > 1) {
                JOptionPane.showMessageDialog(this, "Only one " + manager.getManifest().getTypeName(false, false) + " can be edited at a time.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Edit the selected object
            editObject(entities.get(0));
        });

        // Return the list view
        return this.list;
    }

    /**
     * Create the button panel to manage the databases.
     *
     * @return Button panel.
     */
    private JPanel buildUiManageButtonsPanel() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Create the buttons to add to the panel
        this.addButton = new JButton("Add");
        this.editButton = new JButton("Edit");
        this.removeButton = new JButton("Remove");

        // Add the buttons to the panel
        buttonPanel.add(this.addButton);
        buttonPanel.add(this.editButton);
        buttonPanel.add(this.removeButton);
        this.addButton.addActionListener(e -> addObject());
        this.editButton.addActionListener(e -> editSelected());
        this.removeButton.addActionListener(e -> removeDatabases());

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Create the button panel to modify the shown features in the object list view.
     *
     * @return Button panel.
     */
    private JPanel buildUiViewSelectionButtonsPanel() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Create the buttons to add to the panel
        this.refreshButton = new JButton("Refresh");
        this.filtersButton = new JButton("Filters...");
        this.columnsButton = new JButton("Columns...");

        // Add the buttons to the panel
        buttonPanel.add(this.refreshButton);
        buttonPanel.add(this.filtersButton);
        buttonPanel.add(this.columnsButton);
        this.refreshButton.addActionListener(e -> refresh());
        this.filtersButton.addActionListener(e -> featureNotImplemented());
        this.columnsButton.addActionListener(e -> featureNotImplemented());

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Create the commit buttons panel to control the form.
     *
     * @return Button panel.
     */
    private JPanel buildUiCommitButtonsPanel() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));

        // Create the commit buttons
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            // Close the frame
            dispose();
        });

        // Add the buttons to the panel in the proper order
        buttonPanel.add(closeButton);

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Update the state of buttons in the button panel.
     */
    private void updateUiButtons() {
        // Get the number of selected items
        int selected = getSelectedCount();

        // Enable the edit button if one item is selected
        editButton.setEnabled(selected == 1);

        // Enable the test and delete buttons if at least one database is selected
        removeButton.setEnabled(selected > 0);
    }

    /**
     * Create a new entity.
     */
    public void addObject() {
        // Create a new database through the edit panel
        final AbstractEntity entity = EntityModifyDialog.showCreate(this, this.manager.getManifest());

        // Add the entity to the list if it isn't null
        if(entity != null) {
            // TODO: Only insert the created object, instead of refreshing everything?

            // Refresh the manager
            this.manager.refresh();
        }
    }

    /**
     * Edit the selected object.
     */
    public void editSelected() {
        // Make sure at least one entity is selected
        if(this.list.getSelectedCount() > 1) {
            JOptionPane.showMessageDialog(this, "Please select a " + manager.getManifest().getTypeName(false, false) + " to edit.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Make sure only one entity is selected
        if(this.list.getSelectedCount() > 1) {
            JOptionPane.showMessageDialog(this, "Only one " + manager.getManifest().getTypeName(false, false) + " can be edited at a time.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Edit the selected entity
        editObject(this.list.getSelectedEntities().get(0));
    }

    /**
     * Edit the given object.
     */
    public void editObject(AbstractEntity object) {
        // Make sure just one item is selected
        if(getSelectedCount() != 1)
            return;

        // Show the edit dialog for this database
        final AbstractEntity result = EntityModifyDialog.showModify(this, object);

        // TODO: Only edit the given objec, instead of refreshing everything?

        // Refresh the list of objects
        this.manager.refresh();
    }

    /**
     * Remove the selected databases.
     */
    public void removeDatabases() {
        // Make sure at least one item is selected
        if(getSelectedCount() <= 0)
            return;

        // Get the type name
        final String question = "Are you sure you'd like to delete " + (getSelectedCount() == 1 ? "this" : "these") + " " + this.manager.getManifest().getTypeName(false, getSelectedCount() != 1) + "?";

        // Ask whether the user wants to delete the databases
        // TODO: Show a proper message!
        switch(JOptionPane.showConfirmDialog(this, question, "Delete " + this.manager.getManifest().getTypeName(false, true), JOptionPane.YES_NO_OPTION)) {
            case JOptionPane.NO_OPTION:
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                return;
        }

        // Create a progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this, "Deleting...", false, "Deleting " + this.manager.getManifest().getTypeName(false, true) + "...", true);
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(getSelectedCount());

        // Delete the selected entity
        for(AbstractEntity entity : this.list.getSelectedEntities()) {
            // Delete the object
            if(!entity.deleteFromDatabase())
                // TODO: Show improved message
                JOptionPane.showMessageDialog(this, "Failed to delete " + this.manager.getManifest().getTypeName(false, false) + " from database.", "Failed to delete", JOptionPane.ERROR_MESSAGE);

            // Set the progress
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();

        // TODO: Only remove the above entities instead of refreshing everything?

        // Refresh the manager
        this.manager.refresh();
    }

    // TODO: This should be removed!
    @Deprecated
    public void featureNotImplemented() {
        JOptionPane.showMessageDialog(this, "Feature not implemented yet!", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Get the number of selected entitys.
     *
     * @return Number of selected entitys.
     */
    public int getSelectedCount() {
        return this.list.getSelectedCount();
    }

    /**
     * Refresh the list of entitys.
     * This also flushes the cache and forces new objects to be fetched.
     */
    public void refresh() {
        // Create a progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this, App.APP_NAME, false, "Refreshing...", true);

        // Refresh the manager
        this.manager.refresh();

        // Dispose the dialog
        progressDialog.dispose();
    }
}
