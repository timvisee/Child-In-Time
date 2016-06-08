package me.childintime.childintime.gui.component.property.action;

import me.childintime.childintime.gui.component.property.DatePropertyField;

import javax.swing.*;

public class DateSelectAction extends AbstractAction {

    /**
     * Icon resource path.
     */
    public static final String RESOURCE_ICON_PATH = "/com/toedter/calendar/images/JDateChooserColor16.gif";

    /**
     * Constructor.
     *
     * @param propertyField Property field.
     */
    public DateSelectAction(DatePropertyField propertyField) {
        // Construct the super
        super(propertyField::showDateChooser, "…", "Choose date...");

        // Set the button icon
        setIcon(new ImageIcon(this.getClass().getResource(RESOURCE_ICON_PATH)));
    }

    @Override
    public void onNullChange(boolean _null) { }
}
