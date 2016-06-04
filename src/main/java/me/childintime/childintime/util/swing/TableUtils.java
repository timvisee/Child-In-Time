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
}
