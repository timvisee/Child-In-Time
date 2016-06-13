package me.childintime.childintime.ui.component.property;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.ui.selector.EntityListSelectorDialog;
import me.childintime.childintime.util.swing.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EntityPropertyField extends AbstractPropertyField {

    /**
     * True if an empty value is allowed.
     * If this is set to false, an empty string will be converted to null when focus is lost.
     * The field will be left empty if null is not allowed.
     */
    private boolean allowEmpty = true;

    /**
     * The selected abstract entity.
     */
    private AbstractEntity selected = null;

    /**
     * Entity manager.
     */
    final private AbstractEntityManager manager;

    /**
     * Entity field (button).
     */
    private JButton button;

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     * @param allowNull True if null is allowed, false if not.
     */
    public EntityPropertyField(AbstractEntityManager manager, boolean allowNull) {
        // Call an alias constructor
        this((Object) manager, allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed, false if not.
     */
    public EntityPropertyField(AbstractEntity value, boolean allowNull) {
        // Call the super
        this((Object) value, allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Value or manager.
     * @param allowNull True if null is allowed, false if not.
     */
    private EntityPropertyField(Object value, boolean allowNull) {
        // Call the super
        super(allowNull);

        // Set the manager
        if(value instanceof AbstractEntityManager)
            this.manager = (AbstractEntityManager) value;
        else if(value instanceof AbstractEntity)
            this.manager = ((AbstractEntity) value).getManifest().getManagerInstance();
        else
            throw new IllegalArgumentException("Invalid value.");

        // Build the UI
        buildUi();

        // Set the selected item
        if(value instanceof AbstractEntity)
            setSelected((AbstractEntity) value);
        else
            setSelected(null);
    }

    @Override
    protected JComponent buildUiField() {
        // Build the combo box
        this.button = new JButton();

        // Set the button text alignment, the margin, and the layout for later use
        this.button.setHorizontalAlignment(SwingConstants.LEFT);
        this.button.setMargin(new Insets(1, 4, 1, 1));
        this.button.setLayout(new BorderLayout());

        // Add an arrow icon to the button
        final JLabel arrowLabel = new JLabel("▾ ");
        this.button.add(arrowLabel, BorderLayout.EAST);

        // Validate the button, to update everything
        this.button.validate();

        // Create a button action
        this.button.addActionListener(e -> {
            // Show the selection dialog
            final AbstractEntity newSelected = EntityListSelectorDialog.showDialog(SwingUtils.getComponentWindow(this), this.manager, this.selected);

            // Set the selected entity if one is selected
            if(newSelected != null)
                setSelected(newSelected);
        });

        // Link the text field listeners
        this.button.addMouseListener(new MouseListener() {
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
        this.button.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                // Disable the property field if it's currently empty
                disableIfEmpty();
            }
        });

        // Set the field back to null when the escape key is pressed
        this.button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        this.button.getActionMap().put("Escape", new AbstractAction() {
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
        return this.button;
    }

    /**
     * Get the button.
     *
     * @return Button.
     */
    public JButton getButton() {
        return this.button;
    }

    @Override
    public AbstractEntity getValue() {
        return getSelected();
    }

    @Override
    public void setValue(Object value) {
        // Set the selected entity, or null
        setSelected((AbstractEntity) value);
    }

    /**
     * Get the property field entity value.
     * If the field is null, null will be returned.
     *
     * @return Abstract entity, or null.
     */
    public AbstractEntity getSelected() {
        return isNull() ? null : this.selected;
    }

    /**
     * Set the selected value.
     *
     * @param selected Selected value.
     */
    public void setSelected(AbstractEntity selected) {
        // Make sure null is allowed
        if(selected == null && !isNullAllowed())
            throw new IllegalArgumentException("Null value not allowed");

        // Set the null state
        if(selected == null)
            setNull(true);
        else {
            // Set the selected value
            this.selected = selected;

            // Set the button text
            this.button.setText(this.selected.toString());
        }
    }

    @Override
    public void setNull(boolean _null) {
        // Set null in the super
        super.setNull(_null);

        // Update the enabled state of both components
        this.button.setEnabled(!_null);
    }

    @Override
    public void clear() {
        // Transfer focus to another component
        this.button.transferFocus();

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
        if(!allowEmpty && !this.button.hasFocus())
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
        this.button.grabFocus();
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
        return this.selected == null;
    }
}
