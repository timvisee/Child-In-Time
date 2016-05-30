package me.childintime.childintime.gui.window;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.configuration.AbstractDatabase;
import me.childintime.childintime.database.configuration.gui.window.DatabaseManagerDialog;
import me.childintime.childintime.database.configuration.gui.window.DatabaseModifyDialog;
import me.childintime.childintime.util.Platform;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LoginDialog extends JDialog {

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
    private JComboBox<AbstractDatabase> comboBox;

    /**
     * Defines whether the client logged in successfully.
     */
    private boolean success = false;

    /**
     * Continue button.
     */
    private JButton continueButton;

    /**
     * Quit button.
     */
    private JButton quitButton;

    /**
     * Configure button. (...)
     */
    private JButton configureButton;

    /**
     * Constructor.
     */
    public LoginDialog() {
        this(null, App.APP_NAME, true);
    }

    /**
     * Constructor.
     *
     * @param owner Owner window, or null.
     * @param title Progress dialog title.
     */
    public LoginDialog(Window owner, String title) {
        // Construct the super class
        super(owner, title);

        // Set the modality type if an owner is set
        if(owner != null)
            setModalityType(ModalityType.APPLICATION_MODAL);

        // Make this dialog a modal
        setModal(true);

        // Build the dialog UI
        buildUI();

        // Link all components
        linkComponents();

        // Configure the close button behaviour
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Configure the window sizes
        configureSize();

        // Set the dialog location to the center of the screen
        setLocationRelativeTo(owner);

        // Bring the window to the front
        toFront();
    }

    /**
     * Show the login dialog.
     *
     * @param owner Window owner.
     *
     * @return The database configuration the user selected, or null if the login was cancelled.
     */
    public static AbstractDatabase showDialog(Window owner) {
        // Create a database selector dialog instance
        LoginDialog dialog = new LoginDialog(owner, App.APP_NAME);

        // Show the database selector dialog
        dialog.setVisible(true);

        // Return the result
        return dialog.isSuccess() ? dialog.getSelectedDatabase() : null;
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
    public LoginDialog(Window owner, String title, boolean show) {
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
        c.gridwidth = 3;
        c.insets = new Insets(0, 0, 8, 0);
        container.add(new JLabel("Please select a database:"), c);

        // Create database selector
        ComboBoxModel<AbstractDatabase> comboBoxModel = new DefaultComboBoxModel<>(Core.getInstance().getDatabaseManager().getDatabases().toArray(new AbstractDatabase[] {}));
        comboBox = new JComboBox<>(comboBoxModel);
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 20));

        // Add database selector
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        container.add(comboBox, c);

        // Create database configuration button
        this.configureButton = new JButton("...");
        //noinspection SuspiciousNameCombination
        this.configureButton.setPreferredSize(new Dimension(comboBox.getPreferredSize().height, comboBox.getPreferredSize().height));

        // Add database configuration button
        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(0, 8, 0, 0);
        container.add(configureButton, c);

        // Add and create user label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(16, 0, 0, 8);
        container.add(new JLabel("User:"), c);

        // Add and create user textbox
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(16, 0, 0, 0);
        container.add(new JTextField(), c);

        // Add and create password label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 0, 0, 8);
        container.add(new JLabel("Password:"), c);

        // Add and create password textbox
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weightx = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(8, 0, 0, 0);
        container.add(new JPasswordField(), c);

        // Create the commit buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 8, 8));

        // Create the continue and quit buttons
        this.continueButton = new JButton("Continue");
        this.quitButton = new JButton("Quit");

        // Put the continue and quit buttons on the frame (in the proper order)
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
        c.gridy = 4;
        c.gridwidth = 3;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(16, 50, 0, 0);
        container.add(buttonPanel, c);

        // Add the container to the dialog
        add(container);

        // Pack everything
        pack();

        // Request focus on the continue button
        // TODO: Move this somewhere else!
        continueButton.requestFocus();
    }

    /**
     * Link all action listeners to it's components.
     */
    private void linkComponents() {
        // Store the current instance
        final LoginDialog instance = this;

        // Create a runnable for the close action
        Runnable closeAction = () -> {
            // Set the success status flag
            instance.success = false;

            // Dispose the dialog
            instance.dispose();
        };

        // Add an action to the configure button
        this.configureButton.addActionListener(e -> {
            // Show the database manager form
            new DatabaseManagerDialog(instance, true);

            // Get the selected combo box value
            AbstractDatabase selected = getSelectedDatabase();

            // Reset the combo box data model
            comboBox.setModel(new DefaultComboBoxModel<>(Core.getInstance().getDatabaseManager().getDatabases().toArray(new AbstractDatabase[] {})));

            // Set the selected value to it's original
            comboBox.setSelectedItem(selected);
        });

        // Add an action to the continue button
        continueButton.addActionListener(e -> {
            // Validate the user input and set the success status flag
            instance.success = check();

            // Dispose the dialog if the user input is valid
            if(instance.success)
                instance.dispose();
        });

        // Add an action to the quit button
        quitButton.addActionListener(e -> {
            // Run the close action
            closeAction.run();
        });

        // Handle window close events
        instance.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) {
                // Run the close action
                closeAction.run();
            }

            @Override
            public void windowClosed(WindowEvent e) { }

            @Override
            public void windowIconified(WindowEvent e) { }

            @Override
            public void windowDeiconified(WindowEvent e) { }

            @Override
            public void windowActivated(WindowEvent e) { }

            @Override
            public void windowDeactivated(WindowEvent e) { }
        });
    }

    /**
     * Check whether the user successfully logged in
     *
     * @return True if succeeded, false if not.
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * Test the database connection and check the user credentials.
     *
     * @return True if valid, false if not.
     */
    public boolean check() {
        // Validate the configuration of the selected database
        if(!validateConfiguration())
            return false;

        // Create a progress dialog window
        ProgressDialog progress = new ProgressDialog(this, App.APP_NAME, true, "Connecting to database...", true);

        // TODO: Check database connection (already done in the database configurator?)

        // Validate user credentials, show status message
        progress.setStatus("Logging in...");

        // Validate user credentials
        // TODO: Validate the login credentials

        // Dispose the progress dialog since we're done
        progress.dispose();

        // TODO: Return the proper result
        return true;
    }

    /**
     * Validate the configuration of the selected database, and show it's edit dialog if it hasn't been fully configurated yet.
     *
     * @return True if validated, false if not.
     */
    public boolean validateConfiguration() {
        // Get the selected database credentials
        AbstractDatabase database = getSelectedDatabase();
        if(database == null) {
            JOptionPane.showMessageDialog(this, "No database selected.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check whether the configuration is already valid
        if(database.isConfigured())
            return true;

        // Show the database edit form for the current database
        AbstractDatabase updated = DatabaseModifyDialog.showModify(this, database);

        // Check whether the database has been configured properly now
        if(!updated.isConfigured()) {
            JOptionPane.showMessageDialog(this, "The selected database is missing some required properties.", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // The configuration seems to be valid, return true
        return true;
    }

    /**
     * Get the selected database.
     *
     * @return The selected database.
     */
    public AbstractDatabase getSelectedDatabase() {
        return (AbstractDatabase) this.comboBox.getSelectedItem();
    }
}
