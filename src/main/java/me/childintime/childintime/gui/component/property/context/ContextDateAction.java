package me.childintime.childintime.gui.component.property.context;

import me.childintime.childintime.gui.component.property.DatePropertyField;

import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class ContextDateAction extends TextAction {

    /**
     * Context action name.
     */
    public static final String ACTION_NAME = "Choose date...";

    /**
     * Date property field.
     */
    private DatePropertyField propertyField;

    /**
     * Constructor.
     */
    public ContextDateAction(DatePropertyField propertyField) {
        // Construct the super with the name
        super(ACTION_NAME);

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
