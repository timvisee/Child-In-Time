/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
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
     * View button instance.
     */
    private JButton viewButton;

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
        this(manager, null);
    }

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     * @param showCoupleFor Show couple for.
     */
    public EntityManagerComponent(AbstractEntityManager manager, AbstractEntity showCoupleFor) {
        // Construct the super
        super();

        // Set the component name
        super.setName(getClass().getSimpleName());

        // Build the component UI
        buildUi(manager, showCoupleFor);
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
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Check whether the user can modify data
        final boolean canEdit = PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel());

        // Determine whether a couple is shown
        final boolean showingCouple = getEntityView().isCoupleView()|| getManager().getManifest().isCouple();

        // Create the buttons
        this.createButton = new JButton(!showingCouple ? "Create" : "Add");
        this.viewButton = new JButton("View");
        this.modifyButton = new JButton(!showingCouple ? "Modify" : "Edit");
        this.deleteButton = new JButton(!showingCouple ? "Delete" : "Remove");

        // Add the buttons to the panel
        buttonPanel.add(this.createButton);
        buttonPanel.add(this.viewButton);
        buttonPanel.add(this.modifyButton);
        buttonPanel.add(this.deleteButton);
        this.createButton.addActionListener(e -> {
            if(!this.entityView.isCoupleView())
                this.entityView.createEntity();
            else
                this.entityView.createEntityCouple();
        });
        this.viewButton.addActionListener(e -> this.entityView.viewSelectedEntity());
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
        this.filtersButton.setEnabled(false);
        this.columnsButton = new JButton("Columns...");
        this.columnsButton.setEnabled(false);

        // Add the buttons to the panel
        buttonPanel.add(this.refreshButton);
        buttonPanel.add(this.filtersButton);
        buttonPanel.add(this.columnsButton);
        this.refreshButton.addActionListener(e -> this.entityView.refresh());
        this.filtersButton.addActionListener(e -> featureNotImplemented());
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
        this.createButton.setEnabled(canEdit && isEnabled());

        // Enable the view button if one entity is selected
        this.viewButton.setEnabled(selected == 1 && isEnabled());

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

    /**
     * Get the window this component is placed in.
     *
     * @return Component window.
     */
    private Window getWindow() {
        return SwingUtils.getComponentWindow(this);
    }

    @Deprecated
    public void featureNotImplemented() {
        JOptionPane.showMessageDialog(this, "Feature not implemented yet!", App.APP_NAME, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Call the super
        super.setEnabled(enabled);

        // Enable the entity view
        this.entityView.setEnabled(enabled);

        // Update the UI buttons
        updateUiButtons();
    }
}
