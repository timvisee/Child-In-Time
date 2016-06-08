package me.childintime.childintime.gui.component.property.action;

import me.childintime.childintime.gui.component.property.AbstractPropertyField;

public class ClearAction extends AbstractAction {

    /**
     * Constructor.
     *
     * @param propertyField Property field.
     */
    public ClearAction(AbstractPropertyField propertyField) {
        // Construct the super
        super(propertyField::clear, "✖", "Clear");
    }

    @Override
    public void onNullChange(boolean _null) {
        // Set the enabled state of the button, if attached
        if(getButton() != null)
            getButton().setEnabled(!_null);
    }
}
