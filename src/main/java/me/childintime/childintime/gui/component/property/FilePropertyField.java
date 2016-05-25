package me.childintime.childintime.gui.component.property;

import com.timvisee.swingtoolbox.border.ComponentBorder;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FilePropertyField extends TextPropertyField {

    /**
     * Browse button.
     */
    private JButton browseButton;

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed.
     */
    public FilePropertyField(boolean allowNull) {
        this(allowNull ? null : "", allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed.
     */
    public FilePropertyField(String value, boolean allowNull) {
        this(value, allowNull, false);
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

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed.
     */
    public FilePropertyField(String value, boolean allowNull, boolean isPassword) {
        // Call the super
        super(value, allowNull, isPassword);

        // Do not allow empty values
        setEmptyAllowed(false);
    }

    /**
     * Constructor.
     *
     * @param file File.
     * @param allowNull True if null is allowed.
     */
    public FilePropertyField(File file, boolean allowNull, boolean isPassword) {
        this(file.getAbsolutePath(), allowNull, isPassword);
    }

    @Override
    protected void buildUi() {
        // Build the UI
        super.buildUi();

        // Create the grid bag constraints
        GridBagConstraints c = new GridBagConstraints();

        // Create the clear button
        this.browseButton = new JButton("...");

        // Define the size of the clear button
        final int buttonSize = this.clearButton.getPreferredSize().height;
        final Dimension buttonDimensions = new Dimension(buttonSize, buttonSize);
        this.clearButton.setPreferredSize(buttonDimensions);
        this.clearButton.setMinimumSize(buttonDimensions);
        this.clearButton.setMaximumSize(buttonDimensions);
        this.clearButton.setSize(buttonDimensions);
        this.clearButton.setBorder(null);

        // Add an action listener to the browse button
        this.browseButton.addActionListener(e -> {
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
        });

        // Create a component border and install the action buttons into the text field
        ComponentBorder cb = new ComponentBorder(this.browseButton, ComponentBorder.Edge.RIGHT, ComponentBorder.CENTER);
        cb.setGap(2);
        cb.setAdjustInsets(true);
        cb.install(this.textField);
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
