package me.childintime.childintime.ui.window.tool;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.spec.bodystate.BodyState;
import me.childintime.childintime.database.entity.spec.bodystate.BodyStateFields;
import me.childintime.childintime.database.entity.spec.student.Student;
import me.childintime.childintime.database.entity.spec.student.StudentFields;
import me.childintime.childintime.database.entity.ui.component.EntityViewComponent;
import me.childintime.childintime.database.entity.ui.selector.EntityListSelectorComponent;
import me.childintime.childintime.ui.component.property.CentimeterPropertyField;
import me.childintime.childintime.ui.component.property.DatePropertyField;
import me.childintime.childintime.ui.component.property.EntityPropertyField;
import me.childintime.childintime.ui.component.property.GramPropertyField;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.Date;

public class BodyStateToolDialog extends JDialog {

    /**
     * Dialog title.
     */
    private static final String DIALOG_TITLE = App.APP_NAME + " - Body state tool";

    /**
     * Group filter field.
     */
    private EntityPropertyField groupFilterField;

    /**
     * Student list.
     */
    private EntityListSelectorComponent studentList;

    /**
     * Date field.
     */
    private DatePropertyField dateField;

    /**
     * Length field.
     */
    private CentimeterPropertyField lengthField;

    /**
     * Weight field.
     */
    private GramPropertyField weightField;

    /**
     * Student body state list.
     */
    private EntityViewComponent studentBodyStates;

    /**
     * Constructor.
     *
     * @param owner Owner window.
     */
    public BodyStateToolDialog(Window owner) {
        // Construct the super
        super(owner, DIALOG_TITLE);

        // Build the UI
        buildUi();

        // Make the dialog modal
        setModal(true);

        // Configure the frame size
        configureSize();

        // Set the window position
        setLocationRelativeTo(owner);

        // Reset
        reset();
    }

    /**
     * Show the body state tool dialog.
     *
     * @param owner Owner window.
     */
    public static void showDialog(Window owner) {
        // Create a new dialog instance
        BodyStateToolDialog dialog = new BodyStateToolDialog(owner);

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

        // Create the container and body state panel
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
        container.add(new JLabel("Select a student and the current parkour, track the time of the student and save the body state."), c);

        // Add the body state panel to the container
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(16, 0, 0, 0);
        container.add(buildUiBodyStatePanel(), c);

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

    private JPanel buildUiBodyStatePanel() {
        // Create a grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Body state panel
        final JPanel bodyStatePanel = new JPanel(new GridBagLayout());
        bodyStatePanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Body state tool"),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Add the student panel to the body state panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 3;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        bodyStatePanel.add(buildUiStudentPanel(), c);

        // Add the input panel to the body state panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.gridheight = 1;
        c.insets = new Insets(0, 16, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        bodyStatePanel.add(buildUiInputPanel(), c);

        // Add the input panel to the body state panel
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.insets = new Insets(16, 16, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        bodyStatePanel.add(buildUiCommitButtonsPanel(), c);

        // Add the input panel to the body state panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.insets = new Insets(32, 16, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        bodyStatePanel.add(buildUiStudentBodyStatesPanel(), c);

        // Return the body state panel
        return bodyStatePanel;
    }

    /**
     * Build the student body states panel.
     *
     * @return Student body states panel.
     */
    private JPanel buildUiStudentBodyStatesPanel() {
        // Create a new grid bag constraints configuration instance
        GridBagConstraints c = new GridBagConstraints();

        // Create the panel
        final JPanel studentBodyStatePanel = new JPanel(new GridBagLayout());

        // Add the label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(8, 2, 2, 0);
        c.anchor = GridBagConstraints.SOUTHWEST;
        studentBodyStatePanel.add(new JLabel("Student's body states:"), c);

        // Create a list for the students body states
        this.studentBodyStates = new EntityViewComponent(Core.getInstance().getBodyStateManager());

        // Add the list
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        studentBodyStatePanel.add(this.studentBodyStates, c);

        // Update the student body state list when the selected student is changed
        this.studentList.addSelectionChangeListenerListener(this::updateStudentBodyStateList);

        // Set the minimum preferred size of the body states list
        this.studentBodyStates.setPreferredSize(new Dimension(200, 100));

        // Return the panel
        return studentBodyStatePanel;
    }

    /**
     * Update the list of student body states.
     */
    private void updateStudentBodyStateList() {
        // Get the selected student
        Student selected = (Student) this.studentList.getSelectedItem();

        // Update the filter
        this.studentBodyStates.setFilter(BodyStateFields.STUDENT_ID, selected);

        // Update the empty label
        this.studentBodyStates.setEmptyLabel(selected != null ? "No body states on record for " + selected + "..." : "No student selected...");
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

        // Create the date field
        this.dateField = new DatePropertyField(new Date(), true);

        // Create the length field
        this.lengthField = new CentimeterPropertyField(0, true);

        // Create the weight field
        this.weightField = new GramPropertyField(0, true);

        // Add the date label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 2, 2, 0);
        c.anchor = GridBagConstraints.SOUTHWEST;
        inputPanel.add(new JLabel("Date:"), c);

        // Add the date field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        inputPanel.add(this.dateField, c);

        // Add the length label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(16, 2, 2, 0);
        c.anchor = GridBagConstraints.SOUTHWEST;
        inputPanel.add(new JLabel("Length:"), c);

        // Add the length field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        inputPanel.add(this.lengthField, c);

        // Add the weight label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(16, 2, 2, 0);
        c.anchor = GridBagConstraints.SOUTHWEST;
        inputPanel.add(new JLabel("Weight:"), c);

        // Add the weight field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        inputPanel.add(this.weightField, c);

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

            // Make the selected date is valid
            if(!this.dateField.isValidDate()) {
                JOptionPane.showMessageDialog(this, "Please select a date.", "Invalid date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Make sure a length is entered
            if(this.lengthField.isNull()) {
                JOptionPane.showMessageDialog(this, "Please enter a body length.", "Invalid length", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Make sure the length is valid
            if(this.lengthField.getCentimeter() == 0) {
                JOptionPane.showMessageDialog(this, "The length you've entered is invalid.", "Invalid length", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Make sure a weight is entered
            if(this.weightField.isNull()) {
                JOptionPane.showMessageDialog(this, "Please enter a body weight.", "Invalid weight", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Make sure the weight is valid
            if(this.weightField.getGrams() == 0) {
                JOptionPane.showMessageDialog(this, "The weight you've entered is invalid.", "Invalid weight", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create the body state object
            BodyState newBodyState = new BodyState();
            newBodyState.parseField(BodyStateFields.STUDENT_ID, this.studentList.getSelectedItem());
            newBodyState.parseField(BodyStateFields.LENGTH, this.lengthField.getCentimeter());
            newBodyState.parseField(BodyStateFields.WEIGHT, this.weightField.getGrams());
            newBodyState.parseField(BodyStateFields.DATE, this.dateField.getDate());

            // Create a progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this, "Saving body state", false, "Saving body state...", true);

            // Add the body state object to the database
            if(!newBodyState.applyToDatabase()) {
                // Dispose the progress dialog
                progressDialog.dispose();

                // Show an error message
                JOptionPane.showMessageDialog(this, "An error occurred while saving this body state to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Refresh the body state manager
            progressDialog.setStatus("Refreshing body state...");
            Core.getInstance().getBodyStateManager().refresh();

            // Dispose the progress dialog
            progressDialog.dispose();

            // Clear the student selection
            this.studentList.setSelectedItem(null);

            // Clear the length field
            this.lengthField.setCentimeter(0);
            this.lengthField.setNull(true);

            // Clear the weight field
            this.weightField.setGrams(0);
            this.weightField.setNull(true);
        });

        // Configure the reset button
        resetButton.addActionListener(e -> reset());

        // Add the buttons to the button panel
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Reset the input fields.
     */
    private void reset() {
        // Reset the group filter
        this.groupFilterField.setNull(true);

        // Clear the student list selection
        this.studentList.setSelectedItem(null);

        // Clear the date field
        this.dateField.setDate(new Date());

        // Clear the length field
        this.lengthField.setCentimeter(0);
        this.lengthField.setNull(true);

        // Clear the weight field
        this.weightField.setGrams(0);
        this.weightField.setNull(true);

        // Update the student body state list
        updateStudentBodyStateList();
    }
}
