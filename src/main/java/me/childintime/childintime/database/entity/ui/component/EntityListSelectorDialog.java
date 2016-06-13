package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.util.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class EntityListSelectorDialog extends JDialog {

    /**
     * Manager instance.
     */
    private final AbstractEntityManager manager;

    /**
     * List selector component.
     */
    private EntityListSelectorComponent list;

    /**
     * Defines whether the selection is discarded.
     */
    private boolean discarded = true;

    /**
     * Select button instance.
     */
    private JButton selectButton;

    /**
     * Constructor.
     *
     * @param owner Owner window.
     * @param manager Entity manager.
     * @param title Dialog title.
     */
    public EntityListSelectorDialog(Window owner, AbstractEntityManager manager, String title) {
        // Construct the super
        super(owner, title);

        // Set the manager instance
        this.manager = manager;

        // Set the closing mode of the window
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Build the dialog UI
        buildUi();

        // Configure the frame size
        configureSize();

        // Make the dialog modal
        setModalityType(ModalityType.APPLICATION_MODAL);
        setModal(true);

        // Set the location relative to the owner
        setLocationRelativeTo(owner);

        // Create a world close listener
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) {
                // Close the dialog
                close();
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

        // Create a listener when an entity is double clicked
        this.list.addEntityActionListener(entities -> select());

        // Update the IU buttons
        updateUiButtons();
    }

    /**
     * Show the selection dialog.
     *
     * @param owner Owner window.
     * @param manager Entity manager.
     *
     * @return The selected abstract entity, or null.
     */
    public static AbstractEntity showDialog(Window owner, AbstractEntityManager manager) {
        return showDialog(owner, manager, null);
    }

    /**
     * Show the selection dialog.
     *
     * @param owner Owner window.
     * @param manager Entity manager.
     * @param selected The selected entity, or null.
     *
     * @return The selected abstract entity, or null.
     */
    public static AbstractEntity showDialog(Window owner, AbstractEntityManager manager, AbstractEntity selected) {
        // Create the dialog
        EntityListSelectorDialog dialog = new EntityListSelectorDialog(owner, manager, "Select a " + manager.getManifest().getTypeName(false, false) + "...");

        // Select the proper entity
        dialog.list.setSelectedItem(selected);

        // Show the dialog
        dialog.setVisible(true);

        // Return null if the dialog was closed
        if(dialog.discarded)
            return null;

        // Return the selected entity
        return dialog.list.getSelectedItem();
    }

    /**
     * Properly configure the window sizes for the current content.
     */
    private void configureSize() {
        // Pack everything
        pack();

        // Get the packed width and height
        final int width = getSize().width;
        final int height = getSize().height;

        // Configure the sizes
        setMinimumSize(new Dimension((int) (width / 1.5), height / 2));
        setPreferredSize(new Dimension((int) (width / 1.5), (int) (height / 1.5)));
        setSize(new Dimension((int) (width / 1.5), (int) (height / 1.5)));
    }

    /**
     * Build the dialog UI.
     */
    public void buildUi() {
        // Create a grid bag constraints instance
        GridBagConstraints c = new GridBagConstraints();

        // Set the dialog layout
        setLayout(new BorderLayout());

        // Create a container panel
        JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Create the list
        // TODO: Define the default selected item!
        this.list = new EntityListSelectorComponent(this.manager);

        // Update the UI buttons when the selection changes
        this.list.addSelectionChangeListenerListener(this::updateUiButtons);

        // Add the list to the container
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 1;
        container.add(this.list, c);

        // Create and add the commit buttons panel
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(8, 32, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        container.add(buildUiCommitButtons(), c);

        // Create the create button
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> create());

        // Add the create button
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(8, 0, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        container.add(createButton, c);

        // Add the container to the dialog
        add(container, BorderLayout.CENTER);
    }

    /**
     * Create a commit buttons panel.
     *
     * @return Commit buttons panel.
     */
    public JPanel buildUiCommitButtons() {
        // Create a commit button panel
        final JPanel commitButtonPanel = new JPanel(new GridLayout(1, 2, 8, 8));

        // Create the buttons
        this.selectButton = new JButton("Select");
        final JButton closeButton = new JButton("Close");
        this.selectButton.addActionListener(e -> select());
        closeButton.addActionListener(e -> close());

        // Add the buttons, reverse the order on Mac OS X
        if(!Platform.isMacOsX()) {
            commitButtonPanel.add(this.selectButton);
            commitButtonPanel.add(closeButton);
        } else {
            commitButtonPanel.add(closeButton);
            commitButtonPanel.add(this.selectButton);
        }

        // Return the button panel
        return commitButtonPanel;
    }

    /**
     * Update the UI buttons.
     */
    public void updateUiButtons() {
        // Enable the select button if one entity is selected
        this.selectButton.setEnabled(this.list.getSelectedCount() == 1);
    }

    /**
     * Create an entity.
     */
    public void create() {
        // Create an entity
        AbstractEntity entity = this.list.createEntity();

        // Make sure an entity was created
        if(entity == null)
            return;

        // Select the newly created entity
        this.list.setSelectedItem(entity);
    }

    /**
     * Select the currently selected.
     */
    public void select() {
        // Make sure an entity is selected
        if(this.list.getSelectedCount() == 0) {
            // Show a warning and return
            JOptionPane.showMessageDialog(this, "Please select an entity.", "No entity selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Set the discarded flag
        this.discarded = false;

        // Dispose the dialog
        dispose();
    }

    /**
     * Dispose the frame.
     */
    public void close() {
        // Dispose the frame
        dispose();
    }
}
