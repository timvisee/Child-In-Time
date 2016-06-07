package me.childintime.childintime.gui.component.property.context;

import me.childintime.childintime.gui.component.property.FilePropertyField;

import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class ContextFileAction extends TextAction {

    /**
     * Context action name.
     */
    public static final String ACTION_NAME = "Browse...";

    /**
     * Date property field.
     */
    private FilePropertyField propertyField;

    /**
     * Constructor.
     */
    public ContextFileAction(FilePropertyField propertyField) {
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
        /// Show the file chooser
        this.propertyField.showFileChooserDialog();
    }
}
