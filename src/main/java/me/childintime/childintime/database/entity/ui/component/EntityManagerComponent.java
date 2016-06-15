package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.permission.PermissionLevel;
import me.childintime.childintime.util.swing.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class EntityManagerComponent extends JComponent {

    /**
     * Entity view component.
     */
    private EntityViewComponent entityView;

    /**
     * Create button instance.
     */
    private JButton createButton;

    /**
     * Modify button instance.
     */
    private JButton modifyButton;

    /**
     * Delete button instance.
     */
    private JButton deleteButton;

    /**
     * Refresh button instance.
     */
    private JButton refreshButton;

    /**
     * Filters button instance.
     */
    private JButton filtersButton;

    /**
     * Columns button instance.
     */
    private JButton columnsButton;

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     */
    public EntityManagerComponent(AbstractEntityManager manager) {
        // Construct the super
        super();

        // Set the component name
        super.setName(getClass().getSimpleName());

        // Build the component UI
        buildUi(manager);
    }

    /**
     * Get the entity manager.
     *
     * @return Entity manager.
     */
    public AbstractEntityManager getManager() {
        return this.entityView.getManager();
    }

    /**
     * Build the component UI.
     */
    private void buildUi(AbstractEntityManager manager) {
        // Construct a grid bag constraints object to specify the placement of all inner components
        GridBagConstraints c = new GridBagConstraints();

        // Set the frame layout
        this.setLayout(new GridBagLayout());

        // Create the entity view
        this.entityView = new EntityViewComponent(manager);

        // Create the management buttons panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 4, 4, 10);
        c.anchor = GridBagConstraints.NORTH;
        add(buildUiManageButtons(), c);

        // Create the view buttons panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 4, 4, 10);
        c.anchor = GridBagConstraints.SOUTH;
        add(buildUiViewButtons(), c);

        // Add the entity view
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 2;
        c.insets = new Insets(0, 0, 4, 4);
        add(this.entityView, c);

        // Update the UI buttons
        updateUiButtons();

        // Update the UI buttons when the selection changes
        this.entityView.addSelectionChangeListenerListener(this::updateUiButtons);
    }

    /**
     * Build the management buttons panel.
     *
     * @return Button panel.
     */
    private JPanel buildUiManageButtons() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Create the buttons
        this.createButton = new JButton("Create");
        this.modifyButton = new JButton("Modify");
        this.deleteButton = new JButton("Delete");

        // Add the buttons to the panel
        buttonPanel.add(this.createButton);
        buttonPanel.add(this.modifyButton);
        buttonPanel.add(this.deleteButton);
        this.createButton.addActionListener(e -> this.entityView.createEntity());
        this.modifyButton.addActionListener(e -> this.entityView.modifySelectedEntity());
        this.deleteButton.addActionListener(e -> this.entityView.deleteSelectedEntities());

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Build the view buttons panel.
     *
     * @return Button panel.
     */
    private JPanel buildUiViewButtons() {
        // Create a panel to put the buttons in and set it's layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Create the buttons to add to the panel
        this.refreshButton = new JButton("Refresh");
        this.filtersButton = new JButton("Filters...");
        this.columnsButton = new JButton("Columns...");

        // Add the buttons to the panel
        buttonPanel.add(this.refreshButton);
        buttonPanel.add(this.filtersButton);
        buttonPanel.add(this.columnsButton);
        // TODO: Use a helper function, to show a proper progress dialog
        this.refreshButton.addActionListener(e -> this.entityView.refresh());
        // TODO: Implement this feature!
        this.filtersButton.addActionListener(e -> featureNotImplemented());
        // TODO: Implement this feature!
        this.columnsButton.addActionListener(e -> featureNotImplemented());

        // Return the button panel
        return buttonPanel;
    }

    /**
     * Update the state of buttons in the button panel.
     */
    private void updateUiButtons() {
        // Get the number of selected entities
        int selected = this.entityView.getSelectedCount();

        // Determine whether the user has edit rights
        final boolean canEdit = PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel());

        // Enable the create button
        this.createButton.setEnabled(canEdit);

        // Enable the modify button if one entity is selected
        this.modifyButton.setEnabled(selected == 1 && canEdit);

        // Enable the delete button if at least one entity is selected
        this.deleteButton.setEnabled(selected > 0 && canEdit);
    }

    /**
     * Get the window this component is placed in.
     *
     * @return Component window.
     */
    private Window getWindow() {
        return SwingUtils.getComponentWindow(this);
    }

    // TODO: This should be removed!
    @Deprecated
    public void featureNotImplemented() {
        JOptionPane.showMessageDialog(this, "Feature not implemented yet!", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
    }
}
