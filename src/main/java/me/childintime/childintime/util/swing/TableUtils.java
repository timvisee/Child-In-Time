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

package me.childintime.childintime.util.swing;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class TableUtils {

    /**
     * The default minimum width of a column.
     */
    public static final int COLUMN_WIDTH_MIN = 50;

    /**
     * Fit the columns of a JTable regarding it's contents.
     *
     * @param table JTable to fit the columns of.
     */
    public static void fitColumns(JTable table) {
        // Get the table's column model
        final TableColumnModel columnModel = table.getColumnModel();

        // Go through each column
        for(int column = 0; column < table.getColumnCount(); column++) {
            // Create a variable for the column width
            int width = COLUMN_WIDTH_MIN;

            // Loop through each row finding the maximum cell width
            for(int row = 0; row < table.getRowCount(); row++) {
                // Get the current cell
                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);

                // Calculate the preferred maximum width
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }

            // Set the determined column width as preferred
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    /**
     * Scroll to the given row in a table.
     *
     * @param table Table.
     * @param rowIndex Row to scroll to.
     * @param vColIndex Column.
     */
    public static void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
        // Make sure the table is valid
        if (!(table.getParent() instanceof JViewport))
            return;

        // Get the viewport
        JViewport viewport = (JViewport)table.getParent();

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);

        // Scroll to the rectangle
        table.scrollRectToVisible(rect);
        viewport.scrollRectToVisible(rect);
    }
}
