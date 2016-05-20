package me.childintime.childintime.database;

import me.childintime.childintime.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class DatabaseEditForm extends JDialog {

    /**
     * Frame title.
     */
    private static final String FORM_TITLE = App.APP_NAME + " - Edit database";

    /**
     * The source database instance, which is the source.
     */
    private AbstractDatabase source;

    /**
     * The new database we're creating.
     */
    private AbstractDatabase database;

    /**
     * The database name field.
     */
    private JTextField databaseNameField;

    /**
     * The database type box.
     */
    private JComboBox<DatabaseType> databaseTypeBox;

    /**
     * Constructor.
     *
     * @param owner The parent window.
     * @param source The source.
     * @param show True to show the frame once it has been initialized.
     */
    public DatabaseEditForm(Window owner, AbstractDatabase source, boolean show) {
        // Construct the form
        super(owner, FORM_TITLE, ModalityType.DOCUMENT_MODAL);

        // Store the database instance
        // TODO: Clone here!
        this.source = source;
        this.database = source;

        // Create the form UI
        createUIComponents();

        // Set the database
        updateComponents(this.database);

        // Make the frame resizable
        this.setResizable(true);

        // Do not close the window when pressing the red cross, execute the close method instead
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) {
                closeFrame();
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

        // Configure the window size
        configureSize();

        // Set the window location to the system's default
        this.setLocationByPlatform(true);
        this.setLocationRelativeTo(owner);

        // Show the form
        this.setVisible(show);
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
     * Create all UI components for the frame.
     */
    private void createUIComponents() {
        // Set the frame layout
        this.setLayout(new BorderLayout());

        // Create the main panel, to put the question and answers in
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Create the properties panel and set the layout
        JPanel pnlProperties = new JPanel();
        pnlProperties.setLayout(new GridBagLayout());
        pnlProperties.setBorder(BorderFactory.createTitledBorder("Properties"));

        // Construct a grid bag constraints object to specify the placement of all components
        GridBagConstraints c = new GridBagConstraints();

        // Configure the placement of the questions label, and add it to the questions panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 16, 8);
        container.add(new JLabel("Edit database."), c);

        // Create and add the name label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        container.add(new JLabel("Name:"), c);

        // Create and add the type label
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 16, 8);
        c.anchor = GridBagConstraints.WEST;
        container.add(new JLabel("Type:"), c);

        // Create the database name field
        this.databaseNameField = new JTextField("Database");

        // Add the database name field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 8, 8);
        c.weightx = 1;
        container.add(this.databaseNameField, c);

        // Create the database type field
        this.databaseTypeBox = new JComboBox<>(DatabaseType.values());

        // Add the database name field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 16, 8);
        c.weightx = 1;
        container.add(this.databaseTypeBox, c);

        // Configure the answers panel placement and add it to the main panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 16, 0);
        container.add(pnlProperties, c);

        // Create the control button panel and add it to the main panel
        JPanel controlsPanel = createControlButtonPanel();
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(8, 0, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        container.add(controlsPanel, c);

        // Add the container to the frame
        this.add(container);
    }

    /**
     * Create the button panel to control the form.
     *
     * @return Button panel.
     */
    public JPanel createControlButtonPanel() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 8, 8));

        // Create the buttons to add to the panel
        JButton okButton = new JButton("Ok");
        JButton applyButton = new JButton("Toepassen");
        JButton cancelButton = new JButton("Annuleren");
        okButton.addActionListener(e -> {
            // Save the questions
            if(!applyDatabase())
                return;

            // Close the frame
            dispose();
        });
        applyButton.addActionListener(e -> applyDatabase());
        cancelButton.addActionListener(e -> closeFrame());

        // Add the buttons to the panel
        buttonPanel.add(okButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Get the current database.
     *
     * @return Database.
     */
    public AbstractDatabase getDatabase() {
        return this.database;
    }

    /**
     * Set the database that is shown.
     *
     * @param database Database.
     */
    public void updateComponents(AbstractDatabase database) {
        // Set the question label
        this.databaseNameField.setText(database.getName());

        // TODO: Set the database type!

//        // Set the labels
//        for(int i = 0; i < ANSWER_COUNT; i++) {
//            // Set whether the answer is correct
//            answerRadioButtons[i].setSelected(database.isCorrectAnswerIndex(i));
//
//            // Put the answers in the answer fields
//            answerFields[i].setText(StringUtils.decodeHtml(database.getAnswer(i, false)));
//        }

        // Pack the frame
        //pack();

        // Force the whole frame to repaint, to prevent graphical artifacts on some operating systems
        this.repaint();
    }

    /**
     * Apply and save the database.
     *
     * @return True if save succeed, false otherwise.
     */
    public boolean applyDatabase() {
        // Make sure the question is valid
        if(this.databaseNameField.getText().trim().length() <= 0) {
            JOptionPane.showMessageDialog(this, "Invalid database name.", "Invalid name", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // TODO: Set the database type, (probably already done)
        // TODO: Set the database-type-specific properties

        // Save succeed, return the result
        return true;
    }

    /**
     * Close the frame. Ask whether the user wants to save the changes.
     */
    public void closeFrame() {
        // Only ask to save if there are any unsaved changes
        if(hasUnsavedChanges()) {
            // Ask whether the user wants to save the questions
            switch(JOptionPane.showConfirmDialog(this, "Would you like to save the changes?", "Database changed", JOptionPane.YES_NO_CANCEL_OPTION)) {
                case JOptionPane.YES_OPTION:
                    // Save the changes
                    if(!applyDatabase())
                        break;

                case JOptionPane.NO_OPTION:
                    // Dispose the frame
                    this.dispose();
                    break;
            }

        } else
            this.dispose();
    }

    /**
     * Check whether this database has unsaved changes.
     *
     * @return True if this database has unsaved changes, false if not.
     */
    public boolean hasUnsavedChanges() {
        // TODO: Check whether there are any unsaved changes (equal the old and new one)

        // No unsaved changes detected, return false
        return false;
    }
}
