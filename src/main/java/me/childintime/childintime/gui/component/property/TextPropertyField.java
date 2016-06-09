package me.childintime.childintime.gui.component.property;

import me.childintime.childintime.gui.component.property.action.TextCopyAction;
import me.childintime.childintime.gui.component.property.action.TextCutAction;
import me.childintime.childintime.gui.component.property.action.TextPasteAction;
import me.childintime.childintime.gui.component.property.context.ContextClearAction;
import me.childintime.childintime.gui.component.property.context.ContextSelectAllAction;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.*;

public class TextPropertyField extends AbstractPropertyField {

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
     * Text field.
     */
    protected JTextField textField;

    /**
     * Context menu.
     */
    protected JPopupMenu contextMenu;

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
        // Call the super
        super(allowNull);

        // Build the UI
        buildUi();

        // Set the text value
        setText(value);
    }

    @Override
    public void buildActionList() {
        // Add the text cut/copy/paste actions
        this.actionsList.add(new TextCutAction());
        this.actionsList.add(new TextCopyAction());
        this.actionsList.add(new TextPasteAction());

        super.buildActionList();
    }

    @Override
    protected JComponent buildUiField() {
        // Build the text field
        this.textField = buildUiTextField();

        // Link the text field listeners
        this.textField.addMouseListener(new MouseListener() {
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
        this.textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                // Disable the property field if it's currently empty
                disableIfEmpty();
            }
        });

        // Set the field back to null when the escape key is pressed
        this.textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        this.textField.getActionMap().put("Escape", new AbstractAction() {
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
            this.textField.setComponentPopupMenu(this.contextMenu);

        // Return the text field
        return this.textField;
    }

    /**
     * Build the text field.
     *
     * @return Build the text field.
     */
    protected JTextField buildUiTextField() {
        return new JTextField();
    }

    /**
     * Build the UI context menu for this component.
     * @return Context menu, or null.
     */
    protected JPopupMenu buildUiMenu() {
        // Create the context menu
        JPopupMenu menu = new JPopupMenu();

        // Create and set up the cut action
        Action cutAction = new DefaultEditorKit.CutAction();
        cutAction.putValue(Action.NAME, "Cut");
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        menu.add(cutAction);

        // Create and set up the copy action
        Action copyAction = new DefaultEditorKit.CopyAction();
        copyAction.putValue(Action.NAME, "Copy");
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        menu.add(copyAction);

        // Create and set up the paste action
        Action pasteAction = new DefaultEditorKit.PasteAction();
        pasteAction.putValue(Action.NAME, "Paste");
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        menu.add(pasteAction);

        // Create and set up the select all action
        menu.add(new ContextSelectAllAction());

        // Add a separator
        menu.addSeparator();

        // Create and set up the clear action
        menu.add(new ContextClearAction(this));

        // Return the menu
        return menu;
    }

    /**
     * Get the text field.
     *
     * @return Text field.
     */
    public JTextField getTextField() {
        return this.textField;
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
     * Get the property field text value.
     * If the field is null, null will be returned.
     * If null is not allowed, an empty string will be returned instead of a null value.
     *
     * @return Text value, or null.
     */
    public String getText() {
        return !isNull() ? this.textField.getText() : (isNullAllowed() ? null : "");
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

        // Set the null state
        if(text == null)
            setNull(true);
        else if(isNull())
            setNull(false);

        // Set the text field text
        if(!isNull())
            this.textField.setText(text);
    }

    @Override
    public void setNull(boolean _null) {
        // Set null in the super
        super.setNull(_null);

        // Update the enabled state of the text field
        this.textField.setEnabled(!_null);

        // Set the field text to the null placeholder if it's set to null
        if(_null)
            this.textField.setText(this.nullPlaceholderText);
    }

    @Override
    public void clear() {
        // Transfer focus to another component
        //this.textField.transferFocus();

        // Clear the field
        SwingUtilities.invokeLater(() -> {
            // Set the field to null, or an empty string if null isn't allowed
            if(isNullAllowed())
                setNull(true);
            else
                setText("");
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
        if(!allowEmpty && !this.textField.hasFocus())
            disableIfEmpty();
    }

    @Override
    public boolean isEmpty() {
        // Call the super
        if(super.isEmpty())
            return true;

        // Check whether the string is empty
        return getText().isEmpty();
    }

    @Override
    public void enableField() {
        // Enable the field if it's disabled because it's value is null
        if(isNull())
            this.setText("");

        // Focus the field and select all
        this.textField.grabFocus();
        this.textField.selectAll();
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
