package me.childintime.childintime.database.object.ui.component;

import me.childintime.childintime.database.object.AbstractEntity;
import me.childintime.childintime.database.object.AbstractEntityManager;
import me.childintime.childintime.database.object.EntityFieldsInterface;
import me.childintime.childintime.database.object.listener.EntityActionListener;
import me.childintime.childintime.database.object.listener.SelectionChangeListener;
import me.childintime.childintime.util.swing.TableUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EntityListComponent extends JComponent {

    /**
     * Entity manager this list is for.
     */
    private AbstractEntityManager manager;

    /**
     * The columns to show in the list view.
     */
    private EntityFieldsInterface[] columns;

    /**
     * Flag with defines whether multi selection mode is enabled.
     * True if enabled, false if not.
     * If this mode is enabled, multiple rows can be selected, otherwise just a single row can be selected.
     */
    private boolean multiSelect = true;

    /**
     * Table instance, to show the list of entitys.
     */
    private JTable uiTable;

    /**
     * Table model instance, used for the object table.
     */
    private AbstractTableModel uiTableModel;

    /**
     * List of entity action listeners.
     */
    private List<EntityActionListener> entityActionListeners = new ArrayList<>();

    /**
     * List of selection change listeners.
     */
    private List<SelectionChangeListener> selectionChangeListeners = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     */
    public EntityListComponent(AbstractEntityManager manager) {
        // Set the attributes
        this.manager = manager;

        // Set the columns
        this.columns = manager.getManifest().getDefaultFields();

        // Set the component name
        super.setName(getClass().getSimpleName());

        // Build the component UI
        buildUi();
    }

    /**
     * Get the entity manager.
     *
     * @return Entity manager.
     */
    public AbstractEntityManager getManager() {
        return this.manager;
    }

    /**
     * Get the columns that are shown.
     *
     * @return Shown columns.
     */
    public EntityFieldsInterface[] getColumns() {
        return this.columns;
    }

    /**
     * Set the columns that are shown.
     * This will automatically update the table to apply the changes.
     */
    public void setColumns(EntityFieldsInterface[] columns) {
        // Set the columns
        this.columns = columns;

        // Update the table structure
        updateViewStructure();
    }

    /**
     * Get the number of columns shown.
     *
     * @return Number of columns.
     */
    public int getColumnCount() {
        return this.columns.length;
    }

    /**
     * Get the Swing table instance.
     *
     * @return Swing table instance.
     */
    public JTable getObjectTable() {
        return this.uiTable;
    }

    /**
     * Ge the Swing table model instance.
     *
     * @return Swing table model instance.
     */
    public AbstractTableModel getSwingTableModel() {
        return this.uiTableModel;
    }

    /**
     * Update the data shown by this view.
     * This must be called to make data changes visible.
     */
    public void updateViewData() {
        this.uiTableModel.fireTableDataChanged();
    }

    /**
     * Update the structure of the view.
     * This must be called to update the view structure, when for example, the columns that are shown are changed.
     */
    public void updateViewStructure() {
        this.uiTableModel.fireTableStructureChanged();
    }

    /**
     * Fit the columns of the view as good as possible based on the cell contents.
     */
    public void fitColumns() {
        TableUtils.fitColumns(this.uiTable);
    }

    /**
     * Check whether multi selection mode is enabled.
     *
     * @return True if enabled.
     */
    public boolean isMultiSelect() {
        return this.multiSelect;
    }

    /**
     * Set whether multi selection mode is enabled.
     *
     * @param multiSelect True if enabled, false if not.
     */
    public void setMultiSelect(boolean multiSelect) {
        // Set the multi select flag
        this.multiSelect = multiSelect;

        // Update the table's selection mode
        this.uiTable.setSelectionMode(multiSelect ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Build the UI of this component.
     */
    private void buildUi() {
        // Set the layout
        setLayout(new BorderLayout());

        // Store the current instance
        final EntityListComponent instance = this;

        // Create the table model
        this.uiTableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return instance.getManager().getObjectCount();
            }

            @Override
            public int getColumnCount() {
                return instance.getColumnCount();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                // Get the object
                final AbstractEntity entity = instance.getManager().getObjects().get(rowIndex);

                // Return the value
                try {
                    return entity.getField(manager.getManifest().getDefaultFields()[columnIndex]);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public String getColumnName(int column) {
                return instance.getColumns()[column].getDisplayName();
            }
        };

        // Create the table
        this.uiTable = new JTable(this.uiTableModel);
        this.uiTable.setFillsViewportHeight(true);

        // Create a scroll pane container for the table, and add it to the base component
        final JScrollPane container = new JScrollPane(this.uiTable);
        add(container);

        // Set the default selection mode
        setMultiSelect(this.multiSelect);

        // Fit the columns of the table
        fitColumns();

        // Set up the listeners
        setUpListeners();
    }

    /**
     * Set up the listeners.
     */
    private void setUpListeners() {
        // Update the view when the manager data is changed
        this.manager.addChangeListener(this::updateViewData);

        // Create a double click listener to fire the entity action event
        this.uiTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Check whether the user double clicked
                if(evt.getClickCount() == 2) {
                    // Make sure at least one entity is selected
                    if(getSelectedCount() <= 0)
                        return;

                    // Fire the entity action listeners
                    fireEntityActionEvent(getSelectedEntities());
                }
            }
        });

        // Create a key listener to fire the entity action event when the enter key is pressed
        this.uiTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                // Check whether the enter key is pressed
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Fire the entities action event if any entity is selected
                    if(getSelectedCount() > 0)
                        fireEntityActionEvent(getSelectedEntities());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        });

        // Attach the selection change event to the selection change event loop of the table
        final ListSelectionModel selectionModel = this.uiTable.getSelectionModel();
        selectionModel.addListSelectionListener(e -> fireSelectionChangeListenerEvent());
    }

    /**
     * Get all selected database entities.
     *
     * @return List of selected database entities.
     */
    public List<AbstractEntity> getSelectedEntities() {
        // Create a list of entities
        List<AbstractEntity> entities = new ArrayList<>();

        // Add each selected entity to the list
        for(int i : this.uiTable.getSelectedRows())
            entities.add(this.manager.getObjects().get(i));

        // Return the list of entities
        return entities;
    }

    /**
     * Get the number of selected database entities.
     *
     * @return Number of selected database entities.
     */
    public int getSelectedCount() {
        return this.uiTable.getSelectedRowCount();
    }

    /**
     * Add an entity action listener.
     *
     * @param listener Listener.
     */
    public void addEntityActionListener(EntityActionListener listener) {
        this.entityActionListeners.add(listener);
    }

    /**
     * Get all the registered entity action listeners.
     *
     * @return List of entity action listeners.
     */
    public List<EntityActionListener> getEntityActionListeners() {
        return this.entityActionListeners;
    }

    /**
     * Remove the given entity action listener.
     *
     * @param listener Listener to remove.
     *
     * @return True if any listener was removed, false if not.
     */
    public boolean removeEntityActionListeners(EntityActionListener listener) {
        return this.entityActionListeners.remove(listener);
    }

    /**
     * Remove all entity action listeners.
     *
     * @return Number of entity action listeners that were removed.
     */
    public int removeAllEntityActionListeners() {
        // Remember the number of entity action listeners
        final int listenerCount = this.entityActionListeners.size();

        // Clear the list of listeners
        this.entityActionListeners.clear();

        // Return the number of cleared listeners
        return listenerCount;
    }

    /**
     * Fire the entity action listener event.
     *
     * @param entities List of entities.
     */
    public void fireEntityActionEvent(List<AbstractEntity> entities) {
        // Fire each registered listener
        for(EntityActionListener listener : this.entityActionListeners)
            listener.onEntityAction(entities);
    }

    /**
     * Add an selection change listener.
     *
     * @param listener Listener.
     */
    public void addSelectionChangeListenerListener(SelectionChangeListener listener) {
        this.selectionChangeListeners.add(listener);
    }

    /**
     * Get all the registered selection change listeners.
     *
     * @return List of selection change listeners.
     */
    public List<SelectionChangeListener> getSelectionChangeListenerListeners() {
        return this.selectionChangeListeners;
    }

    /**
     * Remove the given selection change listener.
     *
     * @param listener Listener to remove.
     *
     * @return True if any listener was removed, false if not.
     */
    public boolean removeSelectionChangeListenerListeners(SelectionChangeListener listener) {
        return this.selectionChangeListeners.remove(listener);
    }

    /**
     * Remove all selection change listeners.
     *
     * @return Number of selection change listeners that were removed.
     */
    public int removeAllSelectionChangeListeners() {
        // Remember the number of selection change listeners
        final int listenerCount = this.selectionChangeListeners.size();

        // Clear the list of listeners
        this.selectionChangeListeners.clear();

        // Return the number of cleared listeners
        return listenerCount;
    }

    /**
     * Fire the selection change listener event.
     */
    public void fireSelectionChangeListenerEvent() {
        // Fire each registered listener
        this.selectionChangeListeners.forEach(SelectionChangeListener::onSelectionChange);
    }
}
