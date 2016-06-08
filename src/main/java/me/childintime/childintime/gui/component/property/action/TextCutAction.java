package me.childintime.childintime.gui.component.property.action;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

public class TextCutAction extends AbstractAction {

    /**
     * Constructor.
     */
    public TextCutAction() {
        // Construct the super
        super(() -> {}, "Cut", "Cut");

        // Hide the button
        setShowButton(false);

        // Create and configure the action
        Action cutAction = new DefaultEditorKit.CutAction();
        cutAction.putValue(Action.NAME, "Cut");
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        setContextMenuAction(cutAction);
    }

    @Override
    public void onNullChange(boolean _null) { }
}
