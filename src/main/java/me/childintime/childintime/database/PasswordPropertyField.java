package me.childintime.childintime.database;

public class PasswordPropertyField extends TextPropertyField {

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed.
     */
    public PasswordPropertyField(boolean allowNull) {
        super(allowNull ? null : "", allowNull, true);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed.
     */
    public PasswordPropertyField(String value, boolean allowNull) {
        super(value, allowNull, true);
    }
}
