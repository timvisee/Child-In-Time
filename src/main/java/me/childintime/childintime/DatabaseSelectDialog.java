package me.childintime.childintime;

import me.childintime.childintime.util.Platform;

import javax.swing.*;
import java.awt.*;

public class DatabaseSelectDialog extends JDialog {

    /**
     * Unique serial version.
     */
    private static final long serialVersionUID = 9067981259873285740L;

    /**
     * Status label.
     */
    private JLabel statusLabel;

    /**
     * Dropdown box component.
     */
    private JComboBox comboBox;

    /**
     * Constructor.
     */
    public DatabaseSelectDialog() {
        this(null, App.APP_NAME, true);
    }

    /**
     * Constructor.
     *
     * @param owner Owner window, or null.
     * @param title Progress dialog title.
     */
    public DatabaseSelectDialog(Window owner, String title) {
        // Construct the super class
        super(owner, title);

        // Set the modality type if an owner is set
        if(owner != null)
            setModalityType(ModalityType.APPLICATION_MODAL);

        // Build the dialog
        buildUI();

        // Configure the close button behaviour
        // TODO: Change this?
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Configure the window sizes
        configureSize();

        // Set the dialog location to the center of the screen
        setLocationRelativeTo(owner);

        // Bring the window to the front
        toFront();
    }

    /**
     * Properly configure the window sizes for the current content.
     */
    private void configureSize() {
        // Pack everything
        pack();

        // Get the maximum height
        final int maxHeight = getSize().height;

        // Configure the sizes
        setMinimumSize(new Dimension(getMinimumSize()));
        setMaximumSize(new Dimension(9999, maxHeight));
        setPreferredSize(new Dimension(getSize().width + 100, maxHeight));
        setSize(new Dimension(getSize().width + 100, maxHeight));
    }

    /**
     * Constructor.
     *
     * @param owner Owner window, or null.
     * @param title Progress dialog title.
     * @param show True to immediately show the progress dialog.
     */
    public DatabaseSelectDialog(Window owner, String title, boolean show) {
        // Construct
        this(owner, title);

        // Show the progress dialog if specified
        if(show)
            setVisible(true);
    }

    /**
     * Build the progress dialog UI.
     */
    private void buildUI() {
        // Create the base panel
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        container.setLayout(new GridBagLayout());

        // Create a grid bag constraints instance
        GridBagConstraints c = new GridBagConstraints();

        // Create and add database selection label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 8, 0);
        container.add(new JLabel("Please select a database:"), c);

        // Create database selector
        DatabaseType[] options = {me.childintime.childintime.DatabaseType.INTEGRATED, me.childintime.childintime.DatabaseType.REMOTE};
        comboBox = new JComboBox(options);
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 20));

        // Add database selector
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        container.add(comboBox, c);

        // Create database configuration button
        JButton configureButton = new JButton("...");
        configureButton.setPreferredSize(new Dimension(comboBox.getPreferredSize().height, comboBox.getPreferredSize().height));

        // Add database configuration button
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(0, 8, 0, 0);
        container.add(configureButton, c);

        // Create the commit buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 8, 8));
        JButton continueButton = new JButton("Continue");
        JButton quitButton = new JButton("Quit");
        if(!Platform.isMacOsX()) {
            buttonPanel.add(continueButton);
            buttonPanel.add(quitButton);
        } else {
            buttonPanel.add(quitButton);
            buttonPanel.add(continueButton);
        }

        // Add the commit buttons panel
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(8, 50, 0, 0);
        container.add(buttonPanel, c);

        // Add the container to the dialog
        add(container);

        // Pack everything
        pack();

        // Request focus on the continue button
        // TODO: Move this somewhere else!
        continueButton.requestFocus();
    }
}
