package me.childintime.childintime.gui.component.property;

import com.timvisee.swingtoolbox.border.ComponentBorder;
import me.childintime.childintime.util.Platform;

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
    public JPanel getActionButtonPanel() {
        // Create the super action button panel
        JPanel actionButtonPanel = super.getActionButtonPanel();

        // Create the clear button
        this.browseButton = new JButton("…");

        // Define the size of the clear button
        this.browseButton.setPreferredSize(this.clearButton.getPreferredSize());
        this.browseButton.setMinimumSize(this.clearButton.getPreferredSize());
        this.browseButton.setMaximumSize(this.clearButton.getPreferredSize());
        this.browseButton.setSize(this.clearButton.getPreferredSize());
        this.browseButton.setBorder(null);

        // Fix button styling on Mac OS X
        if(Platform.isMacOsX()) {
            this.browseButton.putClientProperty("JButton.sizeVariant", "mini");
            this.browseButton.putClientProperty("JButton.buttonType", "square");
            this.browseButton.setMargin(new Insets(0, 0, 0, 0));
            this.browseButton.setFont(new Font(this.browseButton.getFont().getFontName(), Font.PLAIN, this.browseButton.getFont().getSize() - 3));
        }

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

        // Add the button
        actionButtonPanel.add(this.browseButton);

        // Return the action button panel
        return actionButtonPanel;
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
