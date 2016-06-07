package me.childintime.childintime.gui.component.property;

import javax.swing.*;

public abstract class AbstractPropertyField extends JComponent {

    /**
     * Defines whether the property field is set to null.
     */
    private boolean _null = false;

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
        // Set whether null is allowed
        this.allowNull = allowNull;

        // Set the component name
        this.setName(getComponentName());
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
     * Check whether the property field is set to null.
     *
     * @return True if the property field is set to null.
     */
    public boolean isNull() {
        // TODO: Only return true if null values are allowed?
        return this._null;
    }

    /**
     * Set whether the property field is set to null.
     *
     * @param _null True if null, false if not.
     */
    public void setNull(boolean _null) {
        this._null = _null;
    }

    /**
     * Get the component name.
     *
     * @return Component name.
     */
    protected abstract String getComponentName();
}