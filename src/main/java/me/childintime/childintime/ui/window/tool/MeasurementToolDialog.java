package me.childintime.childintime.ui.window.tool;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.spec.measurement.Measurement;
import me.childintime.childintime.database.entity.spec.measurement.MeasurementFields;
import me.childintime.childintime.database.entity.spec.student.StudentFields;
import me.childintime.childintime.database.entity.ui.selector.EntityListSelectorComponent;
import me.childintime.childintime.ui.component.StopwatchComponent;
import me.childintime.childintime.ui.component.property.EntityPropertyField;
import me.childintime.childintime.ui.component.property.MillisecondPropertyField;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.Date;

public class MeasurementToolDialog extends JDialog {

    /**
     * Dialog title.
     */
    private static final String DIALOG_TITLE = App.APP_NAME + " - Measurement tool";

    /**
     * Group filter field.
     */
    private EntityPropertyField groupFilterField;

    /**
     * Student list.
     */
    private EntityListSelectorComponent studentList;

    /**
     * Parkour selector.
     */
    private EntityPropertyField parkourSelector;

    /**
     * Stopwatch component.
     */
    private StopwatchComponent stopwatchComponent;

    /**
     * Time field.
     */
    private MillisecondPropertyField timeField;

    /**
     * Constructor.
     *
     * @param owner Owner window.
     */
    public MeasurementToolDialog(Window owner) {
        // Construct the super
        super(owner, DIALOG_TITLE);

        // Build the UI
        buildUi();

        // Configure the frame size
        configureSize();

        // Set the window position
        setLocationRelativeTo(owner);
    }

    /**
     * Show the measurement tool dialog.
     *
     * @param owner Owner window.
     */
    public static void showDialog(Window owner) {
        // Create a new dialog instance
        MeasurementToolDialog dialog = new MeasurementToolDialog(owner);

        // Show the dialog
        dialog.setVisible(true);
    }

    /**
     * Properly configure the window sizes for the current content.
     */
    private void configureSize() {
        // Pack everything
        pack();

        // Determine the preferred dimensions
        final Dimension preferredSize = new Dimension(getMinimumSize().width + 300, getMinimumSize().height + 200);

        // Configure the sizes
        setMinimumSize(new Dimension(getMinimumSize()));
        setPreferredSize(preferredSize);
        setSize(preferredSize);
    }

    /**
     * Build the UI.
     */
    private void buildUi() {
        // Set the layout
        setLayout(new BorderLayout());

        // Create a grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Create the container and measurement panel
        final JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Add the close button
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);
        container.add(new JLabel("Select a student and the current parkour, track the time of the student and save the measurement."), c);

        // Add the measurement panel to the container
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(16, 0, 0, 0);
        container.add(buildUiMeasurementPanel(), c);

        // Create the close button
        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        // Add the close button
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(8, 0, 0, 0);
        container.add(closeButton, c);

        // Add the container
        add(container, BorderLayout.CENTER);
    }

    private JPanel buildUiMeasurementPanel() {
        // Create a grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Measurement panel
        final JPanel measurementPanel = new JPanel(new GridBagLayout());
        measurementPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Measurement tool"),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Add the student panel to the measurement panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 3;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        measurementPanel.add(buildUiStudentPanel(), c);

        // Add the input panel to the measurement panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.gridheight = 1;
        c.insets = new Insets(0, 16, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        measurementPanel.add(buildUiInputPanel(), c);

        // Add the input panel to the measurement panel
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.insets = new Insets(16, 16, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        measurementPanel.add(buildUiCommitButtonsPanel(), c);

        // Return the measurement panel
        return measurementPanel;
    }

    /**
     * Build the UI student panel.
     *
     * @return Student panel.
     */
    private JPanel buildUiStudentPanel() {
        // Create a grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Create a panel for the student selector
        final JPanel studentPanel = new JPanel(new GridBagLayout());

        // Create a group filter property field
        this.groupFilterField = new EntityPropertyField(Core.getInstance().getGroupManager(), true);
        this.groupFilterField.setNull(true);

        // Add the student label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 2, 2, 0);
        c.anchor = GridBagConstraints.SOUTHWEST;
        studentPanel.add(new JLabel("Student group filter:"), c);

        // Add the group filter
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 8, 0);
        c.anchor = GridBagConstraints.CENTER;
        studentPanel.add(this.groupFilterField, c);

        // Create a student selector list
        this.studentList = new EntityListSelectorComponent(Core.getInstance().getStudentManager());

        // Create a listener to set the student list filter when the group changes
        this.groupFilterField.addValueChangeListenerListener(newValue -> {
            // Set or clear the filter
            if(newValue != null)
                this.studentList.setFilter(StudentFields.GROUP_ID, newValue);
            else
                this.studentList.clearFilter();
        });

        // Add the student label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(8, 2, 2, 0);
        c.anchor = GridBagConstraints.SOUTHWEST;
        studentPanel.add(new JLabel("Student:"), c);

        // Add the student list
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        studentPanel.add(this.studentList, c);

        // Set the minimum preferred size
        studentPanel.setPreferredSize(new Dimension(200, 200));

        // Return the student panel
        return studentPanel;
    }

    /**
     * Build the input panel.
     *
     * @return Input panel.
     */
    private JPanel buildUiInputPanel() {
        // Create a new grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Create the panel
        final JPanel inputPanel = new JPanel(new GridBagLayout());

        // Create a parkour selector
        this.parkourSelector = new EntityPropertyField(Core.getInstance().getParkourManager(), true);

        // Add the parkour label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 2, 2, 0);
        c.anchor = GridBagConstraints.SOUTHWEST;
        inputPanel.add(new JLabel("Parkour:"), c);

        // Add the parkour selector
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        inputPanel.add(this.parkourSelector, c);

        // Create a time field and attach a stopwatch component
        this.timeField = new MillisecondPropertyField(0, true);
        this.stopwatchComponent = new StopwatchComponent(this.timeField);

        // Add the stopwatch
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(24, 0, 0, 0);
        c.anchor = GridBagConstraints.SOUTH;
        inputPanel.add(this.stopwatchComponent, c);

        // Add the parkour label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 2, 2, 0);
        c.anchor = GridBagConstraints.SOUTHWEST;
        inputPanel.add(new JLabel("Parkour time:"), c);

        // Add the time field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        inputPanel.add(this.timeField, c);

        // Set the minimum preferred size
        inputPanel.setPreferredSize(new Dimension(150, inputPanel.getPreferredSize().height));

        // Return the input panel
        return inputPanel;
    }

    /**
     * Build the commit buttons panel.
     *
     * @return Input panel.
     */
    private JPanel buildUiCommitButtonsPanel() {
        // Create a new grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Create the panel
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 8, 8));

        // Create the buttons
        JButton saveButton = new JButton("Save");
        JButton resetButton = new JButton("Reset");

        // Configure the save button
        saveButton.addActionListener(e -> {
            // Make sure a student is selected
            if(this.studentList.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select a student.", "No student selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Make sure a parkour is selected
            if(this.parkourSelector.getValue() == null) {
                JOptionPane.showMessageDialog(this, "Please select the parkour.", "No parkour selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Stop the stopwatch if it's running
            if(this.stopwatchComponent.isStarted())
                this.stopwatchComponent.startStop();

            // Make sure a time is entered
            if(this.timeField.isNull()) {
                JOptionPane.showMessageDialog(this, "Please enter a time.", "Invalid time", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Make sure the time is valid
            if(this.timeField.getMilliseconds() == 0) {
                JOptionPane.showMessageDialog(this, "The time you've entered is invalid.", "Invalid time", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create the measurement object
            Measurement newMeasurement = new Measurement();
            newMeasurement.parseField(MeasurementFields.STUDENT_ID, this.studentList.getSelectedItem());
            newMeasurement.parseField(MeasurementFields.PARKOUR_ID, this.parkourSelector.getSelected());
            newMeasurement.parseField(MeasurementFields.TIME, this.timeField.getMilliseconds());
            newMeasurement.parseField(MeasurementFields.DATE, new Date());

            // Create a progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this, "Saving measurement", false, "Saving measurement...", true);

            // Add the measurement object to the database
            if(!newMeasurement.applyToDatabase()) {
                // Dispose the progress dialog
                progressDialog.dispose();

                // Show an error message
                JOptionPane.showMessageDialog(this, "An error occurred while saving this measurement to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Refresh the measurements manager
            progressDialog.setStatus("Refreshing measurements...");
            Core.getInstance().getMeasurementManager().refresh();

            // Dispose the progress dialog
            progressDialog.dispose();

            // Clear the time field and stopwatch
            this.stopwatchComponent.clear();
            this.timeField.setValue(0);
            this.timeField.setNull(true);

            // Clear the student selection
            this.studentList.setSelectedItem(null);
        });

        // Configure the reset button
        resetButton.addActionListener(e -> {
            // Reset the group filter
            this.groupFilterField.setNull(true);

            // Clear the student list selection
            this.studentList.setSelectedItem(null);

            // Clear the parkour field
            this.parkourSelector.setNull(true);

            // Stop and clear the stopwatch
            if(this.stopwatchComponent.isStarted())
                this.stopwatchComponent.startStop();
            this.stopwatchComponent.clear();

            // Clear the time
            this.timeField.setValue(0);
            this.timeField.setNull(true);
        });

        // Add the buttons to the button panel
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);

        // Return the button panel
        return buttonPanel;
    }
}
