package me.childintime.childintime.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TextPropertyField extends AbstractPropertyField {

    /**
     * Define whether this is a password field.
     */
    private boolean isPassword = false;

    /**
     * The property field text.
     */
    private String text = null;

    /**
     * Text field.
     */
    private JTextField textField;

    /**
     * Clear button.
     */
    private JButton clearButton;

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public TextPropertyField(boolean allowNull) {
        // Call an alias constructor
        this(allowNull ? null : "", allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed, false if not.
     */
    public TextPropertyField(String value, boolean allowNull) {
        this(value, allowNull, false);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed, false if not.
     * @param isPassword True if password, false if not.
     */
    public TextPropertyField(String value, boolean allowNull, boolean isPassword) {
        // Call the super
        super(allowNull);

        // Define whether this is a password
        this.isPassword = isPassword;

        // Build the UI
        buildUi();

        // Set the text
        setText(value);
    }

    /**
     * Build the component UI.
     */
    private void buildUi() {
        // Set the layout
        setLayout(new GridBagLayout());

        // Create the grid bag constraints
        GridBagConstraints c = new GridBagConstraints();

        // Create the text field
        this.textField = !this.isPassword ? new JTextField("") : new JPasswordField("");
        this.textField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Enable the field
                enableField();
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Enable the field
                enableField();
            }

            @Override
            public void focusLost(FocusEvent e) { }
        });

        // Create the clear button
        this.clearButton = new JButton("✖");
        this.clearButton.addActionListener(e -> {
            // Set the value to null
            setNull(true);
        });

        this.clearButton.setPreferredSize(new Dimension(this.textField.getPreferredSize().height, this.textField.getPreferredSize().height));
        this.clearButton.setBorder(null);

        // Add the components
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        add(this.textField, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        add(this.clearButton, c);
    }

    @Override
    public String getValue() {
        return getText();
    }

    @Override
    public void setValue(Object text) {
        // Set the text, or null
        setText(text != null ? text.toString() : null);
    }

    /**
     * Get the text value.
     *
     * @return Text value.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the text value.
     *
     * @param text Text value.
     */
    public void setText(String text) {
        // Make sure null is allowed
        if(text == null && !isNullAllowed())
            throw new IllegalArgumentException("Null value not allowed");

        // Set the text and the null flag
        this.text = text;

        // Set the field content
        // TODO: Show null state if field is null!
        if(this.text != null)
            this.textField.setText(this.text);
        else
            this.textField.setText("<null>");

        // Update the enabled state of both components
        this.textField.setEnabled(!isNull());
        this.clearButton.setEnabled(!isNull());
        this.setFocusable(isNull());
    }

    /**
     * Set whether the property field is null.
     *
     * @param isNull True if null.
     */
    public void setNull(boolean isNull) {
        setText(isNull ? null : "");
    }

    /**
     * Enable the field for editing.
     * This will reset the null state, and select all the text inside the field.
     */
    public void enableField() {
        // Enable the field if it's disabled because it's value is null
        if(isNull())
            this.setText("");

        // Focus the field and select all
        this.textField.grabFocus();
        this.textField.selectAll();
    }
}
