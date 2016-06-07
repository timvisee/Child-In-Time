package me.childintime.childintime.gui.component.property.context;

import me.childintime.childintime.gui.component.property.DatePropertyField;

import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class ContextDateAction extends TextAction {

    /**
     * Date property field.
     */
    private DatePropertyField propertyField;

    /**
     * Constructor.
     */
    public ContextDateAction(DatePropertyField propertyField) {
        // Construct the super with the name
        super("Choose Date...");

        // Set the property field instance
        this.propertyField = propertyField;
    }

    /**
     * Action listener.
     *
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        /// Show the date chooser
        this.propertyField.showDateChooser();
    }
}
