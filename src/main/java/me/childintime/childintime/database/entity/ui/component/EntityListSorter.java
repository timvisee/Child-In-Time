package me.childintime.childintime.database.entity.ui.component;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

public class EntityListSorter extends TableRowSorter<AbstractTableModel> {

    /**
     * Constructor.
     *
     * @param model Constructor.
     */
    public EntityListSorter(AbstractTableModel model) {
        super(model);
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
}
