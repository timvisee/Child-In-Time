package me.childintime.childintime.gui.component.property.context;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class ContextDynamicAction extends AbstractAction {

    /**
     * Action.
     */
    private me.childintime.childintime.gui.component.property.action.AbstractAction action;

    /**
     * Constructor.
     */
    public ContextDynamicAction(me.childintime.childintime.gui.component.property.action.AbstractAction action) {
        // Construct the super with the name
        super(action.getDescription());

        // Set the property field instance
        this.action = action;
    }

    /**
     * Action listener.
     *
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        this.action.run();
    }
}
