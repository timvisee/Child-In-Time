package me.childintime.childintime.gui.component.property;

import javax.swing.*;

public class PasswordPropertyField extends TextPropertyField {

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed.
     */
    public PasswordPropertyField(boolean allowNull) {
        super(allowNull ? null : "", allowNull, new JPasswordField(""));
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed.
     */
    public PasswordPropertyField(String value, boolean allowNull) {
        super(value, allowNull, new JPasswordField(""));
    }
}
