package me.childintime.childintime.permission;

public enum PermissionLevel {

    /**
     * Permission to do anything.
     */
    ALL(0),

    /**
     * Edit permissions.
     */
    EDIT(1),

    /**
     * View permissions.
     */
    VIEW(2),

    /**
     * View permissions for anonymous data.
     */
    VIEW_ANONYMOUS(3);

    /**
     * Level number.
     */
    private int level;

    /**
     * Constructor.
     *
     * @param level Level number.
     */
    PermissionLevel(int level) {
        this.level = level;
    }

    /**
     * Get the level number.
     *
     * @return Level number.
     */
    public int getLevel() {
        return this.level;
    }
}
