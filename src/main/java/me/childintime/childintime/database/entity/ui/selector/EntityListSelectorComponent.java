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

package me.childintime.childintime.database.entity.ui.selector;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.ui.component.EntityViewComponent;
import me.childintime.childintime.util.swing.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class EntityListSelectorComponent extends EntityViewComponent {

    /**
     * Remember the item that was last selected.
     */
    private int lastSelectedIndex = 0;

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     */
    public EntityListSelectorComponent(AbstractEntityManager manager) {
        // Construct this without a default selected item
        this(manager, null);
    }

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     * @param selected Default selected entity.
     */
    public EntityListSelectorComponent(AbstractEntityManager manager, AbstractEntity selected) {
        // Construct the super
        super(manager);

        // Disable multi select
        setMultiSelect(false);

        // Set the selected item
        setSelectedItem(selected);

        // Create a selection listener, which forces one item to be selected
        addSelectionChangeListenerListener(() -> {
            // Make sure at least one item is available in the list
            if(getManager().getEntityCount() == 0)
                return;

            // Select the last item if none is selected
            if(getSwingTable().getSelectedRowCount() == 0)
                setSelectedItem(this.lastSelectedIndex);

            else
                // Remember the last selected index
                this.lastSelectedIndex = getSelectedIndex();
        });
    }

    /**
     * Get the selected item from the list.
     *
     * @return Selected item.
     */
    public AbstractEntity getSelectedItem() {
        return getSelectedEntity();
    }

    /**
     * Select the given item in the list.
     * No item will be selected if the item is null, or if the list is empty.
     *
     * @param item Item to select, or null.
     */
    public void setSelectedItem(AbstractEntity item) {
        // Clear the selection if the list is empty or if the item is null
        if(getSwingTable().getRowCount() == 0 || item == null) {
            getSwingTable().clearSelection();
            return;
        }

        // Get the list of entities
        final java.util.List<AbstractEntity> entities = getManager().getEntities();

        // Determine the index of the given item
        int itemIndex = -1;
        for(int i = 0; i < entities.size(); i++) {
            if(entities.get(i).equals(item)) {
                itemIndex = i;
                break;
            }
        }

        // Clear the selection if no valid item was found
        if(itemIndex == -1) {
            // Clear the selection
            getSwingTable().clearSelection();

            // We're done, return
            return;
        }

        // Select the item
        setSelectedItem(itemIndex);
    }

    /**
     * Select the item at the given index in the list.
     *
     * @param i Index of the item to select.
     */
    public void setSelectedItem(int i) {
        // Get the Swing table
        final JTable table = getSwingTable();

        // Do not select anything if the list is empty
        if(table.getRowCount() == 0)
            return;

        // Make sure the item is in-bound
        i = Math.max(Math.min(i, getManager().getEntityCount() - 1), 0);

        // Make sure the selected value is in bound
        if(i < 0 || i > table.getRowCount())
            return;

        // Select the item
        table.setRowSelectionInterval(i, i);

        // Set the last selected item
        this.lastSelectedIndex = i;

        // Scroll to the selected value
        scrollToRow(i);
    }

    /**
     * Get the window this component is placed in.
     *
     * @return Component window.
     */
    private Window getWindow() {
        return SwingUtils.getComponentWindow(this);
    }
}
