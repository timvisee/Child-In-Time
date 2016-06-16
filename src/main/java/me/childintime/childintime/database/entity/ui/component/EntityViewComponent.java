package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.App;
import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityCoupleManifest;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.ui.dialog.EntityModifyDialog;
import me.childintime.childintime.database.entity.ui.dialog.EntityViewDialog;
import me.childintime.childintime.database.entity.ui.selector.EntityListSelectorComponent;
import me.childintime.childintime.database.entity.ui.selector.EntityListSelectorDialog;
import me.childintime.childintime.permission.PermissionLevel;
import me.childintime.childintime.util.swing.ProgressDialog;
import me.childintime.childintime.util.swing.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EntityViewComponent extends EntityListComponent {

    /**
     * Define whether a user can create an entity.
     */
    private boolean canCreate = true;

    /**
     * Define whether a user can view an entity.
     */
    private boolean canView = true;

    /**
     * Define whether a user can modify an entity.
     */
    private boolean canModify = true;

    /**
     * Define whether a user can delete an entry.
     */
    private boolean canDelete = true;

    /**
     * Define whether to show the 'open manager' action on right click.
     */
    private boolean showManagerAction = true;

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     */
    public EntityViewComponent(AbstractEntityManager manager) {
        this(manager, null);
    }

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     * @param showCoupleFor The entity instance to show the couples for, or null.
     */
    public EntityViewComponent(AbstractEntityManager manager, AbstractEntity showCoupleFor) {
        // Construct the super
        super(manager, showCoupleFor);

        // Attach the entity action listener, to execute entity modifications
        addEntityActionListener(entities -> {
            // The modify flag must be enabled
            if(!canModify)
                return;

            // Do not execute the edit action if this component is a list selector
            // TODO: Move this somewhere else!
            if(this instanceof EntityListSelectorComponent)
                return;

            // Check whether the user had edit permissions
            if(PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel()))
                // Modify the entity
                modifyEntity(entities);

            else
                // View the entity
                viewEntity(entities);
        });

        // Create a key listener to catch delete key presses
        getSwingTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                // The delete flag must be enabled
                if(!canDelete)
                    return;

                // Check whether the delete key is pressed
                if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                    // Delete the selected entities
                    deleteSelectedEntities();

                    // Consume the key press
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        });

        // Add a proper right click menu
        // TODO: Move part of this to the base class
        getSwingTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // Make sure the table is enabled
                if(!getSwingTable().isEnabled())
                    return;

                // Make sure the right mouse button was released
                if(!SwingUtilities.isRightMouseButton(e))
                    return;

                // Make sure the source is the table, and that this is an popup trigger
                if(!e.isPopupTrigger() || !(e.getComponent() instanceof JTable))
                    return;

                // Get the selected row
                final int rawMouseRowIndex = getSwingTable().rowAtPoint(e.getPoint());
                if(rawMouseRowIndex > 0) {
                    // Convert the row to the model index
                    final int mouseRowIndex = getSwingTable().convertRowIndexToModel(rawMouseRowIndex);

                    // Check whether this row was selected
                    boolean isSelected = false;
                    for(int rowIndex : getSelectedIndices()) {
                        if(mouseRowIndex == rowIndex) {
                            isSelected = true;
                            break;
                        }
                    }

                    // Select this row if it wasn't selected
                    if(!isSelected)
                        getSwingTable().setRowSelectionInterval(rawMouseRowIndex, rawMouseRowIndex);
                }

                // Get the selected entities
                final List<AbstractEntity> selectedEntities = getSelectedEntities();

                // Create a popup menu
                JPopupMenu popup = new JPopupMenu();

                // Create the create menu
                JMenuItem createAction = new JMenuItem(!isCoupleView() ? "Create..." : "Add couple...");
                createAction.addActionListener(e1 -> {
                    if(!isCoupleView())
                        createEntity();
                    else
                        createEntityCouple();
                });
                createAction.setEnabled(canCreate);
                popup.add(createAction);

                // Create the view menu
                if(selectedEntities.size() >= 1) {
                    JMenuItem viewAction = new JMenuItem("View" + (isCoupleView() ? " couple" : "") + "...");
                    viewAction.addActionListener(e1 -> viewSelectedEntity());
                    viewAction.setEnabled(canView);
                    popup.add(viewAction);
                }

                // Create the modify menu
                if(selectedEntities.size() >= 1) {
                    JMenuItem modifyAction = new JMenuItem(!isCoupleView() ? "Modify..." : "Edit couple...");
                    modifyAction.addActionListener(e1 -> modifySelectedEntity());
                    modifyAction.setEnabled(canModify);
                    popup.add(modifyAction);
                }

                // Create the delete menu
                if(selectedEntities.size() >= 1) {
                    JMenuItem deleteAction = new JMenuItem(!isCoupleView() ? "Delete..." : "Remove couple...");
                    deleteAction.addActionListener(e1 -> deleteSelectedEntities());
                    deleteAction.setEnabled(canDelete);
                    popup.add(deleteAction);
                }

                // Show some couple view reference actions
                if(isCoupleView() && selectedEntities.size() >= 1) {
                    // Add a separator
                    popup.addSeparator();

                    // Create the view menu
                    JMenuItem viewAction = new JMenuItem("View reference...");
                    viewAction.addActionListener(e1 -> viewSelectedEntityCoupleReference());
                    viewAction.setEnabled(canView);
                    popup.add(viewAction);

                    // Create the modify menu
                    JMenuItem modifyAction = new JMenuItem("Modify reference...");
                    modifyAction.addActionListener(e1 -> modifySelectedEntityCoupleReference());
                    modifyAction.setEnabled(canModify);
                    popup.add(modifyAction);
                }

                // Create the manager action
                if(isShowManagerAction()) {
                    JMenuItem managerAction = new JMenuItem("Open manager...");
                    managerAction.addActionListener(e1 -> getManager().getManifest().showManagerDialog(getWindow()));
                    popup.addSeparator();
                    popup.add(managerAction);
                }

                // Create the refresh menu
                JMenuItem refreshAction = new JMenuItem("Refresh");
                refreshAction.addActionListener(e1 -> refresh());
                popup.addSeparator();
                popup.add(refreshAction);

                // Show the popup menu
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    /**
     * Check whether a user can create an entity.
     *
     * @return True if a user can create, false if not.
     */
    public boolean isCanCreate() {
        return this.canCreate;
    }

    /**
     * Set whether a user can create an entity.
     *
     * @param canCreate True if a user can create, false if not.
     */
    public void setCanCreate(boolean canCreate) {
        // Set the editable flag
        this.canCreate = canCreate;
    }

    /**
     * Check whether a user can view an entity.
     *
     * @return True if a user can view, false if not.
     */
    public boolean isCanView() {
        return this.canView;
    }

    /**
     * Set whether a user can view an entity.
     *
     * @param canView True if a user can view, false if not.
     */
    public void setCanView(boolean canView) {
        // Set the editable flag
        this.canView = canView;
    }

    /**
     * Check whether a user can modify an entity.
     *
     * @return True if a user can modify, false if not.
     */
    public boolean isCanModify() {
        return this.canModify;
    }

    /**
     * Set whether a user can modify an entity.
     *
     * @param canModify True if a user can modify, false if not.
     */
    public void setCanModify(boolean canModify) {
        // Set the editable flag
        this.canModify = canModify;
    }

    /**
     * Check whether a user can delete an entity.
     *
     * @return True if a user can delete, false if not.
     */
    public boolean isCanDelete() {
        return this.canDelete;
    }

    /**
     * Set whether a user can delete an entity.
     *
     * @param canDelete True if a user can delete, false if not.
     */
    public void setCanDelete(boolean canDelete) {
        // Set the editable flag
        this.canDelete = canDelete;
    }

    /**
     * Check whether to show the 'open in manager' action.
     *
     * @return True to show, false to hide.
     */
    public boolean isShowManagerAction() {
        return this.showManagerAction;
    }

    /**
     * Set whether to show the 'open in manager' action.
     *
     * @param showManagerAction True to show, false to hide.
     */
    public void setShowManagerAction(boolean showManagerAction) {
        this.showManagerAction = showManagerAction;
    }

    /**
     * Create a new entity.
     *
     * @return Created entity, or null when cancelled.
     */
    public AbstractEntity createEntity() {
        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to create a new " + getManager().getManifest().getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        // Show the modification dialog to create a new entity
        final AbstractEntity entity = EntityModifyDialog.showCreate(getWindow(), getManager().getManifest());

        // TODO: Insert the entity in the manager, instead of refreshing everything.

        // Refresh the manager when a new entity is created
        if(entity != null)
            // Refresh the manager
            getManager().refresh();

        // Return the created entity
        return entity;
    }

    /**
     * Create a new entity couple.
     *
     * @return Created entity couple, or null when cancelled.
     */
    public AbstractEntity createEntityCouple() {
        // Make sure we're working with a couple view
        if(!isCoupleView())
            return null;

        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to create a new " + getManager().getManifest().getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        // Get the couple manifest
        AbstractEntityCoupleManifest coupleManifest = (AbstractEntityCoupleManifest) getManager().getManifest();

        // Get the manifest of the other manifest
        AbstractEntityManifest otherManifest = coupleManifest.getOtherManifest(getShowCoupleFor());

        // Show a selection dialog for this manifest
        AbstractEntity selected = EntityListSelectorDialog.showDialog(getWindow(), otherManifest.getManagerInstance());

        // Make sure an entity is selected
        if(selected == null)
            return null;

        try {
            // Create a new entity instance
            AbstractEntity newEntity = getManager().getManifest().getEntity().newInstance();

            // Put the base reference and the selected reference in the object
            newEntity.parseField(
                    ((AbstractEntityCoupleManifest) getManager().getManifest()).getFieldByReferenceManifest(getShowCoupleFor().getManifest()),
                    getShowCoupleFor()
            );
            newEntity.parseField(
                    ((AbstractEntityCoupleManifest) getManager().getManifest()).getFieldByReferenceManifest(selected.getManifest()),
                    selected
            );

            // Apply the new entity to the database
            if(!newEntity.applyToDatabase()) {
                // Show a warning
                JOptionPane.showMessageDialog(
                        getWindow(),
                        "Failed to store couple.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return null;
            }

            // Return the couple
            return newEntity;

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Return null
            return null;
        }
    }

    /**
     * View the reference of an entity couple.
     */
    public void viewSelectedEntityCoupleReference() {
        // Make sure we're working with a couple view
        if(!isCoupleView())
            return;

        // Get the objects manifest
        final AbstractEntityManifest manifest = getManager().getManifest();

        // Make sure an entity is selected
        if(getSelectedCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a " + manifest.getTypeName(false, false) + " to view.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // View the selected entity
        viewEntityCoupleReference(getSelectedEntities());
    }

    /**
     * View the references of the given couples.
     * This method allows a list to be supplied, even though only one entity can be viewed.
     */
    public void viewEntityCoupleReference(List<AbstractEntity> couples) {
        // Only one entity can be modified
        if(getSelectedCount() > 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Only one " + getManager().getManifest().getTypeName(false, false) + " can be viewed at a time.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // View the entity
        viewEntityCoupleReference(couples.get(0));
    }

    /**
     * View the given entity couple reference..
     */
    public void viewEntityCoupleReference(AbstractEntity couple) {
        // Get the reference
        final AbstractEntity reference = convertCoupleToReference(couple);

        // Show the entity modification dialog
        EntityViewDialog.showDialog(getWindow(), reference);

        // Refresh both managers
        reference.getManifest().getManagerInstance().refresh();
        getManager().refresh();
    }

    /**
     * View the selected entities.
     */
    public void viewSelectedEntity() {
        // Get the objects manifest
        final AbstractEntityManifest manifest = getManager().getManifest();

        // Make sure an entity is selected
        if(getSelectedCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a " + manifest.getTypeName(false, false) + " to view.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // View the selected entity
        viewEntity(getSelectedEntities());
    }

    /**
     * View the given entity.
     * This method allows a list to be supplied, even though only one entity can be viewed.
     */
    public void viewEntity(List<AbstractEntity> entities) {
        // Only one entity can be modified
        if(getSelectedCount() > 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Only one " + getManager().getManifest().getTypeName(false, false) + " can be viewed at a time.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // View the entity
        viewEntity(entities.get(0));
    }

    /**
     * View the given entity.
     */
    public void viewEntity(AbstractEntity entity) {
        // Show the entity modification dialog
        EntityViewDialog.showDialog(getWindow(), entity);
    }

    /**
     * Modify the selected couple reference.
     */
    public void modifySelectedEntityCoupleReference() {
        // Get the objects manifest
        final AbstractEntityManifest manifest = getManager().getManifest();

        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to modify a " + manifest.getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Make sure an entity is selected
        if(getSelectedCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a " + manifest.getTypeName(false, false) + " to modify.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Modify the selected entity couple reference
        modifyEntityCoupleReference(getSelectedEntities());
    }

    /**
     * Modify the given entity couple reference.
     * This method allows a list to be supplied, even though only one entity can be modified.
     * This method is for ease of use, and thus can't be used to execute multiple modifications.
     */
    public void modifyEntityCoupleReference(List<AbstractEntity> couples) {
        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to modify a " + getManager().getManifest().getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Only one entity can be modified
        if(getSelectedCount() > 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Only one " + getManager().getManifest().getTypeName(false, false) + " can be modified at a time.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Modify the entity couple reference
        modifyEntityCoupleReference(couples.get(0));
    }

    /**
     * Modify the given entity couple reference.
     */
    public void modifyEntityCoupleReference(AbstractEntity couple) {
        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to modify a " + getManager().getManifest().getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Convert the couple to it's reference
        final AbstractEntity reference = convertCoupleToReference(couple);

        // Show the entity modification dialog
        EntityModifyDialog.showModify(getWindow(), reference);

        // TODO: Only edit the given entity, instead of refreshing everything?

        // Refresh both managers
        reference.getManifest().getManagerInstance().refresh();
        getManager().refresh();
    }

    /**
     * Modify the selected entities.
     */
    public void modifySelectedEntity() {
        // Get the objects manifest
        final AbstractEntityManifest manifest = getManager().getManifest();

        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to modify a " + manifest.getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Make sure an entity is selected
        if(getSelectedCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a " + manifest.getTypeName(false, false) + " to modify.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Modify the selected entity
        modifyEntity(getSelectedEntities());
    }

    /**
     * Modify the given entity.
     * This method allows a list to be supplied, even though only one entity can be modified.
     * This method is for ease of use, and thus can't be used to execute multiple modifications.
     */
    public void modifyEntity(List<AbstractEntity> entities) {
        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to modify a " + getManager().getManifest().getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Only one entity can be modified
        if(getSelectedCount() > 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Only one " + getManager().getManifest().getTypeName(false, false) + " can be modified at a time.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Modify the entity
        modifyEntity(entities.get(0));
    }

    /**
     * Modify the given entity.
     */
    public void modifyEntity(AbstractEntity entity) {
        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to modify a " + getManager().getManifest().getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Show the entity modification dialog
        EntityModifyDialog.showModify(getWindow(), entity);
    }

    /**
     * Delete the selected entities.
     */
    public void deleteSelectedEntities() {
        // Get the objects manifest
        final AbstractEntityManifest manifest = getManager().getManifest();

        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to delete a " + getManager().getManifest().getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Make sure an entity is selected
        if(getSelectedCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a " + manifest.getTypeName(false, false) + " to delete.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Delete the selected entities
        deleteEntities(getSelectedEntities());
    }

    /**
     * Delete the given entities.
     * A confirmation will be shown to confirm the deletion.
     */
    public void deleteEntities(List<AbstractEntity> entities) {
        // Return if no entity was given
        if(entities.size() == 0)
            return;

        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    getWindow(),
                    "You don't have permission to delete a " + getManager().getManifest().getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Generate the question dialog string
        final String question = "<html>" +
                "You're about to delete the selected " +
                " " + getManager().getManifest().getTypeName(false, getSelectedCount() != 1) + ".<br>" +
                "Other entities that are linked to " + (getSelectedCount() == 1 ? "this" : "these") +
                " " + getManager().getManifest().getTypeName(false, getSelectedCount() != 1) + " will be deleted along with it.<br>" +
                "This action can not be reverted.<br>" +
                "<br>" +
                "Are you sure you'd like to delete " + (getSelectedCount() == 1 ? "this" : "these") +
                " " + getManager().getManifest().getTypeName(false, getSelectedCount() != 1) + "?";

        // Confirm the deletion, return if the use cancelled
        switch(JOptionPane.showConfirmDialog(
                this,
                question,
                "Delete " + getManager().getManifest().getTypeName(false, true),
                JOptionPane.YES_NO_OPTION)
                ) {
            case JOptionPane.NO_OPTION:
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                return;
        }

        // Create a progress dialog, to show the delete progression
        ProgressDialog progressDialog = new ProgressDialog(
                getWindow(),
                "Deleting...",
                false,
                "Deleting " + getManager().getManifest().getTypeName(false, true) + "...",
                true
        );
        progressDialog.setShowProgress(true);
        progressDialog.setProgressMax(getSelectedCount());

        // Delete the given entities
        for(AbstractEntity entity : entities) {
            // Delete the entity from the database
            if(!entity.deleteFromDatabase()) {
                // Show an error message
                JOptionPane.showMessageDialog(
                        this,
                        "An error occurred while deleting a " + getManager().getManifest().getTypeName(false, false) +
                                " from the database. The operation has been aborted.",
                        "Failed to delete",
                        JOptionPane.ERROR_MESSAGE
                );

                // Dispose the progress dialog
                progressDialog.dispose();

                // Return
                return;
            }

            // Increase the progress
            progressDialog.increaseProgressValue();
        }

        // Dispose the progress dialog
        progressDialog.dispose();

        // Refresh the managers
        for(AbstractEntityManifest abstractEntityManifest : entities.get(0).getManifest().getReferencedManifests())
            abstractEntityManifest.getManagerInstance().refresh();
        entities.get(0).getManifest().getManagerInstance().refresh();
    }

    /**
     * Get the window this component is placed in.
     *
     * @return Component window.
     */
    private Window getWindow() {
        return SwingUtils.getComponentWindow(this);
    }

    /**
     * Convert the given couple to it's reference.
     *
     * @param couple Couple to convert.
     *
     * @return Reference.
     */
    private AbstractEntity convertCoupleToReference(AbstractEntity couple) {
        // Create a list with couples
        List<AbstractEntity> couples = new ArrayList<>();
        couples.add(couple);

        // Convert and return
        //noinspection ConstantConditions
        return convertCouplesToReferences(couples).get(0);
    }

    /**
     * Convert the given couples to their references.
     *
     * @param couples Couples to convert.
     *
     * @return References.
     */
    private List<AbstractEntity> convertCouplesToReferences(List<AbstractEntity> couples) {
        // Get the couple manifest
        final AbstractEntityCoupleManifest coupleManifest = (AbstractEntityCoupleManifest) getManager().getManifest();

        // Create a list of referenced entities
        List<AbstractEntity> references = new ArrayList<>();

        // Get the references of the selected entities
        for(AbstractEntity selectedEntity : couples) {
            try {
                references.add((AbstractEntity) selectedEntity.getField(
                        coupleManifest.getFieldByReferenceManifest(
                                coupleManifest.getOtherManifest(getShowCoupleFor()))
                ));

            } catch(Exception e) {
                // Print the stack trace
                e.printStackTrace();

                // Return return
                return null;
            }
        }

        // Return the list of references
        return references;
    }
}
