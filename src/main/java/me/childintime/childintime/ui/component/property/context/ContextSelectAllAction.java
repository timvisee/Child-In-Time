package me.childintime.childintime.ui.component.property.context;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class ContextSelectAllAction extends TextAction {

    /**
     * Action key stroke.
     */
    public static final String ACTION_KEY_STROKE = "control A";

    /**
     * Constructor.
     */
    public ContextSelectAllAction() {
        // Construct the super with the name
        super("Select All");

        // Set the shortcut key
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(ACTION_KEY_STROKE));
    }

    /**
     * Action listener.
     *
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        // Get the component
        JTextComponent component = getFocusedComponent();

        // Select everything inside the component
        component.selectAll();
        component.requestFocusInWindow();
    }
}
