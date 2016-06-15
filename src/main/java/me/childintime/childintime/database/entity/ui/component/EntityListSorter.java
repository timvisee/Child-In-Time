package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.EntityFieldsInterface;

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
     * Filter value.
     */
    private Object filterValue = null;

    /**
     * Constructor.
     *
     * @param model Constructor.
     * @param manager Entity manager.
     */
    public EntityListSorter(AbstractTableModel model, AbstractEntityManager manager) {
        super(model);

        setRowFilter(
                new RowFilter<Object, Integer>() {
                    public boolean include(Entry entry) {
                        // Include if the filter is disabled
                        if(filterField == null || filterValue == null)
                            return true;

                        // Get the row identifier
                        int i = (int) entry.getIdentifier();

                        // Get the corresponding entity
                        // TODO: Are these indexes still correct when the table is sorted?
                        AbstractEntity entity = manager.getEntities().get(i);

                        // Get the filter field, and compare it to the value
                        try {
                            return entity.getField(filterField).equals(filterValue);

                        } catch(Exception e) {
                            // Show the stack trace
                            e.printStackTrace();

                            // Return true for now
                            return true;
                        }
                    }
                }
        );
    }

    @Override
    public void toggleSortOrder(int column) {
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
}
