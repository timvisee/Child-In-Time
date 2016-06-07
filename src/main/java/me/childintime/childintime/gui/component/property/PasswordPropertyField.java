package me.childintime.childintime.gui.component.property;

import javax.swing.*;

public class PasswordPropertyField extends TextPropertyField {

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed.
     */
    public PasswordPropertyField(boolean allowNull) {
        super(allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed.
     */
    public PasswordPropertyField(String value, boolean allowNull) {
        super(value, allowNull);
    }

    @Override
    protected JTextField buildUiTextField() {
        return new JPasswordField();
    }
}
