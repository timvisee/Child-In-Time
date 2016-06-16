package me.childintime.childintime.ui.component.property;

import me.childintime.childintime.database.entity.listener.ValueChangeListener;
import me.childintime.childintime.permission.PermissionLevel;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionLevelPropertyField extends AbstractPropertyField {

    /**
     * True if an empty value is allowed.
     * If this is set to false, an empty string will be converted to null when focus is lost.
     * The field will be left empty if null is not allowed.
     */
    private boolean allowEmpty = true;

    /**
     * Permission level combobox.
     */
    private JComboBox permissionLevelField;

    /**
     * List of value change listeners.
     */
    private List<ValueChangeListener> valueChangeListeners = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param permissionLevel Permission level.
     * @param allowNull True if null is allowed, false if not.
     */
    public PermissionLevelPropertyField(PermissionLevel permissionLevel, boolean allowNull) {
        // Call the super
        super(allowNull);

        // Build the UI
        buildUi();

        // Set the selected item
        setSelected(permissionLevel);
    }

    @Override
    protected JComponent buildUiField() {
        // Build the combo box
        this.permissionLevelField = new JComboBox<>(PermissionLevel.values());

        // Link the text field listeners
        this.permissionLevelField.addMouseListener(new MouseListener() {
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
        this.permissionLevelField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                // Disable the property field if it's currently empty
                disableIfEmpty();
            }
        });

        // Set the field back to null when the escape key is pressed
        this.permissionLevelField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        this.permissionLevelField.getActionMap().put("Escape", new AbstractAction() {
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
        return this.permissionLevelField;
    }

    /**
     * Get the combo box.
     *
     * @return Combo box.
     */
    public JComboBox getComboBox() {
        return this.permissionLevelField;
    }

    @Override
    public PermissionLevel getValue() {
        return getSelected();
    }

    @Override
    public void setValue(Object value) {
        // Set the selected entity, or null
        setSelected((PermissionLevel) value);
    }

    /**
     * Get the property field entity value.
     * If the field is null, null will be returned.
     *
     * @return Permission level, or null.
     */
    public PermissionLevel getSelected() {
        return isNull() ? null : (PermissionLevel) this.getComboBox().getSelectedItem();
    }

    /**
     * Set the selected value.
     *
     * @param selected Selected value.
     */
    public void setSelected(PermissionLevel selected) {
        // Make sure null is allowed
        if(selected == null && !isNullAllowed())
            throw new IllegalArgumentException("Null value not allowed");

        // Set the null state
        if(selected == null)
            setNull(true);
        else {
            // Disable the null state if the component is currently null
            if(isNull())
                setNull(false);

            // Set the selected value
            this.permissionLevelField.setSelectedItem(selected);

            // Fire the value change event
            fireValueChangeListenerEvent(selected);
        }
    }

    @Override
    public void setNull(boolean _null) {
        // Set null in the super
        super.setNull(_null);

        // Update the enabled state of both components
        this.permissionLevelField.setEnabled(!_null);

        // Fire the value change event
        fireValueChangeListenerEvent(null);
    }

    @Override
    public void clear() {
        // Transfer focus to another component
        this.permissionLevelField.transferFocus();

        // Clear the field
        SwingUtilities.invokeLater(() -> {
            // Set the field to null, or an empty string if null isn't allowed
            if(isNullAllowed())
                setNull(true);
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
        if(!allowEmpty && !this.permissionLevelField.hasFocus())
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
        this.permissionLevelField.grabFocus();
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

    @Override
    public boolean isNull() {
        // Return true if the super is null
        if(super.isNull())
            return true;

        // Return true if the selected value is null/undefined
        return this.permissionLevelField.getSelectedItem() == null;
    }

    /**
     * Add an value change listener.
     *
     * @param listener Listener.
     */
    public void addValueChangeListenerListener(ValueChangeListener listener) {
        this.valueChangeListeners.add(listener);
    }

    /**
     * Get all the registered value change listeners.
     *
     * @return List of value change listeners.
     */
    public List<ValueChangeListener> getValueChangeListenerListeners() {
        return this.valueChangeListeners;
    }

    /**
     * Remove the given value change listener.
     *
     * @param listener Listener to remove.
     *
     * @return True if any listener was removed, false if not.
     */
    public boolean removeValueChangeListenerListeners(ValueChangeListener listener) {
        return this.valueChangeListeners.remove(listener);
    }

    /**
     * Remove all value change listeners.
     *
     * @return Number of value change listeners that were removed.
     */
    public int removeAllValueChangeListeners() {
        // Remember the number of value change listeners
        final int listenerCount = this.valueChangeListeners.size();

        // Clear the list of listeners
        this.valueChangeListeners.clear();

        // Return the number of cleared listeners
        return listenerCount;
    }

    /**
     * Fire the value change listener event.
     *
     * @param newValue New value.
     */
    public void fireValueChangeListenerEvent(Object newValue) {
        // Fire each registered listener
        this.valueChangeListeners.forEach(valueChangeListener -> valueChangeListener.onValueChange(newValue));
    }
}
