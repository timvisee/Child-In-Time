package me.childintime.childintime.ui.component.property;

import javax.swing.*;
import java.awt.event.*;

public class IntegerPropertyField extends AbstractPropertyField {

    /**
     * True if an empty value is allowed.
     * If this is set to false, an empty string will be converted to null when focus is lost.
     * The field will be left empty if null is not allowed.
     */
    private boolean allowEmpty = true;

    /**
     * Integer field.
     */
    protected JSpinner spinner;

    /**
     * Spinner mouse listener.
     */
    protected final MouseListener spinnerMouseListener = new MouseListener() {
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

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public IntegerPropertyField(boolean allowNull) {
        // Call an alias constructor
        this(0, allowNull);
    }

    /**
     * Constructor.
     *
     * @param number Number alue.
     * @param allowNull True if null is allowed, false if not.
     */
    public IntegerPropertyField(Integer number, boolean allowNull) {
        // Call the super
        super(allowNull);

        // Build the UI
        buildUi();

        // Set the state value
        setInteger(number);
    }

    @Override
    protected JComponent buildUiField() {
        // Build the text field
        this.spinner = new JSpinner(new SpinnerNumberModel());

        // Align the spinner number to the left
        JComponent spinnerEditor = spinner.getEditor();
        if(spinnerEditor instanceof JSpinner.DefaultEditor)
            ((JSpinner.DefaultEditor) spinnerEditor).getTextField().setHorizontalAlignment(SwingConstants.LEFT);

        // Link the text field listeners
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addMouseListener(spinnerMouseListener);
        this.spinner.addMouseListener(spinnerMouseListener);
        this.spinner.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                // Disable the property field if it's currently empty
                disableIfEmpty();
            }
        });

        // Set the field back to null when the escape key is pressed
        this.spinner.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        this.spinner.getActionMap().put("Escape", new AbstractAction() {
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
        return this.spinner;
    }

    /**
     * Get the spinner.
     *
     * @return Spinner.
     */
    public JSpinner getSpinner() {
        return this.spinner;
    }

    @Override
    public Object getValue() {
        return getInteger();
    }

    @Override
    public void setValue(Object number) {
        // Set the text, or null
        setInteger((Integer) number);
    }

    /**
     * Get the property field number value.
     * If the field is null, null will be returned.
     * If null is not allowed, zero will be returned.
     *
     * @return Number value, or null.
     */
    public Integer getInteger() {
        // Return null, if the value is null
        if(isNull())
            return null;

        // Return the number otherwise
        return (Integer) this.spinner.getValue();
    }

    /**
     * Set the number value.
     *
     * @param number Number value.
     */
    public void setInteger(Integer number) {
        // Make sure null is allowed
        if(number == null && !isNullAllowed())
            throw new IllegalArgumentException("Null value not allowed");

        // Set the null state
        if(number == null)
            setNull(true);
        else {
            // Disable the null state of the field
            if(isNull())
                setNull(false);

            // Set the value
            this.spinner.setValue(number);
        }
    }

    @Override
    public void setNull(boolean _null) {
        // Set null in the super
        super.setNull(_null);

        // Update the enabled state of both components
        this.spinner.setEnabled(!_null);
    }

    @Override
    public void clear() {
        // Transfer focus to another component
        this.spinner.transferFocus();

        // Clear the field
        SwingUtilities.invokeLater(() -> {
            // Set the field to null, or an empty string if null isn't allowed
            if(isNullAllowed())
                setNull(true);
            else
                setInteger(0);
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
        if(!allowEmpty && !this.spinner.hasFocus())
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
            this.setNull(false);

        // Focus the field and select all
        //this.spinner.grabFocus();
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
