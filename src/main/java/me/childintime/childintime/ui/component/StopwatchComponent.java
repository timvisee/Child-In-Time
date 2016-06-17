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

package me.childintime.childintime.ui.component;

import me.childintime.childintime.ui.component.property.MillisecondPropertyField;
import me.childintime.childintime.util.time.Profiler;

import javax.swing.*;
import java.awt.*;

public class StopwatchComponent extends JComponent {

    /**
     * Attached millisecond property field.
     */
    private MillisecondPropertyField attachedField;

    /**
     * Action button.
     */
    private JButton actionButton;

    /**
     * Clear button.
     */
    private JButton clearButton;

    /**
     * The time label.
     */
    private JLabel timeLabel;

    /**
     * Profiler, used to track time.
     */
    private Profiler profiler = new Profiler(false);

    /**
     * Constructor.
     */
    public StopwatchComponent() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param attachedField Attached field, may be null.
     */
    public StopwatchComponent(MillisecondPropertyField attachedField) {
        // Set the attached field
        this.attachedField = attachedField;

        // Build the component UI
        buildUi();
    }

    /**
     * Build the component UI.
     */
    private void buildUi() {
        // Create a grid bag constraints configuration
        GridBagConstraints c = new GridBagConstraints();

        // Set the component layout
        setLayout(new GridBagLayout());

        // Build the control buttons panel
        final JPanel controlButtonsPanel = new JPanel(new GridLayout(1, 2, 8, 8));

        // Create the time label
        this.timeLabel = new JLabel("0 s", SwingConstants.CENTER);
        this.timeLabel.setFont(new Font(this.timeLabel.getFont().getName(), Font.PLAIN, 36));

        // Add the label
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        add(this.timeLabel, c);

        // Create the control buttons
        this.actionButton = new JButton("Start");
        this.clearButton = new JButton("Clear");
        this.actionButton.addActionListener(e -> startStop());
        this.clearButton.addActionListener(e -> clear());

        // Add the buttons to the panel
        controlButtonsPanel.add(this.actionButton);
        controlButtonsPanel.add(this.clearButton);

        // Add the button panel to the component
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(8, 0, 0, 0);
        add(controlButtonsPanel, c);
    }

    /**
     * Check whether the stopwatch is started/active.
     */
    public boolean isStarted() {
        return this.profiler.isActive();
    }

    /**
     * Start/stop the stopwatch.
     */
    public void startStop() {
        // Start or stop the stopwatch
        if(!isStarted()) {
            // Start the profiler
            this.profiler.start();

            // Build the thread
            final Thread stopwatchThread = new Thread(() -> {
                // Create a while loop
                while(isStarted()) {
                    // Start the profiler if it hasn't been started yet
                    this.profiler.start();

                    // Update the label
                    updateLabel();

                    // Sleep for a while, don't chew up all available resources!
                    try {
                        Thread.sleep(16);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Start the thread
            stopwatchThread.start();

            // Disable the field
            if(this.attachedField != null)
                this.attachedField.setNull(true);

            // Update the label
            updateLabel();

            // Set the action button text
            this.actionButton.setText("Stop");

        } else {
            // Stop the profiler
            this.profiler.stop();

            // Set the time in milliseconds in the field
            if(this.attachedField != null)
                this.attachedField.setMilliseconds((int) this.profiler.getTime());

            // Set the action button text
            this.actionButton.setText("Start");
        }
    }

    /**
     * Clear the stopwatch.
     */
    public void clear() {
        // Clear the profiler
        this.profiler.clear();

        // Clear the field
        if(attachedField != null) {
            this.attachedField.setMilliseconds(0);
            this.attachedField.setNull(true);
        }

        // Update the label
        updateLabel();
    }

    /**
     * Update the label.
     */
    private void updateLabel() {
        // Update the label
        this.timeLabel.setText(this.profiler.getTimeFormatted());
    }

    /**
     * Get the attached field.
     *
     * @return Attached field.
     */
    public MillisecondPropertyField getAttachedField() {
        return this.attachedField;
    }

    /**
     * Set the attached field.
     *
     * @param attachedField Attached field.
     */
    public void setAttachedField(MillisecondPropertyField attachedField) {
        this.attachedField = attachedField;
    }
}
