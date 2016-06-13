package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
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

        // Set the default modify and delete flags
        setCanModify(false);
        setCanDelete(false);

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
     * No row will be selected if the list is empty.
     *
     * @param item Item to select, or null to select the first row.
     */
    public void setSelectedItem(AbstractEntity item) {
        // Do not select anything if the list is empty
        if(getSwingTable().getRowCount() == 0)
            return;

        // Select the first row if the item is null
        if(item == null) {
            setSelectedItem(0);
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

        // Make sure a valid index is found
        if(itemIndex == -1) {
            // Select the first item if none has been selected yet
            if(getSelectedCount() == 0)
                setSelectedItem(0);

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

        // Select the item
        table.setRowSelectionInterval(i, i);

        // Set the last selected item
        this.lastSelectedIndex = i;
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
