package me.childintime.childintime.gui.component.property.action;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

public class TextCopyAction extends AbstractAction {

    /**
     * Constructor.
     */
    public TextCopyAction() {
        // Construct the super
        super(() -> {}, "Copy", "Copy");

        // Hide the button
        setShowButton(false);

        // Create and configure the action
        Action copyAction = new DefaultEditorKit.CopyAction();
        copyAction.putValue(Action.NAME, "Copy");
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        setContextMenuAction(copyAction);
    }

    @Override
    public void onNullChange(boolean _null) { }
}
