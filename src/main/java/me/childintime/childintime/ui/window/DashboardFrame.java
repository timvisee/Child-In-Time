package me.childintime.childintime.ui.window;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.configuration.gui.window.DatabaseManagerDialog;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.ui.component.EntityViewComponent;
import me.childintime.childintime.database.entity.ui.dialog.EntityManagerDialog;
import me.childintime.childintime.util.Platform;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {

    /**
     * Unique serial version.
     */
    private static final long serialVersionUID = 9067983491873285740L;

    /**
     * Frame title.
     */
    public static final String FRAME_TITLE = App.APP_NAME + " - Dashboard";

    /**
     * Constructor.
     */
    public DashboardFrame() {
        // Build the super
        super(FRAME_TITLE);

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
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 0));
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

        // Create a main actions panel
        JPanel statistics = new JPanel();
        statistics.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statistics.add(new JLabel("Some statistics should be shown here!"));

        // Add the main actions panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 16);
        container.add(mainActions, c);

        // Add the main actions panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 16);
        container.add(statistics, c);

        // Add the student panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 16);
        container.add(createDashboardPanel(Core.getInstance().getStudentManager()), c);

        // Add the student panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 16);
        container.add(createDashboardPanel(Core.getInstance().getTeacherManager()), c);

        // Add the student panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 16);
        container.add(createDashboardPanel(Core.getInstance().getParkourManager()), c);

        // Add the student panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 16);
        container.add(createDashboardPanel(Core.getInstance().getBodyStateManager()), c);

        // Add the container to the dialog
        add(container);

        // Build the menu bar
        buildUiMenu();

        // Pack everything
        pack();
    }

    /**
     * Build the menu bar for this frame.
     */
    public void buildUiMenu() {
        // Create the menu
        MenuBar menu = new MenuBar();

        // Create the file menu
        Menu fileMenu = new Menu("File");

        // Create the database manager action
        MenuItem databaseManagerAction = new MenuItem("Manage databases");
        databaseManagerAction.addActionListener(e -> new DatabaseManagerDialog(this, true));
        fileMenu.add(databaseManagerAction);
        fileMenu.addSeparator();

        // Create exit action
        MenuItem exitAction = new MenuItem("Exit");
        exitAction.addActionListener(e -> exit());
        fileMenu.add(exitAction);

        // Add the file menu to the menu bar
        menu.add(fileMenu);

        // Create the help menu
        Menu helpMenu = new Menu("Help");

        // Create the about action
        MenuItem aboutAction = new MenuItem("About");
        aboutAction.addActionListener(e -> new AboutDialog(this, true));
        helpMenu.add(aboutAction);

        // Add the help menu to the menu bar
        menu.add(helpMenu);

        // Set the menu
        setMenuBar(menu);



        // TODO: Toolbar test

        // Toolbar button
        JButton myButton = new JButton("Test");
        myButton.setIcon(new ImageIcon(this.getClass().getResource("/com/toedter/calendar/images/JDateChooserColor16.gif")));

        // Toolbar
        JToolBar tb = new JToolBar();
        tb.add(myButton);

        if(Platform.isMacOsX())
            tb.setBackground(new Color(237, 237, 237));

        add(tb, BorderLayout.PAGE_START);
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

    /**
     * Exit the application.
     */
    public void exit() {
        // Dispose the dashboard frame
        dispose();

        // Destroy the application core
        Core.getInstance().destroy();
    }
}
