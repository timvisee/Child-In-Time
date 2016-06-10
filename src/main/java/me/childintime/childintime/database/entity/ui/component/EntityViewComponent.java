package me.childintime.childintime.database.entity.ui.component;

import me.childintime.childintime.App;
import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManager;
import me.childintime.childintime.database.entity.AbstractEntityManifest;
import me.childintime.childintime.database.entity.ui.dialog.EntityModifyDialog;
import me.childintime.childintime.util.swing.ProgressDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EntityViewComponent extends EntityListComponent {

    /**
     * Constructor.
     *
     * @param manager Entity manager.
     */
    public EntityViewComponent(AbstractEntityManager manager) {
        // Construct the super
        super(manager);

        addEntityActionListener(entities -> modifyEntity(entities.get(0)));
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

        // Only one entity can be selected
        if(getSelectedCount() > 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Only one " + manifest.getTypeName(false, false) + " can be edited at a time.",
                    App.APP_NAME,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Edit the selected entity
        modifyEntity(getSelectedEntity());
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
