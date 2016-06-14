package me.childintime.childintime.ui.component.property.action;

import me.childintime.childintime.ui.component.property.EntityPropertyField;

public class EntitySelectAction extends AbstractAction {

    /**
     * Constructor.
     *
     * @param propertyField Property field.
     */
    public EntitySelectAction(EntityPropertyField propertyField) {
        // Construct the super
        super(propertyField::showSelect, "…", "Select...");

        // Hide the button
        setShowButton(false);
    }

    @Override
    public void onNullChange(boolean _null) { }
}
