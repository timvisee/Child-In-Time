package me.childintime.childintime.gui.component.property;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GenderPropertyField extends AbstractPropertyField {

    /**
     * True if an empty value is allowed.
     * If this is set to false, an empty string will be converted to null when focus is lost.
     * The field will be left empty if null is not allowed.
     */
    private boolean allowEmpty = true;

    /**
     * Null placeholder text.
     */
    private String nullPlaceholderText = "<null>";

    /**
     * Radio button for men.
     */
    protected JRadioButton menButton;

    /**
     * Radio button for women.
     */
    protected JRadioButton womenButton;

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public GenderPropertyField(boolean allowNull) {
        // Call an alias constructor
        this(false, allowNull);
    }

    /**
     * Constructor.
     *
     * @param state Value.
     * @param allowNull True if null is allowed, false if not.
     */
    public GenderPropertyField(Boolean state, boolean allowNull) {
        // Call the super
        super(allowNull);

        // Build the UI
        buildUi();

        // Set the state value
        setState(state);
    }

    @Override
    protected JComponent buildUiField() {
        // Create a panel with the two radio buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 8, 8));

        // Create a button group
        ButtonGroup buttonGroup = new ButtonGroup();

        // Build the radio buttons
        this.menButton = new JRadioButton("Men");
        this.womenButton = new JRadioButton("Women");

        // Add the radio buttons to the group
        buttonGroup.add(menButton);
        buttonGroup.add(womenButton);

        // Link the text field listeners
        final MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
        };
        this.menButton.addMouseListener(mouseListener);
        this.womenButton.addMouseListener(mouseListener);

        // Set the focus listeners
        final FocusListener focusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                // Disable the property field if it's currently empty
                disableIfEmpty();
            }
        };
        this.menButton.addFocusListener(focusListener);
        this.womenButton.addFocusListener(focusListener);

        // Set the field back to null when the escape key is pressed
        final Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make sure null is allowed
                if(!isNullAllowed())
                    return;

                // Transfer the focus to some other component
                transferFocus();

                // Set the field to null
                setNull(true);
            }
        };
        this.menButton.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        this.womenButton.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        this.menButton.getActionMap().put("Escape", escapeAction);
        this.womenButton.getActionMap().put("Escape", escapeAction);

        // Add the radio buttons to the panel
        buttonPanel.add(this.menButton);
        buttonPanel.add(this.womenButton);

        // Return the panel with the checkboxes
        return buttonPanel;
    }

    @Override
    public Boolean getValue() {
        return isMen();
    }

    @Override
    public void setValue(Object state) {
        // Set the text, or null
        setState((Boolean) state);
    }

    /**
     * Get the property field text value.
     * If the field is null, null will be returned.
     * If null is not allowed, an empty string will be returned instead of a null value.
     *
     * @return Text value, or null.
     */
    public boolean isMen() {
        // TODO: Return null?
        return !isNull() && !this.womenButton.isSelected();
    }

    /**
     * Set the text value.
     *
     * @param state Text value.
     */
    public void setState(Boolean state) {
        // Make sure null is allowed
        if(state == null && !isNullAllowed())
            throw new IllegalArgumentException("Null value not allowed");

        // Set the null state
        if(state == null)
            setNull(true);
        else if(isNull())
            setNull(false);

        // Set the text field text
        if(!isNull()) {
            this.menButton.setSelected(state);
            this.womenButton.setSelected(!state);
        }
    }

    @Override
    public void setNull(boolean _null) {
        // Set null in the super
        super.setNull(_null);

        // Update the enabled state of both components
        this.menButton.setEnabled(!_null);
        this.womenButton.setEnabled(!_null);

        // Disable the selection
        if(_null) {
            this.menButton.setSelected(false);
            this.womenButton.setSelected(false);
        }
    }

    @Override
    public void clear() {
        // Transfer focus to another component
        this.menButton.transferFocus();
        this.womenButton.transferFocus();

        // Clear the field
        SwingUtilities.invokeLater(() -> {
            // Set the field to null, or an empty string if null isn't allowed
            if(isNullAllowed())
                setNull(true);
            else
                setState(false);
        });
    }

    /**
     * Get the null placeholder text.
     *
     * @return Null placeholder text.
     */
    public String getNullPlaceholderText() {
        return this.nullPlaceholderText;
    }

    /**
     * Set the null placeholder text.
     *
     * @param nullPlaceholderText Null placeholder text.
     */
    public void setNullPlaceholderText(String nullPlaceholderText) {
        this.nullPlaceholderText = nullPlaceholderText;
    }

    @Override
    protected String getComponentName() {
        return getClass().getSimpleName();
    }

    /**
     * Check whether an empty field is allowed.
     *
     * @return True if an empty field is allowed.
     */
    public boolean isEmptyAllowed() {
        return this.allowEmpty;
    }

    /**
     * Set whether an empty field is allowed.
     *
     * @param allowEmpty True if allowed, false if not.
     */
    public void setEmptyAllowed(boolean allowEmpty) {
        // Set the flag
        this.allowEmpty = allowEmpty;

        // Clear the field if it's empty while it isn't currently focused
        if(!allowEmpty && !this.menButton.hasFocus())
            disableIfEmpty();
    }

    /**
     * Check whether the property field is empty.
     * If the field is null, true is returned.
     *
     * @return True if empty, false if not.
     */
    public boolean isEmpty() {
        return isNull();
    }

    /**
     * Enable the field for editing.
     * This will reset the null state, and select all the text inside the field.
     */
    public void enableField() {
        // Enable the field if it's disabled because it's value is null
        if(isNull())
            this.setState(true);

        // Focus the field and select all
        this.menButton.grabFocus();
    }

    /**
     * Disable the field if it's empty.
     * This sets the field to null if it's empty, if null is not allowed the field is left blank.
     */
    public void disableIfEmpty() {
        // Clear the field if it's empty, and empty is allowed
        if(isNullAllowed() && !isEmptyAllowed() && !isNull() && isEmpty())
            setNull(true);
    }
}
