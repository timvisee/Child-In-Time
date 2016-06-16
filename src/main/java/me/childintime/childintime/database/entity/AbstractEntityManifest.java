package me.childintime.childintime.database.entity;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.entity.ui.dialog.EntityManagerDialog;
import me.childintime.childintime.database.entity.ui.dialog.EntityModifyDialog;
import me.childintime.childintime.permission.PermissionLevel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class AbstractEntityManifest {

    /**
     * Base constructor.
     */
    public AbstractEntityManifest() { }

    /**
     * Get the name of the current entity manager type.
     *
     * @param capital True to make the first character capital.
     * @param plural True to make the type name a plural.
     *
     * @return Entity manager type name.
     */
    public abstract String getTypeName(boolean capital, boolean plural);

    /**
     * Get the database table name for the manifest´s entity.
     *
     * @return Database table name.
     */
    public abstract String getTableName();

    /**
     * Get the default entity fields.
     *
     * @return Default entity fields to fetch.
     */
    public abstract EntityFieldsInterface[] getDefaultFields();

    /**
     * Get the fields class for this entity.
     *
     * @return Fields class.
     */
    public abstract Class<? extends EntityFieldsInterface> getFields();

    /**
     * Get the field values for this entity from the fields class.
     *
     * @return Field values.
     *
     * @throws NoSuchMethodException Throws if the fields class is invalid.
     * @throws InvocationTargetException Throws if an error occurred.
     * @throws IllegalAccessException Throws if an error occurred.
     */
    public EntityFieldsInterface[] getFieldValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Get the field values from the fields class instance
        return (EntityFieldsInterface[]) getFields().getMethod("valuesAllowed").invoke(getFields());
    }

    /**
     * Get the entity and instance class for this entity.
     *
     * @return Entity class
     */
    public abstract Class<? extends AbstractEntity> getEntity();

    /**
     * Get the manager class for this entity.
     *
     * @return Manager class.
     */
    public abstract Class<? extends AbstractEntityManager> getManager();

    /**
     * Get the manager instance.
     *
     * @return Manager instance.
     */
    public abstract AbstractEntityManager getManagerInstance();

    /**
     * Check whether this entity is a couple specification.
     *
     * @return True if this entity is a couple specification, false if it's a normal entity.
     */
    public abstract boolean isCouple();

    /**
     * Check whether this entity has any couple.
     *
     * @return True if this entity has any couple.
     */
    public boolean hasCouples() {
        return getCouples().size() > 0;
    }

    /**
     * Get the couples this entity has.
     * The returned list will be empty if this entity doesn't have any couple.
     *
     * @return List of couples.
     */
    public abstract List<AbstractEntityManifest> getCouples();

    /**
     * Show the manager dialog.
     *
     * @param owner Owner window, or null.
     */
    public void showManagerDialog(Window owner) {
        // Show the manager dialog
        new EntityManagerDialog(owner, getManagerInstance(), true);
    }

    /**
     * Show the entity creation dialog.
     *
     * @param owner Owner window, or null.
     *
     * @return Created entity, or null when cancelled.
     */
    public AbstractEntity showCreateDialog(Window owner) {
        // Make sure the user has edit rights
        if(!PermissionLevel.EDIT.orBetter(Core.getInstance().getAuthenticator().getPermissionLevel())) {
            // Show a warning
            JOptionPane.showMessageDialog(
                    owner,
                    "You don't have permission to create a new " + this.getTypeName(false, false) + ".",
                    "No permission",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        // Show the modification dialog to create a new entity
        final AbstractEntity entity = EntityModifyDialog.showCreate(owner, this);

        // TODO: Insert the entity in the manager, instead of refreshing everything.

        // Refresh the manager when a new entity is created
        if(entity != null)
            // Refresh the manager
            getManagerInstance().refresh();

        // Return the created entity
        return entity;
    }
}
