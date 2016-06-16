package me.childintime.childintime.database.entity.ui.dialog;

import me.childintime.childintime.App;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.ui.component.EntityManagerComponent;

import javax.swing.*;
import java.awt.*;

public class EntityManagerDialog extends JDialog {

    /**
     * The entity manager this dialog is used for.
     */
    private AbstractEntityManager manager;

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
        mainPanel.add(buildUiManager(), c);

        // Add the commit buttons panel
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10, 10, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        mainPanel.add(buildUiCommitButtons(), c);

        // Add the main panel to the frame
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(10, 10, 10, 10);
        this.add(mainPanel, c);
    }

    /**
     * Build the object management panel.
     *
     * @return Object management panel.
     */
    private JComponent buildUiManager() {
        // Create a container
        final JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createTitledBorder(this.manager.getManifest().getTypeName(true, true)));

        // Create the entity manager
        final EntityManagerComponent entityManager = new EntityManagerComponent(this.manager);

        // Hide the 'open in manager' action
        entityManager.getEntityView().setShowManagerAction(false);

        // Add the entity manager
        container.add(entityManager, BorderLayout.CENTER);

        // Return the container
        return container;
    }

    /**
     * Create the commit buttons panel to control the form.
     *
     * @return Button panel.
     */
    private JPanel buildUiCommitButtons() {
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
}
