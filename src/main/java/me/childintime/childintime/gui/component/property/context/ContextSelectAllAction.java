package me.childintime.childintime.gui.component.property.context;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class ContextSelectAllAction extends TextAction {

    /**
     * Constructor.
     */
    public ContextSelectAllAction() {
        // Construct the super with the name
        super("Select All");

        // Set the shortcut key
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
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
