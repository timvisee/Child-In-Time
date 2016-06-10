package me.childintime.childintime.ui.component.property.action;

import javax.swing.*;

public abstract class AbstractAction {

    /**
     * Action.
     */
    private Runnable action;

    /**
     * Action text.
     */
    private String text;

    /**
     * Action description..
     */
    private String description;

    /**
     * The icon for this action.
     */
    private ImageIcon icon;

    /**
     * True to show an action button.
     */
    private boolean showButton = true;

    /**
     * The button instance created for this action.
     */
    private JButton button = null;

    /**
     * True to show a context menu item for this action.
     */
    private boolean showContextMenu = true;

    /**
     * Context menu action.
     */
    private Action contextMenuAction = null;

    /**
     * Constructor.
     *
     * @param action Action.
     */
    public AbstractAction(Runnable action) {
        this.action = action;
    }

    /**
     * Constructor.
     *
     * @param action Action.
     * @param text Action text.
     * @param description Action description.
     */
    public AbstractAction(Runnable action, String text, String description) {
        this.action = action;
        this.text = text;
        this.description = description;
    }

    /**
     * Get the action.
     *
     * @return Action.
     */
    public Runnable getAction() {
        return this.action;
    }

    /**
     * Set the action.
     *
     * @param action Action.
     */
    public void setAction(Runnable action) {
        this.action = action;
    }

    /**
     * Get the action text.
     *
     * @return Action text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the action text.
     *
     * @param text Action text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the action description.
     *
     * @return Action description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Set the action description.
     *
     * @param description Action description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the icon for this action.
     *
     * @return Action icon.
     */
    public ImageIcon getIcon() {
        return this.icon;
    }

    /**
     * Set the icon for this action.
     *
     * @param icon Icon for this action.
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * Check whether an icon has been specified.
     *
     * @return True if an icon is specified, false if not.
     */
    public boolean hasIcon() {
        return this.icon != null;
    }

    /**
     * Run the action.
     */
    public void run() {
        // Make sure an action is configured
        if(this.action == null)
            return;

        // Run the action
        this.action.run();
    }

    /**
     * Check whether a button should be shown.
     *
     * @return True if a button should be shown, false if not.
     */
    public boolean isShowButton() {
        return this.showButton;
    }

    /**
     * Set whether a button should be shown.
     *
     * @param showButton True if a button should be shown, false if not.
     */
    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    /**
     * Get the button instance.
     *
     * @return Button instance.
     */
    public JButton getButton() {
        return this.button;
    }

    /**
     * Set the button instance.
     *
     * @param button Button instance.
     */
    public void setButton(JButton button) {
        this.button = button;
    }

    /**
     * Check whether this action should be shown in the context menu.
     *
     * @return True if it should be shown, false if not.
     */
    public boolean isShowContextMenu() {
        return this.showContextMenu;
    }

    /**
     * Set whether this action should be shown in the context menu.
     *
     * @param showContextMenu True if it should be shown, false if not.
     */
    public void setShowContextMenu(boolean showContextMenu) {
        this.showContextMenu = showContextMenu;
    }

    /**
     * Get the context menu instance for this action.
     *
     * @return Context menu instance.
     */
    public Action getContextMenuAction() {
        return this.contextMenuAction;
    }

    /**
     * Set the context menu instance for this action.
     *
     * @param contextMenuAction Context menu or null.
     */
    public void setContextMenuAction(Action contextMenuAction) {
        this.contextMenuAction = contextMenuAction;
    }

    /**
     * Called when the null state of the attached property field is changed.
     *
     * @param _null True if null, false if not.
     */
    public abstract void onNullChange(boolean _null);
}
