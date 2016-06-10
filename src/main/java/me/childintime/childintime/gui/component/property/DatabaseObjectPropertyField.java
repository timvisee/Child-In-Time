package me.childintime.childintime.gui.component.property;

import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;

import javax.swing.*;
import java.awt.event.*;

public class DatabaseObjectPropertyField extends AbstractPropertyField {

    /**
     * True if an empty value is allowed.
     * If this is set to false, an empty string will be converted to null when focus is lost.
     * The field will be left empty if null is not allowed.
     */
    private boolean allowEmpty = true;

    /**
     * Database object manager.
     */
    final private AbstractDatabaseObjectManager manager;

    /**
     * Object field.
     */
    private JComboBox<AbstractDatabaseObject> comboBox;

    /**
     * Constructor.
     *
     * @param manager Database object manager.
     * @param allowNull True if null is allowed, false if not.
     */
    public DatabaseObjectPropertyField(AbstractDatabaseObjectManager manager, boolean allowNull) {
        // Call an alias constructor
        this((Object) manager, allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed, false if not.
     */
    public DatabaseObjectPropertyField(AbstractDatabaseObject value, boolean allowNull) {
        // Call the super
        this((Object) value, allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Value or manager.
     * @param allowNull True if null is allowed, false if not.
     */
    private DatabaseObjectPropertyField(Object value, boolean allowNull) {
        // Call the super
        super(allowNull);

        // Set the manager
        if(value instanceof AbstractDatabaseObjectManager)
            this.manager = (AbstractDatabaseObjectManager) value;
        else if(value instanceof AbstractDatabaseObject)
            this.manager = ((AbstractDatabaseObject) value).getManifest().getManagerInstance();
        else
            throw new IllegalArgumentException("Invalid value.");

        // Build the UI
        buildUi();

        // Set the selected item
        if(value instanceof AbstractDatabaseObject)
            setSelected((AbstractDatabaseObject) value);
        else
            setSelected(null);
    }

    @Override
    protected JComponent buildUiField() {
        // Build the combo box
        this.comboBox = new JComboBox<>(this.manager.getObjects().toArray(new AbstractDatabaseObject[]{}));

        // Link the text field listeners
        this.comboBox.addMouseListener(new MouseListener() {
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
        this.comboBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                // Disable the property field if it's currently empty
                disableIfEmpty();
            }
        });

        // Set the field back to null when the escape key is pressed
        this.comboBox.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        this.comboBox.getActionMap().put("Escape", new AbstractAction() {
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
        return this.comboBox;
    }

    /**
     * Get the combo box.
     *
     * @return combo box.
     */
    public JComboBox<AbstractDatabaseObject> getComboBox() {
        return this.comboBox;
    }

    @Override
    public AbstractDatabaseObject getValue() {
        return getSelected();
    }

    @Override
    public void setValue(Object value) {
        // Set the selected database object, or null
        setSelected((AbstractDatabaseObject) value);
    }

    /**
     * Get the property field database object value.
     * If the field is null, null will be returned.
     *
     * @return Abstract database object, or null.
     */
    public AbstractDatabaseObject getSelected() {
        return isNull() ? null : (AbstractDatabaseObject) this.comboBox.getSelectedItem();
    }

    /**
     * Set the selected value.
     *
     * @param selected Selected value.
     */
    public void setSelected(AbstractDatabaseObject selected) {
        // Make sure null is allowed
        if(selected == null && !isNullAllowed())
            throw new IllegalArgumentException("Null value not allowed");

        // Set the null state
        if(selected == null)
            setNull(true);
        else
            this.comboBox.setSelectedItem(selected);
    }

    @Override
    public void setNull(boolean _null) {
        // Set null in the super
        super.setNull(_null);

        // Update the enabled state of both components
        this.comboBox.setEnabled(!_null);
    }

    @Override
    public void clear() {
        // Transfer focus to another component
        this.comboBox.transferFocus();

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
        if(!allowEmpty && !this.comboBox.hasFocus())
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
        this.comboBox.grabFocus();
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
