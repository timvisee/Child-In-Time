package me.childintime.childintime.gui.component.property;

import javax.swing.*;

public abstract class AbstractPropertyField extends JComponent {

    /**
     * Flag which defines whether a null value is allowed.
     */
    private boolean allowNull;

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public AbstractPropertyField(boolean allowNull) {
        this.allowNull = allowNull;
    }

    /**
     * Get the property field value.
     *
     * @return Property field value.
     */
    public abstract Object getValue();

    public abstract void setValue(Object value);

    /**
     * Check whether a null value is allowed.
     *
     * @return True if allowed, false if not.
     */
    public boolean isNullAllowed() {
        return this.allowNull;
    }

    /**
     * Set whether a null value is allowed.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public void setNullAllowed(boolean allowNull) {
        this.allowNull = allowNull;
    }

    /**
     * Check whether the value is null.
     *
     * @return True if the value is null, false if not.
     */
    public boolean isNull() {
        return getValue() == null;
    }
}