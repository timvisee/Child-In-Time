package me.childintime.childintime.database.configuration.gui.window;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.configuration.AbstractDatabase;
import me.childintime.childintime.database.configuration.DatabaseManager;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatabaseManagerDialog extends JDialog {

    /**
     * Frame title.
     */
    private static final String FORM_TITLE = App.APP_NAME + " - Database configuration manager";

    /**
     * The database manager label.
     */
    private JLabel mainLabel = new JLabel("Add, edit or delete databases.");

    /**
     * Database list model instance.
     */
    private DefaultListModel<AbstractDatabase> databaseListModel;

    /**
     * Database list instance.
     */
    private JList databaseList;

    /**
     * Test button instance.
     */
    private JButton testButton;

    /**
     * Create button instance.
     */
    private JButton addButton;

    /**
     * Edit button instance.
     */
    private JButton editButton;

    /**
     * Move up button instance.
     */
    private JButton moveUpButton;

    /**
     * Move down button instance.
     */
    private JButton moveDownButton;

    /**
     * Remove button instance.
     */
    private JButton removeButton;

    /**
     * List of the databases being shown.
     */
    private List<AbstractDatabase> databases = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param owner Owner dialog.
     * @param show True to show the frame once it has been initialized.
     */
    public DatabaseManagerDialog(Window owner, boolean show) {
        // Construct the form
        super(owner, FORM_TITLE, ModalityType.APPLICATION_MODAL);

        // Load the databases
        this.databases = Core.getInstance().getDatabaseManager().getDatabasesClone();

        // Create the form UI
        buildUi();

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

        // Set the frame sizes
        this.setMinimumSize(new Dimension(350, 465));
        this.setPreferredSize(new Dimension(400, 450));
        this.setSize(new Dimension(400, 450));

        // Set the window location to the system's default
        this.setLocationByPlatform(true);
        this.setLocationRelativeTo(owner);

        // Show the form
        this.setVisible(show);
    }

    /**
     * Build the UI components for this window.
     */
    private void buildUi() {
        // Construct a grid bag constraints object to specify the placement of all components
        GridBagConstraints c = new GridBagConstraints();

        // Set the frame layout
        this.setLayout(new GridBagLayout());

        // Make the frame non-resizable
        this.setResizable(true);

        // Create the main panel, to put the question and answers in
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new GridBagLayout());

        // Configure the placement of the questions label, and add it to the questions panel
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
        pnlMain.add(new JLabel("Databases:"), c);

        // Create the database manager list and add it to the main panel
        JScrollPane databaseList = createDatabaseList();
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
        JPanel manageButtonPanel = createManageButtonPanel();
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
        JPanel controlButtonPanel = createControlButtonPanel();
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
        updateButtons();
    }

    /**
     * Create a list to manage the databases in.
     *
     * @return Scroll pane with list.
     */
    public JScrollPane createDatabaseList() {
        // Create the default list model
        this.databaseListModel = new DefaultListModel<>();

        // Refresh the list of databases to add them to the list model
        updateListView();

        // Create the list and create an empty border
        this.databaseList = new JList<>(this.databaseListModel);
        this.databaseList.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Update the button panel on selection change
        this.databaseList.addListSelectionListener(e -> updateButtons());
        this.databaseList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(evt.getClickCount() == 2)
                    editDatabase();
            }
        });

        // Create a scroll pane with the database list and return it
        return new JScrollPane(this.databaseList);
    }

    /**
     * Update the list view to show the list of databases.
     */
    public void updateListView() {
        // Clear all current items
        this.databaseListModel.clear();

        // Add the items
        this.databases.forEach(this.databaseListModel::addElement);
    }

    /**
     * Create the button panel to manage the databases.
     *
     * @return Button panel.
     */
    public JPanel createManageButtonPanel() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));

        // Create the buttons to add to the panel
        this.testButton = new JButton("Test");
        this.addButton = new JButton("Add");
        this.editButton = new JButton("Edit");
        this.moveUpButton = new JButton("Move up");
        this.moveDownButton = new JButton("Move down");
        this.removeButton = new JButton("Remove");

        // Add the buttons to the panel
        buttonPanel.add(testButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(moveUpButton);
        buttonPanel.add(moveDownButton);
        buttonPanel.add(removeButton);
        testButton.addActionListener(e -> testDatabase());
        addButton.addActionListener(e -> addDatabase());
        editButton.addActionListener(e -> editDatabase());
        moveUpButton.addActionListener(e -> moveDatabasesUp());
        moveDownButton.addActionListener(e -> moveDatabasesDown());
        removeButton.addActionListener(e -> removeDatabases());

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Create the button panel to control the form.
     *
     * @return Button panel.
     */
    public JPanel createControlButtonPanel() {
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
        cancelButton.addActionListener(e -> closeFrame());

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
    public void updateButtons() {
        // Get the number of selected items
        int selected = getSelectedCount();

        // Enable the edit button if one item is selected
        editButton.setEnabled(selected == 1);

        // Enable the test and delete buttons if at least one database is selected
        testButton.setEnabled(selected > 0);
        removeButton.setEnabled(selected > 0);

        // Enable the move buttons if at least one database is selected and if the databases can move in that direction
        moveUpButton.setEnabled(canMoveDatabasesUp());
        moveDownButton.setEnabled(canMoveDatabasesDown());
    }

    /**
     * Test the selected databases.
     *
     * @return True if all tests succeeded, false if not. If no database is selected, false will be returned.
     */
    public boolean testDatabase() {
        // Make sure just one or more databases are selected
        if(getSelectedCount() == 0)
            return false;

        // Create a progress dialog for testing
        ProgressDialog progressDialog = new ProgressDialog(this, "Testing database...", false, "Testing database configurations...", true);

        // Get the list of selected databases
        List selected = this.databaseList.getSelectedValuesList();

        // Loop through the databases
        for(Object entity : selected) {
            // Make sure the instance is valid
            if(!(entity instanceof AbstractDatabase))
                continue;

            // Cast the entity
            AbstractDatabase database = (AbstractDatabase) entity;

            // Make sure the database is successfully configured
            if(!database.isConfigured())
                // Edit the selected database
                DatabaseModifyDialog.showModify(progressDialog, database);

            // Show a message if it's still not configured properly
            if(!database.isConfigured()) {
                // Show a status message
                JOptionPane.showMessageDialog(progressDialog, "The database configuration '" + database.getName() + "' is missing some required properties.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);

                // Dispose the progress dialog and return the result
                progressDialog.dispose();
                return false;
            }

            // Test the database
            if(!database.test(this, progressDialog)) {
                // Dispose the progress dialog and return the result
                progressDialog.dispose();
                return false;
            }
        }

        // Dispose the progress dialog
        progressDialog.dispose();

        // Show a success message
        JOptionPane.showMessageDialog(
                this,
                "Successfully connected to the selected database" + (getSelectedCount() > 1 ? "s" : "") + ".",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Everything seems to be fine, return the result
        return true;
    }

    /**
     * Create a new database, ask for the name.
     */
    public void addDatabase() {
        // Create a new database through the edit panel
        final AbstractDatabase database = DatabaseModifyDialog.showCreate(this);

        // Add the database to the list if it isn't null
        if(database != null) {
            // Add the database
            this.databases.add(database);

            // Refresh the list of databases
            updateListView();
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
        final AbstractDatabase selected = (AbstractDatabase) this.databaseList.getSelectedValue();

        // Show the edit dialog for this database
        final AbstractDatabase result = DatabaseModifyDialog.showModify(this, selected);

        // Set the result, or remove it from the list if it's null
        if(result != null)
            this.databases.set(this.databaseList.getSelectedIndex(), result);
        else
            this.databases.remove(this.databaseList.getSelectedIndex());

        // Refresh the list
        updateListView();
    }

    /**
     * Move the selected databases up.
     */
    public void moveDatabasesUp() {
        // Make sure at least one database is selected
        if(getSelectedCount() <= 0)
            return;

        // Get the indices
        int[] indices = this.databaseList.getSelectedIndices();

        // Move the databases
        if(moveDatabases(this.databaseList.getSelectedIndices(), -1))
            for(int i = 0; i < indices.length; i++)
                indices[i]--;

        // Update the list
        updateListView();

        // Set the selected indices
        this.databaseList.setSelectedIndices(indices);
    }

    /**
     * Check whether the selected databases can be moved up.
     *
     * @return True if they can be moved up, false otherwise.
     */
    public boolean canMoveDatabasesUp() {
        // Make sure at least one database is selected
        if(getSelectedCount() <= 0)
            return false;

        // Check whether the selected databases can be moved, return the result
        return canMoveDatabases(this.databaseList.getSelectedIndices(), -1);
    }

    /**
     * Move the selected databases down.
     */
    public void moveDatabasesDown() {
        // Make sure at least one database is selected
        if(getSelectedCount() <= 0)
            return;

        // Get the indices
        int[] indices = this.databaseList.getSelectedIndices();

        // Move the databases
        if(moveDatabases(this.databaseList.getSelectedIndices(), 1))
            for(int i = 0; i < indices.length; i++)
                indices[i]++;

        // Update the list
        updateListView();

        // Set the selected indices
        this.databaseList.setSelectedIndices(indices);
    }

    /**
     * Check whether the selected databases can be moved down.
     *
     * @return True if they can be moved down, false otherwise.
     */
    public boolean canMoveDatabasesDown() {
        // Make sure at least one database is selected
        if(getSelectedCount() <= 0)
            return false;

        // Check whether the selected databases can be moved, return the result
        return canMoveDatabases(this.databaseList.getSelectedIndices(), 1);
    }

    /**
     * Move the databases.
     *
     * @param databaseIndexes Indexes of databases to move.
     * @param move How much to move.
     *
     * @return True if any item was moved, false if not.
     */
    private boolean moveDatabases(int[] databaseIndexes, int move) {
        // Check whether the databases can be moved
        if(!canMoveDatabases(databaseIndexes, move))
            return false;

        // Sort the array with indexes
        Arrays.sort(databaseIndexes);

        // Inverse the list if they should be moved upwards
        if(move > 0) {
            for(int i = 0; i < databaseIndexes.length / 2; i++) {
                int temp = databaseIndexes[i];
                databaseIndexes[i] = databaseIndexes[databaseIndexes.length - i - 1];
                databaseIndexes[databaseIndexes.length - i - 1] = temp;
            }
        }

        // Move all the databases
        for(int i : databaseIndexes)
            Collections.swap(this.databases, i, i + move);

        // Return the result
        return true;
    }

    /**
     * Check whether the specified databases can be moved.
     *
     * @param databaseIndexes Database indexes.
     * @param move Relative move.
     *
     * @return True if they can move, false if not.
     */
    private boolean canMoveDatabases(int[] databaseIndexes, int move) {
        // Get the lowest and highest new index
        int lowest = databaseIndexes[0] + move;
        int highest = databaseIndexes[0] + move;

        // Loop through the database indexes and update the lowest and highest values
        for(int i = 1; i < databaseIndexes.length; i++) {
            lowest = Math.min(databaseIndexes[i] + move, lowest);
            highest = Math.max(databaseIndexes[i] + move, highest);
        }

        // Make sure the databases can be moved to that position
        return !(lowest < 0 || highest >= this.databases.size());
    }

    /**
     * Remove the selected databases.
     */
    public void removeDatabases() {
        // Make sure at least one item is selected
        if(getSelectedCount() <= 0)
            return;

        // Ask whether the user wants to delete the databases
        switch(JOptionPane.showConfirmDialog(this, "Are you sure you'd like to delete this database?", "Delete database", JOptionPane.YES_NO_OPTION)) {
            case JOptionPane.NO_OPTION:
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                return;
        }

        // Delete the selected databases
        for(Object database : this.databaseList.getSelectedValuesList())
            //noinspection RedundantCast
            this.databases.remove((AbstractDatabase) database);

        // Refresh the list
        updateListView();
    }

    /**
     * Get the number of selected items.
     *
     * @return Number of selected items.
     */
    public int getSelectedCount() {
        return this.databaseList.getSelectedValuesList().size();
    }

    /**
     * Apply and save the databases.
     */
    public void applyDatabases() {
        // Store a copy of the databases
        Core.getInstance().getDatabaseManager().setDatabases(this.databases);

        // Create a progress dialog and save the database configuration
        ProgressDialog progress = new ProgressDialog(this, FORM_TITLE, false, "Saving database configuration...", true);

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
     * Close the frame. Ask whether the user wants to save the changes.
     */
    public void closeFrame() {
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
        // Get the database manager
        DatabaseManager databaseManager = Core.getInstance().getDatabaseManager();

        // Compare the number of databases
        if(databaseManager.getDatabaseCount() != this.databases.size())
            return true;

        // Compare the databases
        for(int i = 0; i < this.databases.size(); i++)
            if(!databaseManager.getDatabase(i).equals(this.databases.get(i)))
                return true;

        // There don't seem to be any unsaved changes, return the result
        return false;
    }
}
