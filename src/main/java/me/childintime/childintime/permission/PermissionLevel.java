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

    /**
     * Get the permission level by the given level number.
     *
     * @param level Level number.
     *
     * @return Permission level.
     */
    public static PermissionLevel getByLevel(int level) {
        // Loop through all the permission levels to find the proper one
        for(PermissionLevel permissionLevel : values())
            if(permissionLevel.getLevel() == level)
                return permissionLevel;

        // Return the base level
        return VIEW_ANONYMOUS;
    }
}
