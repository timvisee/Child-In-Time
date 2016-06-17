/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package me.childintime.childintime.ui.component.property;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

public class MillisecondPropertyField extends IntegerPropertyField {

    /**
     * Number format.
     */
    private static final String NUMBER_FORMAT = "#0.000";

    /**
     * Number format suffix.
     */
    private static final String NUMBER_FORMAT_SUFFIX = " s";

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public MillisecondPropertyField(boolean allowNull) {
        // Call an alias constructor
        this(0, allowNull);
    }

    /**
     * Constructor.
     *
     * @param millisecond Weight in milliseconds.
     * @param allowNull True if null is allowed, false if not.
     */
    public MillisecondPropertyField(Integer millisecond, boolean allowNull) {
        // Call the super
        super(millisecond, allowNull);

        // Set the time to zero if it's undefined
        if(millisecond == null)
            millisecond = 0;

        // Convert the millisecond into seconds
        double second = millisecond / 1000.0;

        // Set the spinner model
        this.spinner.setModel(new SpinnerNumberModel(second, 0.0, 9999999.0, 0.01));

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
        editor.getTextField().addMouseListener(this.spinnerMouseListener);
    }

    @Override
    public Object getValue() {
        return (int) (((Double) this.spinner.getValue()) * 1000);
    }

    @Override
    public void setValue(Object time) {
        // Cast the value to an integer
        int milliseconds = (int) time;

        // Convert the time into a double
        double seconds = milliseconds / 1000.0;

        // Disable the null state of the field
        if(isNull())
            setNull(false);

        // Set the value
        this.spinner.setValue(seconds);
    }

    /**
     * Get the weight in milliseconds.
     *
     * @return Weight in milliseconds.
     */
    public int getMilliseconds() {
        return (int) getValue();
    }

    /**
     * Set the weight in milliseconds.
     *
     * @param milliseconds Weight in milliseconds.
     */
    public void setMilliseconds(int milliseconds) {
        setValue(milliseconds);
    }
}
