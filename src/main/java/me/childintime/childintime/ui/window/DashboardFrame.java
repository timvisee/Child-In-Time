package me.childintime.childintime.ui.window;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.configuration.gui.window.DatabaseManagerDialog;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.ui.component.EntityViewComponent;
import me.childintime.childintime.database.entity.ui.dialog.EntityManagerDialog;
import me.childintime.childintime.permission.PermissionLevel;
import me.childintime.childintime.ui.component.LinkLabel;
import me.childintime.childintime.ui.window.tool.BmiToolDialog;
import me.childintime.childintime.ui.window.tool.BodyStateToolDialog;
import me.childintime.childintime.ui.window.tool.MeasurementToolDialog;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
     * Status label.
     */
    private JLabel statusLabel;

    /**
     * Constructor.
     */
    public DashboardFrame() {
        // Build the super
        super(FRAME_TITLE);

        // Build the dialog UI
        buildUI();

        // Configure the close button behaviour
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Add a window listener
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) {
                // Dispose the frame
                dispose();

                // Destroy the core
                Core.getInstance().destroy();
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

        // Configure the window sizes
        configureSize();

        // Set the dialog location to the center of the screen
        setLocationByPlatform(true);
        setLocationRelativeTo(null);

        // Bring the window to the front
        toFront();

        // Refresh everything after the dashboard is loaded
        SwingUtilities.invokeLater(this::refreshAll);
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
        setMinimumSize(new Dimension(getMinimumSize().width + 50, getMinimumSize().height + 50));
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

        // Add the main actions panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 16);
        container.add(buildUiMainActionsPanel(), c);

        // Add the main actions panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 16);
        container.add(buildUiStatisticsPanel(), c);

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
        container.add(createDashboardPanel(Core.getInstance().getMeasurementManager()), c);

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
     * Build the main actions panel.
     *
     * @return Main actions panel.
     */
    private JPanel buildUiMainActionsPanel() {
        // Create a main actions panel
        final JPanel mainActions = new JPanel();
        mainActions.setLayout(new BorderLayout());
        mainActions.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Actions"),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Create the container
        final JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Add the main label
        container.add(new JLabel("<html>" +
                "<b>Welcome to the " + App.APP_NAME + " dashboard.</b><br>"));

        // Show the BMI tool if the user has permission
        if(PermissionLevel.VIEW_ANONYMOUS.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Add the measurement label
            container.add(new JLabel("<html><br>" +
                    "Use the BMI tool to view detailed<br>body status of a student:<br>"));

            // Create and add the measurement tool link
            final LinkLabel bmiToolLink = new LinkLabel("Open BMI tool");
            bmiToolLink.addActionListener(e -> BmiToolDialog.showDialog(this));
            container.add(bmiToolLink);
        }

        // Show the measurement tool if the user has permission
        if(PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Add the measurement label
            container.add(new JLabel("<html><br>" +
                    "Use the measurement tool to create<br>new measurements for a student:<br>"));

            // Create and add the measurement tool link
            final LinkLabel measurementToolLink = new LinkLabel("Open measurement tool");
            measurementToolLink.addActionListener(e -> MeasurementToolDialog.showDialog(this));
            container.add(measurementToolLink);
        }

        // Show the body state tool if the user has permission
        if(PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Add the body state label
            container.add(new JLabel("<html><br>" +
                    "Use the body state tool to store<br>new body states for a student:<br>"));

            // Create and add the body state tool link
            final LinkLabel bodyStateToolLink = new LinkLabel("Open body state tool");
            bodyStateToolLink.addActionListener(e -> BodyStateToolDialog.showDialog(this));
            container.add(bodyStateToolLink);
        }

        // Create a scroll pane to fit larger content
        final JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        mainActions.add(scrollPane);

        // Set the preferred size
        mainActions.setMinimumSize(new Dimension(250, 0));
        mainActions.setPreferredSize(new Dimension(250, container.getPreferredSize().height));

        // Return the main actions panel
        return mainActions;
    }

    /**
     * Build the main actions panel.
     *
     * @return Main actions panel.
     */
    private JPanel buildUiStatisticsPanel() {
        // Create a main actions panel
        final JPanel statistics = new JPanel();
        statistics.setLayout(new BorderLayout());
        statistics.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Statistics"),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Create the container
        final JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Initialize the label
        this.statusLabel = new JLabel("Initializing status...");

        // Add the status label
        container.add(this.statusLabel);

        // Create an update status label callable
        final Runnable updateStatusLabel = () -> statusLabel.setText("<html>" +
                "<b>Entities:</b><br>" +
                Core.getInstance().getUserManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getUserManager().getEntityCount() + "<br>" +
                Core.getInstance().getStudentManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getStudentManager().getEntityCount() + "<br>" +
                Core.getInstance().getTeacherManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getTeacherManager().getEntityCount() + "<br>" +
                Core.getInstance().getSchoolManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getSchoolManager().getEntityCount() + "<br>" +
                Core.getInstance().getGroupManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getGroupManager().getEntityCount() + "<br>" +
                Core.getInstance().getMeasurementManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getMeasurementManager().getEntityCount() + "<br>" +
                Core.getInstance().getBodyStateManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getBodyStateManager().getEntityCount() + "<br>" +
                Core.getInstance().getParkourManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getParkourManager().getEntityCount() + "<br>" +
                Core.getInstance().getSportManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getSportManager().getEntityCount() + "<br>" +
                "<br>" +
                "<b>Couples:</b><br>" +
                Core.getInstance().getGroupTeacherCoupleManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getGroupTeacherCoupleManager().getEntityCount() + "<br>" +
                Core.getInstance().getStudentSportCoupleManager().getManifest().getTypeName(true, true) + ": " + Core.getInstance().getStudentSportCoupleManager().getEntityCount() + "<br>"
        );

        // Update the status label when the managers change
        Core.getInstance().getUserManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getStudentManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getTeacherManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getSchoolManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getGroupManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getMeasurementManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getBodyStateManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getParkourManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getSportManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getGroupTeacherCoupleManager().addChangeListener(updateStatusLabel::run);
        Core.getInstance().getStudentSportCoupleManager().addChangeListener(updateStatusLabel::run);

        // Update the label for the first time
        updateStatusLabel.run();

        // Create a scroll pane to fit larger content
        final JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        statistics.add(scrollPane);

        // Set the preferred size
        statistics.setMinimumSize(new Dimension(250, 0));
        statistics.setPreferredSize(new Dimension(250, container.getPreferredSize().height));

        // Return the main actions panel
        return statistics;
    }

    /**
     * Build the menu bar for this frame.
     */
    public void buildUiMenu() {
        // Create the menu
        MenuBar menu = new MenuBar();

        // Create the dashboard menu
        Menu dashboardMenu = new Menu("Dashboard");

        // Create the change user action
        MenuItem changeUserAction = new MenuItem("Change user...");
        // TODO: An action placeholder is used, assign a proper action!
        changeUserAction.addActionListener(e -> restart());
        dashboardMenu.add(changeUserAction);

        // Create the change database action
        MenuItem changeDatabaseAction = new MenuItem("Change database...");
        // TODO: An action placeholder is used, assign a proper action!
        changeDatabaseAction.addActionListener(e -> restart());
        dashboardMenu.add(changeDatabaseAction);
        dashboardMenu.addSeparator();

        // Create exit action
        MenuItem exitAction = new MenuItem("Exit");
        exitAction.addActionListener(e -> exit());
        dashboardMenu.add(exitAction);

        // Add the dashboard menu to the menu bar
        menu.add(dashboardMenu);

        // Create the edit menu
        Menu editMenu = new Menu("Edit");

        // Create the database manager action
        MenuItem databaseManagerAction = new MenuItem("Databases...");
        databaseManagerAction.addActionListener(e -> new DatabaseManagerDialog(this, true));
        editMenu.add(databaseManagerAction);
        editMenu.addSeparator();

        // Create the refresh all action
        MenuItem refreshAllAction = new MenuItem("Refresh all");
        refreshAllAction.addActionListener(e -> refreshAll());
        editMenu.add(refreshAllAction);

        // Add the edit menu to the menu bar
        menu.add(editMenu);

        // Create the entity menu
        Menu entityMenu = new Menu("Entity");
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getUserManager()));
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getStudentManager()));
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getTeacherManager()));
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getSchoolManager()));
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getGroupManager()));
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getMeasurementManager()));
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getBodyStateManager()));
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getParkourManager()));
        entityMenu.add(buildUiMenuEntity(Core.getInstance().getSportManager()));

        // Create the advanced menu
        Menu advancedMenu = new Menu("Advanced");
        advancedMenu.add(buildUiMenuEntity(Core.getInstance().getGroupTeacherCoupleManager()));
        advancedMenu.add(buildUiMenuEntity(Core.getInstance().getStudentSportCoupleManager()));

        // Add the advanced menu
        entityMenu.addSeparator();
        entityMenu.add(advancedMenu);

        // Add the entity menu to the menu bar
        menu.add(entityMenu);

        // Create the tools menu
        Menu toolsMenu = new Menu("Tools");

        // Create the BMI tool menu item
        MenuItem bmiToolAction = new MenuItem("BMI analysis tool");
        bmiToolAction.addActionListener(e -> BmiToolDialog.showDialog(this));
        bmiToolAction.setEnabled(PermissionLevel.VIEW_ANONYMOUS.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel()));
        toolsMenu.add(bmiToolAction);
        toolsMenu.addSeparator();

        // Create the measurement tool menu item
        MenuItem measurementToolAction = new MenuItem("Measurement tool");
        measurementToolAction.addActionListener(e -> MeasurementToolDialog.showDialog(this));
        measurementToolAction.setEnabled(PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel()));
        toolsMenu.add(measurementToolAction);

        // Create the body state tool menu item
        MenuItem bodyStateToolAction = new MenuItem("Body state tool");
        bodyStateToolAction.addActionListener(e -> BodyStateToolDialog.showDialog(this));
        bodyStateToolAction.setEnabled(PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel()));
        toolsMenu.add(bodyStateToolAction);

        // Add the tools menu to the menu bar
        menu.add(toolsMenu);

        // Create the help menu
        Menu helpMenu = new Menu("Help");

        // Create the about action
        MenuItem aboutAction = new MenuItem("About");
        aboutAction.addActionListener(e -> about());
        helpMenu.add(aboutAction);

        // Add the help menu to the menu bar
        menu.add(helpMenu);

        // Set the menu
        setMenuBar(menu);
    }

    /**
     * Build a menu for the given entity.
     *
     * @param manager Entity manager to create the menu for.
     *
     * @return Entity menu.
     */
    private Menu buildUiMenuEntity(AbstractEntityManager manager) {
        // Create the base menu
        Menu menu = new Menu(manager.getManifest().getTypeName(true, true));

        // Get the manifest
        final AbstractEntityManifest manifest = manager.getManifest();

        // Add a manage action
        MenuItem manageAction = new MenuItem("Manage " + manager.getManifest().getTypeName(false, true) + "...");
        manageAction.addActionListener(e -> manifest.showManagerDialog(this));
        menu.add(manageAction);

        // Add a create action
        MenuItem createAction = new MenuItem("Create " + manager.getManifest().getTypeName(false, false) + "...");
        createAction.addActionListener(e -> manifest.showCreateDialog(this));
        createAction.setEnabled(PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel()));
        menu.add(createAction);
        menu.addSeparator();

        // Add a refresh action
        MenuItem refreshAction = new MenuItem("Refresh " + manager.getManifest().getTypeName(false, true));
        refreshAction.addActionListener(e -> manager.refresh());
        menu.add(refreshAction);

        // Return the menu
        return menu;
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
        refreshButton.addActionListener(e -> list.refresh());

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
     * Refresh all managers.
     */
    // TODO: Improve this method!
    private void refreshAll() {
        // Store the visibility state of the progress dialog
        final boolean progressDialogVisible = Core.getInstance().getProgressDialog().isVisible();

        // Show the progress dialog and set the state
        Core.getInstance().getProgressDialog().setVisible(true);
        Core.getInstance().getProgressDialog().setStatus("Refreshing...");
        // TODO: Show a progress bar!

        // Refresh all managers
        Core.getInstance().getStudentManager().refresh();
        Core.getInstance().getTeacherManager().refresh();
        Core.getInstance().getSchoolManager().refresh();
        Core.getInstance().getGroupManager().refresh();
        Core.getInstance().getMeasurementManager().refresh();
        Core.getInstance().getBodyStateManager().refresh();
        Core.getInstance().getParkourManager().refresh();
        Core.getInstance().getUserManager().refresh();
        Core.getInstance().getGroupTeacherCoupleManager().refresh();
        Core.getInstance().getSportManager().refresh();
        Core.getInstance().getStudentSportCoupleManager().refresh();

        // Revert the visibility state of the progress dialog
        Core.getInstance().getProgressDialog().setVisible(progressDialogVisible);
    }

    /**
     * Show the about dialog.
     */
    private void about() {
        new AboutDialog(this, true);
    }

    /**
     * Restart the application.
     */
    // TODO: Properly restart, don't just destroy and reinitialize!
    private void restart() {
        // Dispose the current frame
        dispose();

        // Destroy the core
        Core.getInstance().destroy();

        // Initialize the core again
        Core.getInstance().init();
    }

    /**
     * Exit the application.
     */
    private void exit() {
        // Dispose the dashboard frame
        dispose();

        // Destroy the application core
        Core.getInstance().destroy();
    }
}
