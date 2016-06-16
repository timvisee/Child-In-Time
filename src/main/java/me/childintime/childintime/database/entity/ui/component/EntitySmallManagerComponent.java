package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.permission.PermissionLevel;

import javax.swing.*;
import java.awt.*;

public class EntitySmallManagerComponent extends JComponent {

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
     * Constructor.
     *
     * @param manager Entity manager.
     */
    public EntitySmallManagerComponent(AbstractEntityManager manager) {
        this(manager, null);
    }

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     * @param showCoupleFor Show couple for.
     */
    public EntitySmallManagerComponent(AbstractEntityManager manager, AbstractEntity showCoupleFor) {
        // Construct the super
        super();

        // Set the component name
        super.setName(getClass().getSimpleName());

        // Build the component UI
        buildUi(manager, showCoupleFor);

        // Set the preferred size
        setPreferredSize(new Dimension(200, 120));
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
     *
     * @param manager Manager.
     * @param showCoupleFor Show couple for.
     */
    private void buildUi(AbstractEntityManager manager, AbstractEntity showCoupleFor) {
        // Construct a grid bag constraints object to specify the placement of all inner components
        GridBagConstraints c = new GridBagConstraints();

        // Set the frame layout
        this.setLayout(new GridBagLayout());

        // Create the entity view
        this.entityView = new EntityViewComponent(manager, showCoupleFor);

        // Create the management buttons panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 4, 4, 10);
        c.anchor = GridBagConstraints.NORTH;
        add(buildUiManageButtons(), c);

        // Add the entity view
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
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
        this.createButton = new JButton(!this.entityView.isCoupleView() ? "Create" : "Add");
        this.modifyButton = new JButton(!this.entityView.isCoupleView() ? "Modify" : "Edit");
        this.deleteButton = new JButton(!this.entityView.isCoupleView() ? "Delete" : "Remove");

        // Add the buttons to the panel
        buttonPanel.add(this.createButton);
        buttonPanel.add(this.modifyButton);
        buttonPanel.add(this.deleteButton);
        this.createButton.addActionListener(e -> {
            if(!this.entityView.isCoupleView())
                this.entityView.createEntity();
            else
                this.entityView.createEntityCouple();
        });
        this.modifyButton.addActionListener(e -> this.entityView.modifySelectedEntity());
        this.deleteButton.addActionListener(e -> this.entityView.deleteSelectedEntities());

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
        this.createButton.setEnabled(canEdit && isEnabled());

        // Enable the modify button if one entity is selected
        this.modifyButton.setEnabled(selected == 1 && canEdit && isEnabled());

        // Enable the delete button if at least one entity is selected
        this.deleteButton.setEnabled(selected > 0 && canEdit && isEnabled());
    }

    /**
     * Get the entity view component.
     *
     * @return Entity view component.
     */
    public EntityViewComponent getEntityView() {
        return this.entityView;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Call the super
        super.setEnabled(enabled);

        // Disable the entity view
        this.entityView.setEnabled(enabled);

        // Update the UI buttons
        updateUiButtons();
    }
}
