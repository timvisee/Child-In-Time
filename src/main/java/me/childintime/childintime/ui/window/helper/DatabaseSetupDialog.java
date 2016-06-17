package me.childintime.childintime.ui.window.helper;

import me.childintime.childintime.App;
import me.childintime.childintime.database.DatabaseBuilder;
import me.childintime.childintime.database.connector.DatabaseConnector;
import me.childintime.childintime.util.Platform;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.sql.SQLException;

public class DatabaseSetupDialog extends JDialog {

    /**
     * Database setup.
     */
    public static final String DIALOG_TITLE = App.APP_NAME + " - Database setup";

    /**
     * Database connector.
     */
    private DatabaseConnector connector;

    /**
     * Default user name field.
     */
    private JTextField defaultUser;

    /**
     * Default password field.
     */
    private JPasswordField defaultPass;

    /**
     * Sample data panel.
     */
    private JPanel sampleDataPanel;

    /**
     * Checkbox which defines whether to generate sample data.
     */
    private JCheckBox generateSampleDataCheckbox;

    /**
     * Spinner field for the number of students.
     */
    private JSpinner studentCountField;

    /**
     * Spinner field for the number of teachers.
     */
    private JSpinner teacherCountField;

    /**
     * Spinner field for the number of schools.
     */
    private JSpinner schoolCountField;

    /**
     * Spinner field for the number of groups.
     */
    private JSpinner groupCountField;

    /**
     * Spinner field for the number of measurements.
     */
    private JSpinner measurementCountField;

    /**
     * Spinner field for the number of body states.
     */
    private JSpinner bodyStateCountField;

    /**
     * Spinner field for the number of parkours.
     */
    private JSpinner parkourCountField;

    /**
     * Spinner field for the number of sports.
     */
    private JSpinner sportCountField;

    /**
     * Spinner field for the number of group/teacher couples.
     */
    private JSpinner groupTeacherField;

    /**
     * Spinner field for the number of student/sport couples.
     */
    private JSpinner studentSportField;

    /**
     * Setup button.
     */
    private JButton setupButton;

    /**
     * Cancel button.
     */
    private JButton cancelButton;

    /**
     * Defaults button.
     */
    private JButton defaultsButton;

    /**
     * Check whether the dialog was cancelled.
     */
    private boolean cancelled = true;

    /**
     * Constructor.
     *
     * @param owner Window owner.
     */
    public DatabaseSetupDialog(Window owner, DatabaseConnector connector) {
        // Construct the super
        super(owner, DIALOG_TITLE);

        // Set the connector
        this.connector = connector;

        // Set the default close operation
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Make the dialog modal
        setModal(true);

        // Build the dialog UI
        buildUi();

        // Configure the frame size
        configureSize();

        // Set the location
        setLocationRelativeTo(owner);

        // Update the sample data panel state
        updateUiSampleDataPanelState();
    }

    /**
     * Properly configure the window sizes for the current content.
     */
    private void configureSize() {
        // Reset the sizes
        setMinimumSize(null);
        setPreferredSize(null);
        setMaximumSize(null);

        // Pack everything
        pack();

        // Get the height
        final int fixedHeight = getSize().height;

        // Configure the sizes
        setMinimumSize(new Dimension(getSize()));
        setMaximumSize(new Dimension(9999, fixedHeight));
        setPreferredSize(getSize());
    }

    /**
     * Show the dialog.
     *
     * @param owner Owner window.
     *
     * @return True if succeed, false if cancelled.
     */
    public static boolean showDialog(Window owner, DatabaseConnector connector) {
        // Create the dialog
        final DatabaseSetupDialog dialog = new DatabaseSetupDialog(owner, connector);

        // Show the dialog
        dialog.setVisible(true);

        // Check whether the dialog was cancelled
        return !dialog.cancelled;
    }

    /**
     * Build the dialog UI.
     */
    private void buildUi() {
        // Create a grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Create a container panel
        final JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Create the main label
        final JLabel mainLabel = new JLabel("<html>" +
                "The database you're trying to connect to is empty.<br>" +
                "Please fill in the fields below so we can set a database up for you."
        );

        // Add the main label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);
        container.add(mainLabel, c);

        // Create a wizard panel
        final JPanel defaultUserPanel = new JPanel(new GridBagLayout());
        defaultUserPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Default user"),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Build the description label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 16, 0);
        defaultUserPanel.add(new JLabel("<html>" +
                "Please enter the preferred credentials for the administrative user."
        ), c);

        // Create the default user and password fields
        this.defaultUser = new JTextField("admin");
        this.defaultPass = new JPasswordField();

        // Add the user field label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);
        defaultUserPanel.add(new JLabel("User:"), c);

        // Add the user field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 16, 0, 0);
        defaultUserPanel.add(this.defaultUser, c);

        // Add the password field label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 0, 0, 0);
        defaultUserPanel.add(new JLabel("Password:"), c);

        // Add the password field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 16, 0, 0);
        defaultUserPanel.add(this.defaultPass, c);

        // Add the default user panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(16, 0, 0, 0);
        container.add(defaultUserPanel, c);

        // Create a sample data panel
        this.sampleDataPanel = new JPanel(new GridBagLayout());
        this.sampleDataPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Sample data"),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Create the generate sample data checkbox
        this.generateSampleDataCheckbox = new JCheckBox("Generate sample data", true);
        this.generateSampleDataCheckbox.addActionListener(e -> updateUiSampleDataPanelState());

        // Add the generate sample data checkbox
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);
        this.sampleDataPanel.add(this.generateSampleDataCheckbox, c);

        // Build the count fields
        this.studentCountField = new JSpinner(new SpinnerNumberModel(100, 1, 99999, 1));
        this.teacherCountField = new JSpinner(new SpinnerNumberModel(10, 1, 99999, 1));
        this.schoolCountField = new JSpinner(new SpinnerNumberModel(5, 1, 99999, 1));
        this.groupCountField = new JSpinner(new SpinnerNumberModel(10, 1, 99999, 1));
        this.measurementCountField = new JSpinner(new SpinnerNumberModel(100, 0, 99999, 1));
        this.bodyStateCountField = new JSpinner(new SpinnerNumberModel(100, 0, 99999, 1));
        this.parkourCountField = new JSpinner(new SpinnerNumberModel(3, 1, 99999, 1));
        this.sportCountField = new JSpinner(new SpinnerNumberModel(3, 0, 99999, 1));
        this.groupTeacherField = new JSpinner(new SpinnerNumberModel(15, 0, 99999, 1));
        this.studentSportField = new JSpinner(new SpinnerNumberModel(100, 0, 99999, 1));

        // Build the description label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 0, 8, 0);
        this.sampleDataPanel.add(new JLabel("<html>" +
                "Please define the number of entities and entity couples to generate."
        ), c);

        // Build the group labels
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 0, 0, 0);
        this.sampleDataPanel.add(new JLabel("<html><b>Entities:</b>"), c);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 32, 0, 0);
        this.sampleDataPanel.add(new JLabel("<html><b>Couples:</b>"), c);

        // Build the entity fields
        int row = 3;
        buildUiCountField(this.sampleDataPanel, row++, 0, "Students", this.studentCountField);
        buildUiCountField(this.sampleDataPanel, row++, 0, "Teachers", this.teacherCountField);
        buildUiCountField(this.sampleDataPanel, row++, 0, "Schools", this.schoolCountField);
        buildUiCountField(this.sampleDataPanel, row++, 0, "Groups", this.groupCountField);
        buildUiCountField(this.sampleDataPanel, row++, 0, "Measurements", this.measurementCountField);
        buildUiCountField(this.sampleDataPanel, row++, 0, "Body states", this.bodyStateCountField);
        buildUiCountField(this.sampleDataPanel, row++, 0, "Parkours", this.parkourCountField);
        buildUiCountField(this.sampleDataPanel, row++, 0, "Sports", this.sportCountField);

        // Build the entity couple fields
        row = 3;
        buildUiCountField(this.sampleDataPanel, row++, 1, "Group & teacher couples", this.groupTeacherField);
        buildUiCountField(this.sampleDataPanel, row++, 1, "Student & sport couples", this.studentSportField);

        // Add the sample data panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(16, 0, 0, 0);
        container.add(this.sampleDataPanel, c);

        // Add the button panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(16, 0, 0, 0);
        container.add(buildUiButtonPanel(), c);

        // Add the container to the dialog
        setLayout(new BorderLayout());
        add(container);
    }

    /**
     * Build a specific field.
     *
     * @param container Container to put the field in.
     * @param layoutRow Row to put the field on.
     * @param fieldLabel Field label (without column).
     * @param field The field to add.
     */
    private void buildUiCountField(JPanel container, int layoutRow, int layoutColumn, String fieldLabel, JSpinner field) {
        // Create a grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Add the field label
        c.fill = GridBagConstraints.NONE;
        c.gridx = layoutColumn * 2;
        c.gridy = layoutRow;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, Math.min(layoutColumn, 1) * 32, 0, 0);
        container.add(new JLabel(fieldLabel + ":"), c);

        // Add the field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = layoutColumn * 2 + 1;
        c.gridy = layoutRow;
        c.weightx = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 8, 0, 0);
        container.add(field, c);
    }

    /**
     * Build the commit buttons panel.
     *
     * @return Commit buttons panel.
     */
    private JPanel buildUiButtonPanel() {
        // Create the gird bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Create a new panel
        final JPanel buttonPanel = new JPanel(new GridBagLayout());

        // Create the commit buttons panel
        final JPanel commitButtonsPanel = new JPanel(new GridLayout(1, 2, 8, 8));

        // Create the buttons
        this.defaultsButton = new JButton("Defaults");
        this.setupButton = new JButton("Setup");
        this.cancelButton = new JButton("Cancel");

        // Link the button actions
        this.defaultsButton.addActionListener(e -> defaults());
        this.setupButton.addActionListener(e -> setup());
        this.cancelButton.addActionListener(e -> dispose());

        // Add the buttons to the panel
        if(!Platform.isMacOsX()) {
            commitButtonsPanel.add(this.setupButton);
            commitButtonsPanel.add(this.cancelButton);
        } else {
            commitButtonsPanel.add(this.setupButton);
            commitButtonsPanel.add(this.cancelButton);
        }

        // Add the defaults button
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);
        buttonPanel.add(this.defaultsButton, c);

        // Add the commit buttons
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 32, 0, 0);
        buttonPanel.add(commitButtonsPanel, c);

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Update the sample panel data state.
     */
    public void updateUiSampleDataPanelState() {
        // Check whether sample data generate is enabled
        final boolean generateData = this.generateSampleDataCheckbox.isSelected();

        // Update the state of the inner components
        for(Component child : this.sampleDataPanel.getComponents())
            child.setEnabled(generateData);

        // Make sure the checkbox is enabled
        this.generateSampleDataCheckbox.setEnabled(true);
    }

    /**
     * Revert all fields to their defaults.
     */
    public void defaults() {
        // Set the default user and clear the password
        this.defaultUser.setText("admin");
        this.defaultPass.setText("");

        // Set the default count values
        studentCountField.setValue(100);
        teacherCountField.setValue(10);
        schoolCountField.setValue(5);
        groupCountField.setValue(10);
        measurementCountField.setValue(100);
        bodyStateCountField.setValue(100);
        parkourCountField.setValue(3);
        sportCountField.setValue(3);
        groupTeacherField.setValue(15);
        studentSportField.setValue(100);

        // Set the checkbox
        this.generateSampleDataCheckbox.setSelected(true);

        // Update the panel state
        updateUiSampleDataPanelState();
    }

    /**
     * Validate the input.
     *
     * @return True on success, false on failure.
     */
    public boolean validateInput() {
        // Make sure the password is at least one character long
        if(this.defaultUser.getText().length() == 0) {
            // Show a message dialog
            JOptionPane.showMessageDialog(this, "Please enter a preferred username for the administrative user.", "Invalid username", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Make sure the password is at least one character long
        if(this.defaultPass.getPassword().length == 0) {
            // Show a message dialog
            JOptionPane.showMessageDialog(this, "Please enter a preferred password for the administrative user.", "Invalid password", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // We're done validating if the user doesn't want to generate sample data
        if(!this.generateSampleDataCheckbox.isSelected())
            return true;

        // If a student/sport couple should be generated, make sure a sport is generated too
        if(((int) this.studentSportField.getValue()) > 0 && ((int) this.sportCountField.getValue()) == 0) {
            // Show a message dialog
            JOptionPane.showMessageDialog(this, "Can't generate student & sport couples because no sports will be generated.\n" +
                    "Please increase the number of sports, or set the number of student & sport couples to zero.", "No sports available", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Everything seems to be all right, return the result
        return true;
    }

    /**
     * Start the database setup process.
     */
    public void setup() {
        // Validate the input
        if(!validateInput())
            return;

        // Create a progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this, "Building database...", false, "Starting...", true);

        // Set up the database builder
        DatabaseBuilder builder = new DatabaseBuilder(connector, progressDialog);

        // Set the user credentials
        builder.setAdminUser(this.defaultUser.getText(), String.valueOf(this.defaultPass.getPassword()));

        // Set whether to generate sample data
        builder.generateSampleData = this.generateSampleDataCheckbox.isSelected();

        // Apply the count values to the database builder
        builder.generateStudentCount = (int) studentCountField.getValue();
        builder.generateTeacherCount = (int) teacherCountField.getValue();
        builder.generateSchoolCount = (int) schoolCountField.getValue();
        builder.generateGroupCount = (int) groupCountField.getValue();
        builder.generateMeasurementCount = (int) measurementCountField.getValue();
        builder.generateBodyStateCount = (int) bodyStateCountField.getValue();
        builder.generateParkourCount = (int) parkourCountField.getValue();
        builder.generateSportCount = (int) sportCountField.getValue();
        builder.generateGroupTeacherCount = (int) groupTeacherField.getValue();
        builder.generateStudentSportCount = (int) studentSportField.getValue();

        // Build the database
        try {
            builder.build();

        } catch(SQLException e) {
            // Show a stack trace
            e.printStackTrace();

            // Show a warning popup
            JOptionPane.showMessageDialog(this, "An error occurred while building the database.", "Database error", JOptionPane.ERROR_MESSAGE);
        }

        // Dispose the progress dialog
        progressDialog.dispose();

        // Set the cancelled state
        this.cancelled = false;

        // Dispose the frame
        dispose();
    }
}
