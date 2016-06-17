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

package me.childintime.childintime.ui.component.property;

import me.childintime.childintime.ui.component.property.action.AbstractAction;
import me.childintime.childintime.ui.component.property.action.ClearAction;
import me.childintime.childintime.ui.component.property.context.ContextDynamicAction;
import me.childintime.childintime.util.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPropertyField extends JComponent {

    /**
     * Action list for this property field.
     */
    protected List<AbstractAction> actionsList = new ArrayList<>();

    /**
     * Defines whether the property field is set to null.
     */
    private boolean _null = false;

    /**
     * Flag which defines whether a null value is allowed.
     */
    private boolean allowNull;

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public AbstractPropertyField(boolean allowNull) {
        // Set whether null is allowed
        this.allowNull = allowNull;

        // Set the component name
        this.setName(getComponentName());

        // Add a focus listener to the property field
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                enableField();
            }

            @Override
            public void focusLost(FocusEvent e) { }
        });
    }

    /**
     * Build the action list.
     */
    public void buildActionList() {
        this.actionsList.add(new ClearAction(this));
    }

    /**
     * Build the component UI.
     */
    public void buildUi() {
        // Create a grid bag constraints instance
        GridBagConstraints c = new GridBagConstraints();

        // Set the component layout
        setLayout(new GridBagLayout());

        // Build the property field
        JComponent propertyField = buildUiField();

        // Build the action button panel
        JPanel actionButtonsPanel = buildUiActionButtonPanel();

        // Build the UI field context menu
        propertyField.setComponentPopupMenu(buildUiFieldContextMenu());

        // Add the field
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 1;
        add(propertyField, c);

        // Add the action button panel
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        add(actionButtonsPanel, c);
    }

    /**
     * Build the UI field.
     *
     * @return UI field.
     */
    protected abstract JComponent buildUiField();

    /**
     * Build the UI context menu for this component.
     * @return Context menu, or null.
     */
    protected JPopupMenu buildUiFieldContextMenu() {
        // Create the context menu
        JPopupMenu menu = new JPopupMenu();

        // Add the actions
        for(AbstractAction action : this.actionsList) {
            // Skip if this action shouldn't be added in the context menu
            if(!action.isShowContextMenu())
                continue;

            // Context action
            Action contextAction = action.getContextMenuAction();

            // Build the context action if it hasn't been configured yet
            if(contextAction == null) {
                // Create the dynamic action
                contextAction = new ContextDynamicAction(action);

                // Set the action
                action.setContextMenuAction(contextAction);
            }

            // Add the action to the context menu
            menu.add(contextAction);
        }

        // Return the menu
        return menu;
    }

    /**
     * Build the action button panel.
     *
     * @return Action button panel.
     */
    protected JPanel buildUiActionButtonPanel() {
        // Build the action list
        buildActionList();

        // Create a grid bag constraints instance
        GridBagConstraints c = new GridBagConstraints();

        // Create an actions button panel
        JPanel actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new GridBagLayout());

        // Add the action buttons
        for(int i = 0; i < this.actionsList.size(); i++) {
            // Get the action
            AbstractAction action = this.actionsList.get(i);

            // Skip some buttons
            if(!action.isShowButton())
                continue;

            // Create the action button
            JButton actionButton = new JButton(action.getText());
            actionButton.setToolTipText(action.getDescription());
            actionButton.addActionListener(e -> action.run());

            // Set the button instance for the action
            action.setButton(actionButton);

            // Set the button icon, if specified
            if(action.hasIcon()) {
                actionButton.setText("");
                actionButton.setIcon(action.getIcon());
            }

            // Define the size of the action button
            final int buttonSize = actionButton.getPreferredSize().height - 2;
            final Dimension buttonDimensions = new Dimension(buttonSize, buttonSize);
            actionButton.setBorder(null);
            actionButton.setFocusable(false);
            actionButton.setPreferredSize(buttonDimensions);
            actionButton.setMinimumSize(buttonDimensions);
            actionButton.setMaximumSize(buttonDimensions);
            actionButton.setSize(buttonDimensions);

            // Fix button styling on Mac OS X
            if(Platform.isMacOsX()) {
                //actionButton.putClientProperty("JButton.sizeVariant", "mini");
                actionButton.putClientProperty("JButton.buttonType", "square");
                actionButton.setMargin(new Insets(0, 0, 0, 0));
                actionButton.setFont(new Font(actionButton.getFont().getFontName(), Font.PLAIN, actionButton.getFont().getSize() - 2));
            }

            // Set up the constraints
            c.gridx = i;
            c.insets = new Insets(0, 4, 0, 0);

            // Add the action button
            actionButtonsPanel.add(actionButton, c);
        }

        // Return the action button panel
        return actionButtonsPanel;
    }

    /**
     * Get the property field value.
     *
     * @return Property field value.
     */
    public abstract Object getValue();

    /**
     * Set the property field value.
     *
     * @param value Property value.
     */
    public abstract void setValue(Object value);

    /**
     * Check whether a null value is allowed.
     *
     * @return True if allowed, false if not.
     */
    public boolean isNullAllowed() {
        return this.allowNull;
    }

    /**
     * Set whether a null value is allowed.
     *
     * @param allowNull True if null is allowed, false if not.
     */
    public void setNullAllowed(boolean allowNull) {
        this.allowNull = allowNull;
    }

    /**
     * Check whether the property field is set to null.
     *
     * @return True if the property field is set to null.
     */
    public boolean isNull() {
        return this._null;
    }

    /**
     * Set whether the property field is set to null.
     *
     * @param _null True if null, false if not.
     */
    public void setNull(boolean _null) {
        // Set the null state
        this._null = _null;

        // Update the enabled state of both components
        this.setFocusable(_null);

        // Fire the null change event for all attached actions
        for(AbstractAction action : this.actionsList)
            action.onNullChange(_null);
    }

    /**
     * Check whether the property field is empty.
     * If a field is null, it is marked as empty.
     * For property fields that contain strings, the field is empty if the string has a length of zero.
     *
     * @return True if empty, false if not.
     */
    public boolean isInputEmpty() {
        return isNull();
    }

    /**
     * Check whether the property field contents are valid.
     * This only does a basic check for some property fields.
     * For example: the path is validated for fields holding a file (the file doesn't need to exist).
     *
     * @return True if valid.
     */
    public boolean isInputValid() {
        // The field is invalid if it's null, while null isn't allowed
        return isNullAllowed() || !isNull();
    }

    /**
     * Clear the field.
     */
    public abstract void clear();

    /**
     * Get the component name.
     *
     * @return Component name.
     */
    protected abstract String getComponentName();

    /**
     * Enable the field for editing.
     * This will reset the null state, and select all the text inside the field.
     */
    public abstract void enableField();
}