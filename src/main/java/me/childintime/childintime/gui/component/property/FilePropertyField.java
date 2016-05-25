package me.childintime.childintime.gui.component.property;

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
        this.clearButton.setPreferredSize(new Dimension(this.clearButton.getPreferredSize().height, this.clearButton.getPreferredSize().height));
        this.clearButton.setMinimumSize(new Dimension(this.clearButton.getPreferredSize().height, this.clearButton.getPreferredSize().height));
        this.clearButton.setMaximumSize(new Dimension(this.clearButton.getPreferredSize().height, this.clearButton.getPreferredSize().height));
        this.clearButton.setSize(new Dimension(this.clearButton.getPreferredSize().height, this.clearButton.getPreferredSize().height));
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

        // Add the components
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0;
        add(this.browseButton, c);
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
