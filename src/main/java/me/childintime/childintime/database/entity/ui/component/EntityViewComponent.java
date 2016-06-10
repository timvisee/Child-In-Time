package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.App;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.ui.dialog.EntityModifyDialog;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class EntityViewComponent extends EntityListComponent {

    /**
     * Define whether a user can modify an entity.
     */
    private boolean canModify = true;

    /**
     * Define whether a user can delete an entry.
     */
    private boolean canDelete = true;

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     */
    public EntityViewComponent(AbstractEntityManager manager) {
        // Construct the super
        super(manager);

        // Attach the entity action listener, to execute entity modifications
        addEntityActionListener(entities -> {
            // The modify flag must be enabled
            if(!canModify)
                return;

            // Modify the entity
            modifyEntity(entities);
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
     * Create a new entity.
     */
    public void createEntity() {
        // Show the modification dialog to create a new entity
        final AbstractEntity entity = EntityModifyDialog.showCreate(getWindow(), getManager().getManifest());

        // TODO: Insert the entity in the manager, instead of refreshing everything.

        // Refresh the manager when a new entity is created
        if(entity != null)
            // Refresh the manager
            getManager().refresh();
    }

    /**
     * Modify the selected entities.
     */
    public void modifySelectedEntity() {
        // Get the objects manifest
        final AbstractEntityManifest manifest = getManager().getManifest();

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
        // Show the entity modification dialog
        EntityModifyDialog.showModify(getWindow(), entity);

        // TODO: Only edit the given entity, instead of refreshing everything?

        // Refresh the manager
        getManager().refresh();
    }

    /**
     * Delete the selected entities.
     */
    public void deleteSelectedEntities() {
        // Get the objects manifest
        final AbstractEntityManifest manifest = getManager().getManifest();

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

        // Generate the question dialog string
        final String question = "Are you sure you'd like to delete " + (getSelectedCount() == 1 ? "this" : "these") +
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

        // TODO: Only remove the above entities instead of refreshing everything?

        // Refresh the manager
        getManager().refresh();
    }

    /**
     * Get the window this component is placed in.
     *
     * @return Component window.
     */
    private Window getWindow() {
        return SwingUtilities.getWindowAncestor(this);
    }
}
