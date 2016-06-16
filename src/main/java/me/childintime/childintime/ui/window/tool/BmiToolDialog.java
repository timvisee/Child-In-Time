package me.childintime.childintime.ui.window.tool;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.spec.bodystate.BodyState;
import me.childintime.childintime.database.entity.spec.bodystate.BodyStateFields;
import me.childintime.childintime.database.entity.spec.student.Student;
import me.childintime.childintime.database.entity.spec.student.StudentFields;
import me.childintime.childintime.database.entity.ui.component.EntityViewComponent;
import me.childintime.childintime.database.entity.ui.selector.EntityListSelectorComponent;
import me.childintime.childintime.ui.component.property.EntityPropertyField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class BmiToolDialog extends JDialog {

    // TODO: This window is based on the body state window, and has not fully been converted yet!

    /**
     * Dialog title.
     */
    private static final String DIALOG_TITLE = App.APP_NAME + " - BMI tool";

    /**
     * Group filter field.
     */
    private EntityPropertyField groupFilterField;

    /**
     * Student list.
     */
    private EntityListSelectorComponent studentList;

    /**
     * Student body state list.
     */
    private EntityViewComponent studentBodyStates;

    /**
     * Chart.
     */
    private JFreeChart chart;

    /**
     * Dataset.
     */
    private XYSeriesCollection dataset = new XYSeriesCollection();

    /**
     * Constructor.
     *
     * @param owner Owner window.
     */
    public BmiToolDialog(Window owner) {
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
     * Show the BMI tool dialog.
     *
     * @param owner Owner window.
     */
    public static void showDialog(Window owner) {
        // Create a new dialog instance
        BmiToolDialog dialog = new BmiToolDialog(owner);

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
        final Dimension preferredSize = new Dimension(getMinimumSize().width + 500, getMinimumSize().height + 400);

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
        container.add(new JLabel("Select a student to see their details."), c);

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
                BorderFactory.createTitledBorder("BMI tool"),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Add the student panel to the body state panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 2;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        bodyStatePanel.add(buildUiStudentPanel(), c);

        // Add the input panel to the body state panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 1;
        c.insets = new Insets(0, 16, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        bodyStatePanel.add(buildUiChartPanel(), c);

        // Add the input panel to the body state panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
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

        // Update the data set
        updateChartDataset();
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
     * Build the chart panel.
     *
     * @return Chart panel.
     */
    private ChartPanel buildUiChartPanel() {
        // Create the chart
        this.chart = createChart(this.dataset);

        // Update the data set
        updateChartDataset();

        // Create the chart panel
        ChartPanel chartPanel = new ChartPanel(this.chart);

        // Set the minimum preferred size
        chartPanel.setPreferredSize(new Dimension(150, 150));

        // Update the chart when the measurements are changed
        Core.getInstance().getBodyStateManager().addChangeListener(this::updateChartDataset);

        // Return the input panel
        return chartPanel;
    }

    /**
     * Reset the input fields.
     */
    private void reset() {
        // Reset the group filter
        this.groupFilterField.setNull(true);

        // Clear the student list selection
        this.studentList.setSelectedItem(null);

        // Update the student body state list
        updateStudentBodyStateList();
    }

    /**
     * Creates a sample dataset.
     *
     * @return a sample dataset.
     */
    private void updateChartDataset() {
        // Clear the data set
        this.dataset.removeAllSeries();

        try {
            // Create the data series
            final XYSeries lengthSeries = new XYSeries("Length (cm)");
            final XYSeries weightSeries = new XYSeries("Weight (kg)");
            final XYSeries bmiSeries = new XYSeries("BMI");

            // Get the student
            Student student = (Student) this.studentList.getSelectedItem();

            // Make sure a student is selected
            if(student == null)
                return;

            // Get the student birthdate
            Date birthdate = (Date) student.getField(StudentFields.BIRTHDAY);

            // Age
            final long age = ChronoUnit.YEARS.between(birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());

            // Loop through the list of body states
            for(AbstractEntity abstractEntity : Core.getInstance().getBodyStateManager().getEntities()) {
                // Cast the entity to a body state
                BodyState bodyState = (BodyState) abstractEntity;

                // Make sure the student owns this body state
                try {
                    if(!bodyState.getField(BodyStateFields.STUDENT_ID).equals(student))
                        continue;

                } catch(Exception e) {
                    e.printStackTrace();
                }

                // Get the measurement date
                final Date measurementDate = (Date) bodyState.getField(BodyStateFields.DATE);

                // Age
                final long measurementAge = ChronoUnit.YEARS.between(measurementDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());

                // Get the length and weight
                final int length = (int) bodyState.getField(BodyStateFields.LENGTH);
                final double weight = ((int) bodyState.getField(BodyStateFields.WEIGHT)) / 1000.0;

                // Calculate the BMI
                final double bmi = weight / Math.pow((length / 100.0), 2);

                // Add the data to the sets
                lengthSeries.add(age - measurementAge, length);
                weightSeries.add(age - measurementAge, weight);
                bmiSeries.add(age - measurementAge, bmi);
            }

            // Add the data series to the set
            this.dataset.addSeries(bmiSeries);
            this.dataset.addSeries(lengthSeries);
            this.dataset.addSeries(weightSeries);

        } catch(Exception e) {
            e.printStackTrace();
        }

        // Re set the dataset
        this.chart.getXYPlot().setDataset(this.dataset);
    }


    /**
     * Creates a chart.
     *
     * @param dataset  the data for the chart.
     *
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        // Create the chart
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "BMI chart",
                "Age",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Set the background color to the LAF's default
        chart.setBackgroundPaint(new JPanel().getBackground());

        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for(int i = 0; i < 3; i++) {
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, true);
            renderer.setSeriesFillPaint(i, Color.RED);
        }
        plot.setRenderer(renderer);

        plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

}
