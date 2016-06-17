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

import me.childintime.childintime.database.entity.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

public class EntityListSorter extends TableRowSorter<AbstractTableModel> {

    /**
     * Filter field.
     */
    private EntityFieldsInterface filterField = null;

    /**
     * List component instance.
     */
    private EntityListComponent listComponent;

    /**
     * Filter value.
     */
    private Object filterValue = null;

    /**
     * Constructor.
     *
     * @param listComponent The entity list component
     */
    public EntityListSorter(EntityListComponent listComponent) {
        // Call the super
        super(listComponent.getSwingTableModel());

        // Set the list component
        this.listComponent = listComponent;

        // Set the filter
        setRowFilter(
                new RowFilter<Object, Integer>() {
                    public boolean include(Entry entry) {
                        // Get the row identifier
                        int i = (int) entry.getIdentifier();

                        // Get the corresponding entity
                        AbstractEntity entity = getManager().getEntities().get(i);

                        // Check whether couple references should be filtered
                        if(listComponent.isCoupleView()) {
                            // Get the couple manifest
                            AbstractEntityCoupleManifest coupleManifest = ((AbstractEntityCoupleManifest) getManager().getManifest());

                            // Get the other reference field
                            EntityCoupleFieldsInterface referenceField = coupleManifest.getFieldByReferenceManifest(listComponent.getShowCoupleFor().getManifest());

                            // Make sure the entity reference field equals the couple object
                            try {
                                if(!entity.getField(referenceField).equals(listComponent.getShowCoupleFor()))
                                    return false;

                            } catch(Exception e) {
                                // Print the stack trace
                                e.printStackTrace();
                            }
                        }

                        // Include if the filter is disabled
                        if(filterField == null)
                            return true;

                        // Get the filter field, and compare it to the value
                        try {
                            return entity.getField(filterField).equals(filterValue);

                        } catch(Exception e) {
                            // Print the stack trace
                            e.printStackTrace();
                        }

                        // Return true for now
                        return true;
                    }
                }
        );
    }

    @Override
    public void toggleSortOrder(int column) {
        // Make sure the list component is enabled
        if(!listComponent.isEnabled())
            return;

        // Get the sorting keys
        List<? extends SortKey> sortKeys = getSortKeys();

        // If the sorting mode was descending, turn it off (UNSORTED -> ASCENDING -> DESCENDING -> UNSORTED)
        if(sortKeys.size() > 0) {
            // Check whether the mode was descending
            if(sortKeys.get(0).getSortOrder() == SortOrder.DESCENDING) {
                // Turn the sorting off
                setSortKeys(null);
                return;
            }
        }

        // Call the super
        super.toggleSortOrder(column);
    }

    /**
     * Set the filter.
     *
     * @param field Filter field.
     * @param value Filter value.
     */
    public void setFilter(EntityFieldsInterface field, Object value) {
        this.filterField = field;
        this.filterValue = value;
    }

    /**
     * Clear the filter.
     */
    public void clearFilter() {
        this.filterField = null;
        this.filterValue = null;
    }

    /**
     * Get the manager for the list.
     *
     * @return List manager.
     */
    public AbstractEntityManager getManager() {
        return this.listComponent.getManager();
    }
}
