package me.childintime.childintime.database.object.window;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.util.Platform;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseObjectManagerDialog extends JDialog {

    /**
     * The database object manager this dialog is used for.
     */
    private AbstractDatabaseObjectManager objectManager;

    /**
     * Data list model instance.
     */
    private DefaultListModel<AbstractDatabaseObject> objectsListModel;

    /**
     * Data list instance.
     */
    private JList objectList;

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
     * List of abstract database objects being shown.
     */
    private List<AbstractDatabaseObject> objects = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param owner Owner dialog.
     * @param objectManager Database object manager instance.
     * @param show True to show the frame once it has been initialized.
     */
    public DatabaseObjectManagerDialog(Window owner, AbstractDatabaseObjectManager objectManager, boolean show) {
        // Construct the form
        super(owner, App.APP_NAME + " - " + objectManager.getTypeName() + " manager", ModalityType.APPLICATION_MODAL);

        // Set the database object manager
        this.objectManager = objectManager;

        // Fetch the databases
        this.objects = this.objectManager.getObjectsClone();

        // Create the form UI
        buildUi();

        // Do not close the window when pressing the red cross, execute the close method instead
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
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

        // Set the frame sizes
        // TODO: Figure this out dynamically, using pack().
        this.setMinimumSize(new Dimension(350, 465));
        this.setPreferredSize(new Dimension(400, 450));
        this.setSize(new Dimension(400, 450));

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
     * Build the UI components for this window.
     */
    private void buildUi() {
        // Construct a grid bag constraints object to specify the placement of all components
        GridBagConstraints c = new GridBagConstraints();

        // Set the frame layout
        this.setLayout(new GridBagLayout());

        // Create the main panel, to put the question and answers in
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new GridBagLayout());

        // Create the main label
        final JLabel mainLabel = new JLabel("Add, edit or delete " + this.objectManager.getTypeName().toLowerCase() + "s.");

        // Add the main label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0, 0, 25, 0);
        pnlMain.add(mainLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.insets = new Insets(0, 0, 10, 10);
        pnlMain.add(new JLabel(this.objectManager.getTypeName() + "s:"), c);

        // Create the database manager list and add it to the main panel
        JScrollPane databaseList = buildUiList();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        pnlMain.add(databaseList, c);

        // Create the manage button panel
        JPanel manageButtonPanel = buildUiManageButtonsPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 10, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        pnlMain.add(manageButtonPanel, c);

        // Create the control button panel
        JPanel controlButtonPanel = buildUiCommitButtonsPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        c.anchor = GridBagConstraints.SOUTH;
        pnlMain.add(controlButtonPanel, c);

        // Configure the main panel placement and add it to the frame
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(10, 10, 10, 10);
        this.add(pnlMain, c);

        // Update the button panel
        updateUiButtons();
    }

    /**
     * Create a list to manage the database objects in.
     *
     * @return Scroll pane with list.
     */
    public JScrollPane buildUiList() {
        // Create the default list model
        this.objectsListModel = new DefaultListModel<>();

        // Refresh the list of databases objects and add them to the list
        updateUiList();

        // Create the list and create an empty border
        this.objectList = new JList<>(this.objectsListModel);
        this.objectList.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Update the button panel on selection change
        this.objectList.addListSelectionListener(e -> updateUiButtons());
        this.objectList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(evt.getClickCount() == 2)
                    editDatabase();
            }
        });

        // Create a scroll pane with the database list and return it
        return new JScrollPane(this.objectList);
    }

    /**
     * Update the UI list with the current list of database objects.
     */
    public void updateUiList() {
        // Clear all current database objects.
        this.objectsListModel.clear();

        // Add the items to the list model
        this.objects.forEach(this.objectsListModel::addElement);
    }

    /**
     * Create the button panel to manage the databases.
     *
     * @return Button panel.
     */
    public JPanel buildUiManageButtonsPanel() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Create the buttons to add to the panel
        this.addButton = new JButton("Add");
        this.editButton = new JButton("Edit");
        this.removeButton = new JButton("Remove");

        // Add the buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        addButton.addActionListener(e -> addDatabase());
        editButton.addActionListener(e -> editDatabase());
        removeButton.addActionListener(e -> removeDatabases());

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Create the commit buttons panel to control the form.
     *
     * @return Button panel.
     */
    public JPanel buildUiCommitButtonsPanel() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Create the buttons to add to the panel
        JButton okButton = new JButton("OK");
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        okButton.addActionListener(e -> {
            // Save the databases
            applyDatabases();

            // Close the frame
            dispose();
        });
        applyButton.addActionListener(e -> applyDatabases());
        cancelButton.addActionListener(e -> onClose());

        // Add the buttons to the panel in the proper order
        if(!Platform.isMacOsX()) {
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            buttonPanel.add(applyButton);
        } else {
            buttonPanel.add(applyButton);
            buttonPanel.add(cancelButton);
            buttonPanel.add(okButton);
        }

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Update the state of buttons in the button panel.
     */
    public void updateUiButtons() {
        // Get the number of selected items
        int selected = getSelectedCount();

        // Enable the edit button if one item is selected
        editButton.setEnabled(selected == 1);

        // Enable the test and delete buttons if at least one database is selected
        removeButton.setEnabled(selected > 0);
    }

    /**
     * Create a new database, ask for the name.
     */
    public void addDatabase() {
        // Create a new database through the edit panel
        // TODO: Implement modification dialog here!
        final AbstractDatabaseObject databaseObject = null; // DatabaseModifyDialog.showCreate(this)

        JOptionPane.showMessageDialog(this, "Feature not implemented yet!", App.APP_NAME, JOptionPane.ERROR_MESSAGE);

        // Add the database object to the list if it isn't null
        if(databaseObject != null) {
            // Add the database
            this.objects.add(databaseObject);

            // Refresh the list of databases
            updateUiList();
        }
    }

    /**
     * Edit the selected database.
     */
    public void editDatabase() {
        // Make sure just one item is selected
        if(getSelectedCount() != 1)
            return;

        // Get the selected database
        final AbstractDatabaseObject selected = (AbstractDatabaseObject) this.objectList.getSelectedValue();

        // Show the edit dialog for this database
        // TODO: Implement the edit dialog here
        final AbstractDatabaseObject result = null; // DatabaseModifyDialog.showModify(this, selected)

        JOptionPane.showMessageDialog(this, "Feature not implemented yet!", App.APP_NAME, JOptionPane.ERROR_MESSAGE);

        // TODO: Update this?
        // Set the result, or remove it from the list if it's null
        if(result != null)
            this.objects.set(this.objectList.getSelectedIndex(), result);
        else
            this.objects.remove(this.objectList.getSelectedIndex());

        // Refresh the list
        updateUiList();
    }

    /**
     * Remove the selected databases.
     */
    public void removeDatabases() {
        // Make sure at least one item is selected
        if(getSelectedCount() <= 0)
            return;

        // Get the type name
        final String typeName = this.objectManager.getTypeName().toLowerCase();

        // Ask whether the user wants to delete the databases
        // TODO: Show a proper message!
        switch(JOptionPane.showConfirmDialog(this, "Are you sure you'd like to delete this " + typeName + "?", "Delete " + typeName, JOptionPane.YES_NO_OPTION)) {
            case JOptionPane.NO_OPTION:
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                return;
        }

        JOptionPane.showMessageDialog(this, "Feature not implemented yet!", App.APP_NAME, JOptionPane.ERROR_MESSAGE);

        // TODO: Improve this!
//        // Delete the selected database object
//        for(Object databaseObject : this.objectList.getSelectedValuesList())
//            //noinspection RedundantCast
//            this.objects.remove((AbstractDatabaseObject) databaseObject);

        // Refresh the list
        updateUiList();
    }

    /**
     * Get the number of selected database objects.
     *
     * @return Number of selected database objects.
     */
    public int getSelectedCount() {
        return this.objectList.getSelectedValuesList().size();
    }

    /**
     * Apply and save the databases.
     */
    // TODO: Create a method that applies the difference between the original list, and the new list, to the database using proper queries.
    public void applyDatabases() {
        // Store a copy of the databases
        // TODO: Reimplement this
        //Core.getInstance().getDatabaseManager().setDatabases(this.objects);

        // Create a progress dialog and save the database configuration
        ProgressDialog progress = new ProgressDialog(this, "Working...", false, "Saving database configuration...", true);

        try {
            // Save the database configuration
            Core.getInstance().getDatabaseManager().save();

            // Dispose the progress dialog
            progress.dispose();

        } catch (IOException e) {
            // Show a status message and print the stack trace
            System.out.println("Failed to save database configuration.");
            e.printStackTrace();

            // Dispose the progress dialog
            progress.dispose();


            // Show a message dialog to inform the user
            JOptionPane.showMessageDialog(null, "Failed to save database configuration.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Called when the frame should be closed.
     * This method determines whether to close the frame, with a confirmation dialog if it contains any unsaved changes.
     */
    public void onClose() {
        // Only ask to save if there are any unsaved changes
        if(hasUnsavedChanges()) {
            // Ask whether the user wants to save the databases
            switch(JOptionPane.showConfirmDialog(this, "Would you like to save the changes?", "Databases changed", JOptionPane.YES_NO_CANCEL_OPTION)) {
                case JOptionPane.YES_OPTION:
                    // Save the changes
                    applyDatabases();

                case JOptionPane.NO_OPTION:
                    // Dispose the frame
                    this.dispose();
                    break;
            }

        } else
            this.dispose();
    }

    /**
     * Check whether this question has unsaved changes.
     *
     * @return True if this question has unsaved changes, false if not.
     */
    public boolean hasUnsavedChanges() {
        // TODO: Reimplement this!
//        // Get the database manager
//        AbstractDatabaseObject databaseManager = Core.getInstance().getDatabaseManager();
//
//        // Compare the number of databases
//        if(databaseManager.getDatabaseCount() != this.objects.size())
//            return true;
//
//        // Compare the databases
//        for(int i = 0; i < this.objects.size(); i++)
//            if(!databaseManager.getDatabase(i).equals(this.objects.get(i)))
//                return true;

        // There don't seem to be any unsaved changes, return the result
        return false;
    }
}
