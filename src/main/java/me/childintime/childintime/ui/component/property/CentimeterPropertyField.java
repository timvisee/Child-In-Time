package me.childintime.childintime.ui.component.property;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

public class CentimeterPropertyField extends IntegerPropertyField {

    /**
     * Number format.
     */
    private static final String NUMBER_FORMAT = "#0.00";

    /**
     * Number format suffix.
     */
    private static final String NUMBER_FORMAT_SUFFIX = " m";

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public CentimeterPropertyField(boolean allowNull) {
        // Call an alias constructor
        this(0, allowNull);
    }

    /**
     * Constructor.
     *
     * @param centimeters Length in centimeters.
     * @param allowNull True if null is allowed, false if not.
     */
    public CentimeterPropertyField(Integer centimeters, boolean allowNull) {
        // Call the super
        super(centimeters, allowNull);

        // Set the time to zero if it's undefined
        if(centimeters == null)
            centimeters = 0;

        // Convert the length into meters
        double meters = centimeters / 100.0;

        // Set the spinner model
        this.spinner.setModel(new SpinnerNumberModel(meters, 0.0, 5.0, 0.01));

        // Create and set a basic number formatter
        final JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this.spinner);
        this.spinner.setEditor(editor);

        // Create the display and edit formatter
        final NumberFormatter displayFormatter = new NumberFormatter(new DecimalFormat(NUMBER_FORMAT + "'" + NUMBER_FORMAT_SUFFIX + "'"));
        final NumberFormatter editFormatter = new NumberFormatter(new DecimalFormat(NUMBER_FORMAT));
        displayFormatter.setValueClass(Double.class);
        editFormatter.setValueClass(Double.class);

        // Configure the formatter
        editor.getTextField().setFormatterFactory(new DefaultFormatterFactory(
                displayFormatter,
                displayFormatter,
                editFormatter
        ));

        // Align the text to the left
        editor.getTextField().setHorizontalAlignment(SwingConstants.LEFT);

        // Select the spinner contents when it's focused
        editor.getTextField().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Select all contents when the spinner field gains focus after it has been updated
                SwingUtilities.invokeLater(() -> editor.getTextField().selectAll());
            }

            @Override
            public void focusLost(FocusEvent e) { }
        });
    }

    @Override
    public Object getValue() {
        return (int) (((Double) this.spinner.getValue()) * 100);
    }

    @Override
    public void setValue(Object time) {
        // Cast the value to an integer
        int milliseconds = (int) time;

        // Convert the time into a double
        double seconds = milliseconds / 100;

        // Set the value
        this.spinner.setValue(seconds);
    }

    /**
     * Get the length in centimeters.
     *
     * @return Length in centimeters.
     */
    public int getCentimeter() {
        return (int) getValue();
    }

    /**
     * Set the length in centimeters.
     *
     * @param centimeters Length in centimeters.
     */
    public void setCentimeter(int centimeters) {
        setValue(centimeters);
    }
}
