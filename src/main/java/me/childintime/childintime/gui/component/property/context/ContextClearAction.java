package me.childintime.childintime.gui.component.property.context;

import me.childintime.childintime.gui.component.property.AbstractPropertyField;

import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class ContextClearAction extends TextAction {

    /**
     * Property field.
     */
    private AbstractPropertyField propertyField;

    /**
     * Constructor.
     */
    public ContextClearAction(AbstractPropertyField propertyField) {
        // Construct the super with the name
        super("Clear");

        // Set the property field instance
        this.propertyField = propertyField;
    }

    /**
     * Action listener.
     *
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        // Clear the property field
        this.propertyField.setNull(true);
    }
}
