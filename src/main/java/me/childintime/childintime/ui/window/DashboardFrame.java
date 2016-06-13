package me.childintime.childintime.ui.window;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.ui.component.EntityViewComponent;
import me.childintime.childintime.database.entity.ui.dialog.EntityManagerDialog;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {

    /**
     * Unique serial version.
     */
    private static final long serialVersionUID = 9067983491873285740L;

    /**
     * Student button instance.
     */
    private JButton studentButton;

    /**
     * Teacher button instance.
     */
    private JButton teacherButton;

    /**
     * School button instance.
     */
    private JButton schoolButton;

    /**
     * Constructor.
     *
     * @param title Window title.
     */
    public DashboardFrame(String title) {
        // Build the super
        super(title);

        // Build the dialog UI
        buildUI();

        // Configure the close button behaviour
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configure the window sizes
        configureSize();

        // Set the dialog location to the center of the screen
        setLocationByPlatform(true);
        setLocationRelativeTo(null);

        // Bring the window to the front
        toFront();
    }

    /**
     * Properly configure the window sizes for the current content.
     */
    private void configureSize() {
        // Pack everything
        pack();

        // Get the maximum height
        final int maxHeight = getSize().height;

        // Configure the sizes
        setMinimumSize(new Dimension(getMinimumSize()));
        setMaximumSize(new Dimension(9999, maxHeight));
        setPreferredSize(new Dimension(getSize().width + 100, maxHeight));
        setSize(new Dimension(getSize().width + 100, maxHeight));
    }

    /**
     * Build the progress dialog UI.
     */
    private void buildUI() {
        // Create the base panel
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        container.setLayout(new GridBagLayout());

        // Create a grid bag constraints instance
        GridBagConstraints c = new GridBagConstraints();

        // TODO: Move this somewhere else
        Core.getInstance().getStudentManager().fetchEntities();
        Core.getInstance().getTeacherManager().fetchEntities();
        Core.getInstance().getSchoolManager().fetchEntities();
        Core.getInstance().getGroupManager().fetchEntities();
        Core.getInstance().getMeasurementManager().fetchEntities();
        Core.getInstance().getBodyStateManager().fetchEntities();

        // Create a main actions panel
        JPanel mainActions = new JPanel();
        mainActions.setBorder(BorderFactory.createTitledBorder("Actions"));
        mainActions.add(new JLabel("Main actions should be shown here!"));

        // Add the main actions panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        container.add(mainActions, c);

        // Add the student panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        container.add(createDashboardPanel(Core.getInstance().getStudentManager()), c);

        // Add the student panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        container.add(createDashboardPanel(Core.getInstance().getTeacherManager()), c);

        // Add the student panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        //container.add(createDashboardPanel(Core.getInstance().getMeasurementManager()), c);

        // Add the student panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        container.add(createDashboardPanel(Core.getInstance().getBodyStateManager()), c);

        // Add the container to the dialog
        add(container);

        // Pack everything
        pack();
    }

    /**
     * Create a dashboard panel for the given manager.
     *
     * @param manager Manager to create a dashboard panel for.
     *
     * @return Dashboard panel.
     */
    private JPanel createDashboardPanel(AbstractEntityManager manager) {
        // Create a grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Create a container
        final JPanel container = new JPanel(new GridBagLayout());

        // Create a titled border
        container.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(manager.getManifest().getTypeName(true, true)),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Create the list
        final EntityViewComponent list = new EntityViewComponent(manager);
        list.setPreferredSize(new Dimension(200, 200));

        // Create the button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 8, 8));

        // Create the buttons
        final JButton manageButton = new JButton("Manage");
        final JButton refreshButton = new JButton("Refresh");

        // Create the button action listeners
        manageButton.addActionListener(e -> new EntityManagerDialog(this, list.getManager(), true));
        refreshButton.addActionListener(e -> list.getManager().refresh());

        // Add the buttons
        buttonPanel.add(manageButton);
        buttonPanel.add(refreshButton);

        // Add the list to the container
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        container.add(list, c);

        // Create and add the button panel to the container
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 0, 0, 8);
        container.add(buttonPanel, c);

        // Return the container
        return container;
    }
}
