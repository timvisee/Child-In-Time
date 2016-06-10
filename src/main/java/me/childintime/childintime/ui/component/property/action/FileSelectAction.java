package me.childintime.childintime.ui.component.property.action;

import me.childintime.childintime.ui.component.property.FilePropertyField;

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
