package me.childintime.childintime.gui.component.property;

import javax.swing.*;
import java.awt.event.*;

public class BooleanPropertyField extends AbstractPropertyField {

    /**
     * True if an empty value is allowed.
     * If this is set to false, an empty string will be converted to null when focus is lost.
     * The field will be left empty if null is not allowed.
     */
    private boolean allowEmpty = true;

    /**
     * Boolean field.
     */
    protected JCheckBox checkBox;

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public BooleanPropertyField(boolean allowNull) {
        // Call an alias constructor
        this(false, allowNull);
    }

    /**
     * Constructor.
     *
     * @param state Value.
     * @param allowNull True if null is allowed, false if not.
     */
    public BooleanPropertyField(Boolean state, boolean allowNull) {
        // Call the super
        super(allowNull);

        // Build the UI
        buildUi();

        // Set the state value
        setState(state);
    }

    @Override
    protected JComponent buildUiField() {
        // Build the text field
        this.checkBox = new JCheckBox();

        // Link the text field listeners
        this.checkBox.addMouseListener(new MouseListener() {
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
        });
        this.checkBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                // Disable the property field if it's currently empty
                disableIfEmpty();
            }
        });

        // Set the field back to null when the escape key is pressed
        this.checkBox.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        this.checkBox.getActionMap().put("Escape", new AbstractAction() {
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
        });

        // Return the checkbox
        return this.checkBox;
    }

    /**
     * Get the checkbox.
     *
     * @return Checkbox.
     */
    public JCheckBox getCheckBox() {
        return this.checkBox;
    }

    @Override
    public Boolean getValue() {
        return getState();
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
    public boolean getState() {
        // TODO: Return null?
        return !isNull() && this.checkBox.isSelected();
    }

    /**
     * Set the text value.
     *
     * @param text Text value.
     */
    public void setState(Boolean text) {
        // Make sure null is allowed
        if(text == null && !isNullAllowed())
            throw new IllegalArgumentException("Null value not allowed");

        // Set the null state
        if(text == null)
            setNull(true);
        else if(isNull())
            setNull(false);

        // Set the text field text
        if(!isNull())
            this.checkBox.setSelected(text);
    }

    @Override
    public void setNull(boolean _null) {
        // Set null in the super
        super.setNull(_null);

        // Update the enabled state of both components
        this.checkBox.setEnabled(!_null);

        // Disable the selection
        if(_null)
            this.checkBox.setSelected(false);
    }

    @Override
    public void clear() {
        // Transfer focus to another component
        this.checkBox.transferFocus();

        // Clear the field
        SwingUtilities.invokeLater(() -> {
            // Set the field to null, or an empty string if null isn't allowed
            if(isNullAllowed())
                setNull(true);
            else
                setState(false);
        });
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
        if(!allowEmpty && !this.checkBox.hasFocus())
            disableIfEmpty();
    }

    /**
     * Check whether the property field is empty.
     * If the field is null, true is returned.
     *
     * @return True if empty, false if not.
     */
    public boolean isInputEmpty() {
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
        this.checkBox.grabFocus();
    }

    /**
     * Disable the field if it's empty.
     * This sets the field to null if it's empty, if null is not allowed the field is left blank.
     */
    public void disableIfEmpty() {
        // Clear the field if it's empty, and empty is allowed
        if(isNullAllowed() && !isEmptyAllowed() && !isNull() && isInputEmpty())
            setNull(true);
    }
}
