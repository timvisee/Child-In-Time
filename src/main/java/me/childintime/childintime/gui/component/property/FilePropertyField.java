package me.childintime.childintime.gui.component.property;

import me.childintime.childintime.gui.component.property.action.FileSelectAction;
import me.childintime.childintime.gui.component.property.context.ContextFileAction;

import javax.swing.*;
import java.io.File;

public class FilePropertyField extends TextPropertyField {

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed.
     */
    public FilePropertyField(boolean allowNull) {
        // Call the super
        super(allowNull);

        // Do not allow empty values
        setEmptyAllowed(false);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed.
     */
    public FilePropertyField(String value, boolean allowNull) {
        // Call the super
        super(value, allowNull);

        // Do not allow empty values
        setEmptyAllowed(false);
    }

    /**
     * Constructor.
     *
     * @param file File.
     * @param allowNull True if null is allowed.
     */
    public FilePropertyField(File file, boolean allowNull) {
        this(file.getAbsolutePath(), allowNull);
    }

    @Override
    public void buildActionList() {
        // Add the file browse action
        this.actionsList.add(new FileSelectAction(this));

        // Call the super
        super.buildActionList();
    }

    @Override
    protected JPopupMenu buildUiMenu() {
        // Build the super context menu
        JPopupMenu contextMenu = super.buildUiMenu();

        // Return null if the super was null
        if(contextMenu == null)
            return null;

        // Add a separator
        contextMenu.addSeparator();

        // Add the file selection context menu item
        contextMenu.add(new ContextFileAction(this));

        // Return the context menu
        return contextMenu;
    }

    /**
     * Show the file chooser dialog.
     */
    public void showFileChooserDialog() {
        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(getFile());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Show the save dialog, to select a file
        fileChooser.showSaveDialog(this);

        // Get the selected file
        File selectedFile = fileChooser.getSelectedFile();

        // Update the selected file if it isn't null
        if(selectedFile != null)
            setFile(selectedFile);
    }

    /**
     * Get the file.
     *
     * @return File, or null.
     */
    public File getFile() {
        // Return null if the value is null
        if(isNull() || isEmpty())
            return null;

        // Return the file
        return new File(getText());
    }

    /**
     * Set the file.
     *
     * @param file File.
     */
    public void setFile(File file) {
        // Set the text
        if(file != null)
            setText(file.getAbsolutePath());
        else
            setNull(true);
    }
}
