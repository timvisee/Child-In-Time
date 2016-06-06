package me.childintime.childintime.gui.component.property;

public class PasswordPropertyField extends TextPropertyField {

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed.
     */
    public PasswordPropertyField(boolean allowNull) {
        this(allowNull ? null : "", allowNull);
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
}
