package me.childintime.childintime.gui.component.property;

import me.childintime.childintime.gui.component.property.action.FileSelectAction;
import me.childintime.childintime.gui.component.property.context.ContextFileAction;
import me.childintime.childintime.util.file.PathUtils;

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

    /**
     * Check whether the current path is valid/parsable.
     * If the text box containing the file path is empty or null, false is returned.
     *
     * @return True if the path is valid, false if not.
     */
    public boolean isValidPath() {
        // Return false if field is empty
        if(isEmpty())
            return false;

        // Check whether the path is parsable
        return PathUtils.isValidPath(getText());
    }

    @Override
    public boolean isValid() {
        // Make sure the super is valid
        if(!super.isValid())
            return false;

        // Make sure the path isn't empty
        if(isEmpty())
            return false;

        // Make sure the path is valid/parsable
        return isValidPath();
    }
}
