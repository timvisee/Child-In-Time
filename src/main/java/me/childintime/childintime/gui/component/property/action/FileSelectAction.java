package me.childintime.childintime.gui.component.property.action;

import me.childintime.childintime.gui.component.property.FilePropertyField;

public class FileSelectAction extends AbstractAction {

    /**
     * Constructor.
     *
     * @param propertyField Property field.
     */
    public FileSelectAction(FilePropertyField propertyField) {
        // Construct the super
        super(propertyField::showFileChooserDialog, "…", "Browse...");
    }

    @Override
    public void onNullChange(boolean _null) { }
}
