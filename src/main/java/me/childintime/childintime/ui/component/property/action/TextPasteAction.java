package me.childintime.childintime.ui.component.property.action;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

public class TextPasteAction extends AbstractAction {

    /**
     * Constructor.
     */
    public TextPasteAction() {
        // Construct the super
        super(() -> {}, "Paste", "Paste");

        // Hide the button
        setShowButton(false);

        // Create and configure the action
        Action copyAction = new DefaultEditorKit.PasteAction();
        copyAction.putValue(Action.NAME, "Paste");
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        setContextMenuAction(copyAction);
    }

    @Override
    public void onNullChange(boolean _null) { }
}
