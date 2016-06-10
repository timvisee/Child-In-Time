package me.childintime.childintime.ui.window;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.ui.component.EntityViewComponent;
import me.childintime.childintime.database.entity.ui.dialog.EntityManagerDialog;

import javax.swing.*;
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

        // Link all components
        linkComponents();

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
        Core.getInstance().getStudentManager().fetchObjects();
        Core.getInstance().getTeacherManager().fetchObjects();
        Core.getInstance().getSchoolManager().fetchObjects();

        // Create the student panel
        JPanel studentPanel = new JPanel();
        studentPanel.setBorder(BorderFactory.createTitledBorder("Students"));
        studentPanel.add(this.studentButton = new JButton("Students"));

        // Create the teacher panel
        JPanel teacherPanel = new JPanel();
        teacherPanel.setBorder(BorderFactory.createTitledBorder("Teachers"));
        teacherPanel.add(this.teacherButton = new JButton("Teachers"));

        // Create the school panel
        JPanel schoolPanel = new JPanel();
        schoolPanel.setBorder(BorderFactory.createTitledBorder("Schools"));
        schoolPanel.add(this.schoolButton = new JButton("Schools"));

        // Add student list
        EntityViewComponent listStudents = new EntityViewComponent(Core.getInstance().getStudentManager());
        listStudents.setPreferredSize(new Dimension(200, 200));
        studentPanel.add(listStudents);

        // Add teacher list
        EntityViewComponent listTeachers = new EntityViewComponent(Core.getInstance().getTeacherManager());
        listTeachers.setPreferredSize(new Dimension(200, 200));
        teacherPanel.add(listTeachers);

        // Add school list
        EntityViewComponent listSchools = new EntityViewComponent(Core.getInstance().getSchoolManager());
        listSchools.setPreferredSize(new Dimension(200, 200));
        schoolPanel.add(listSchools);

        // Add the student panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        container.add(studentPanel, c);

        // Add the teacher panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        container.add(teacherPanel, c);

        // Add the school panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        container.add(schoolPanel, c);

        // Add the container to the dialog
        add(container);

        // Pack everything
        pack();
    }

    /**
     * Link all action listeners to it's components.
     */
    private void linkComponents() {
        // Link the buttons to their managers
        this.studentButton.addActionListener(e -> new EntityManagerDialog(this, Core.getInstance().getStudentManager(), true));
        this.teacherButton.addActionListener(e -> new EntityManagerDialog(this, Core.getInstance().getTeacherManager(), true));
        this.schoolButton.addActionListener(e -> new EntityManagerDialog(this, Core.getInstance().getSchoolManager(), true));

//        // Store the current instance
//        final LoginDialog instance = this;
//
//        // Create a runnable for the close action
//        Runnable closeAction = () -> {
//            // Set the success status flag
//            instance.success = false;
//
//            // Dispose the dialog
//            instance.dispose();
//        };
//
//        // Add an action to the configure button
//        this.configureButton.addActionListener(e -> {
//            // Show the database manager form
//            new DatabaseManagerDialog(instance, true);
//
//            // Get the selected combo box value
//            AbstractDatabase selected = getSelectedDatabase();
//
//            // Reset the combo box data model
//            comboBox.setModel(new DefaultComboBoxModel<>(Core.getInstance().getDatabaseManager().getDatabases().toArray(new AbstractDatabase[] {})));
//
//            // Set the selected value to it's original
//            comboBox.setSelectedItem(selected);
//        });
//
//        // Add an action to the continue button
//        continueButton.addActionListener(e -> {
//            // Validate the user input and set the success status flag
//            instance.success = check();
//
//            // Dispose the dialog if the user input is valid
//            if(instance.success)
//                instance.dispose();
//        });
//
//        // Add an action to the quit button
//        quitButton.addActionListener(e -> {
//            // Run the close action
//            closeAction.run();
//        });
//
//        // Handle window close events
//        instance.addWindowListener(new WindowListener() {
//            @Override
//            public void windowOpened(WindowEvent e) { }
//
//            @Override
//            public void windowClosing(WindowEvent e) {
//                // Run the close action
//                closeAction.run();
//            }
//
//            @Override
//            public void windowClosed(WindowEvent e) { }
//
//            @Override
//            public void windowIconified(WindowEvent e) { }
//
//            @Override
//            public void windowDeiconified(WindowEvent e) { }
//
//            @Override
//            public void windowActivated(WindowEvent e) { }
//
//            @Override
//            public void windowDeactivated(WindowEvent e) { }
//        });
    }
}
