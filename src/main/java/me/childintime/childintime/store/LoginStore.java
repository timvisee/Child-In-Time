package me.childintime.childintime.store;

public class LoginStore {

    /**
     * Store section.
     */
    public static final String STORE_SECTION = "login";

    /**
     * Store key for the username.
     */
    public static final String STORE_KEY_USER = "user";

    /**
     * Store key for the password.
     */
    public static final String STORE_KEY_PASSWORD = "password";

    /**
     * Get the stored username.
     *
     * @param def Default.
     *
     * @return Stored username.
     */
    public String getUsername(String def) {
        return GlobalStore.getValue(STORE_SECTION, STORE_KEY_USER, def);
    }

    /**
     * Check whether a username is stored.
     *
     * @return Stored username.
     */
    public boolean hasUsername() {
        return GlobalStore.hasValue(STORE_SECTION, STORE_KEY_USER);
    }

    /**
     * Set the stored username.
     *
     * @param username Stored username.
     */
    public void setUsername(String username) {
        GlobalStore.setValue(STORE_SECTION, STORE_KEY_USER, username);
    }

    /**
     * Get the stored password.
     *
     * @param def Default.
     *
     * @return Stored password.
     */
    public String getPassword(String def) {
        return GlobalStore.getValue(STORE_SECTION, STORE_KEY_PASSWORD, def);
    }

    /**
     * Check whether a password is stored.
     *
     * @return Stored password.
     */
    public boolean hasPassword() {
        return GlobalStore.hasValue(STORE_SECTION, STORE_KEY_PASSWORD);
    }

    /**
     * Set the stored password.
     *
     * @param password Stored password.
     */
    public void setPassword(String password) {
        GlobalStore.setValue(STORE_SECTION, STORE_KEY_PASSWORD, password);
    }
}
