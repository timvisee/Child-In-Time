package me.childintime.childintime.gui.component.property;

import com.timvisee.swingtoolbox.border.ComponentBorder;
import me.childintime.childintime.gui.component.property.context.ContextClearAction;
import me.childintime.childintime.gui.component.property.context.ContextSelectAllAction;
import me.childintime.childintime.util.Platform;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.*;

public class BooleanPropertyField extends AbstractPropertyField {

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
     * Boolean field.
     */
    protected JCheckBox checkBox;

    /**
     * Context menu.
     */
    protected JPopupMenu contextMenu;

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

        // Build the context menu, and attach it to the component
        this.contextMenu = buildUiMenu();
        if(this.contextMenu != null)
            this.checkBox.setComponentPopupMenu(this.contextMenu);

        // Return the checkbox
        return this.checkBox;
    }

    /**
     * Build the UI context menu for this component.
     * @return Context menu, or null.
     */
    protected JPopupMenu buildUiMenu() {
        // Create the context menu
        JPopupMenu menu = new JPopupMenu();

        // Create and set up the clear action
        menu.add(new ContextClearAction(this));

        // Return the menu
        return menu;
    }

//    /**
//     * Get the action button panel.
//     *
//     * @return Action button panel.
//     */
//    public JPanel getActionButtonPanel() {
//        // Create the action button panel
//        JPanel actionButtonPanel = new JPanel();
//        actionButtonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 1, 1));
//        actionButtonPanel.setOpaque(false);
//
//        // Create the clear button
//        this.clearButton = new JButton(BUTTON_CLEAR_TEXT);
//        this.clearButton.setToolTipText(BUTTON_CLEAR_TOOLTIP);
//        this.clearButton.addActionListener(e -> {
//            // Set the value to null
//            setNull(true);
//        });
//
//        // Define the size of the clear button
//        final int buttonSize = this.checkBox.getPreferredSize().height - 4;
//        final Dimension buttonDimensions = new Dimension(buttonSize, buttonSize);
//        this.clearButton.setPreferredSize(buttonDimensions);
//        this.clearButton.setMinimumSize(buttonDimensions);
//        this.clearButton.setMaximumSize(buttonDimensions);
//        this.clearButton.setSize(buttonDimensions);
//        this.clearButton.setBorder(null);
//        this.clearButton.setFocusable(false);
//
//        // Fix button styling on Mac OS X
//        if(Platform.isMacOsX()) {
//            this.clearButton.putClientProperty("JButton.sizeVariant", "mini");
//            this.clearButton.putClientProperty("JButton.buttonType", "square");
//            this.clearButton.setMargin(new Insets(0, 0, 0, 0));
//            this.clearButton.setFont(new Font(this.clearButton.getFont().getFontName(), Font.PLAIN, this.clearButton.getFont().getSize() - 2));
//        }
//
//        // Add the button to the panel
//        actionButtonPanel.add(this.clearButton);
//
//        // Return the button panel
//        return actionButtonPanel;
//    }

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
        if(!allowEmpty && !this.checkBox.hasFocus())
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
        this.checkBox.grabFocus();
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
