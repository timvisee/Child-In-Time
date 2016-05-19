package me.childintime.childintime.util;

public enum Platform {
	/**
	 * Windows operating system.
	 */
	WINDOWS,

	/**
	 * Mac OS X operationg system.
	 */
	MAC_OS_X,

	/**
	 * Solaris operating system.
	 */
	SOLARIS,

	/**
	 * Linux operating system.
	 */
	LINUX,

	/**
	 * Unknown operating system.
	 */
	UNKNOWN;

	/**
	 * Get the current platform.
	 *
	 * @return Platform.
     */
	public static Platform getPlatform() {
		// Get the operating system name
		String os = System.getProperty("os.name").toLowerCase();

		// Check whether we're running on Windows
		if(os.contains("win"))
			return WINDOWS;

		// Check whether we're running on Linux
		if (os.contains("linux") || os.contains("unix"))
			return LINUX;

		// Check whether we're running on Mac OS X
		if (os.contains("mac"))
			return MAC_OS_X;

		// Check whether we're running on Solaris
		if (os.contains("solaris") || os.contains("sunos"))
			return SOLARIS;

		// We're running on an unknown operating system
		return UNKNOWN;
	}

	/**
	 * Check whether the application is running on a Windows machine.
	 *
	 * @return True if the application is running on a Windows machine, false if not.
	 */
	public static boolean isWindows() {
		return getPlatform() == WINDOWS;
	}

	/**
	 * Check whether the application is running on a Linux machine.
	 *
	 * @return True if the application is running on a Linux machine, false if not.
	 */
	public static boolean isLinux() {
		return getPlatform() == LINUX;
	}

	/**
	 * Check whether the application is running on a Mac OS X machine.
	 *
	 * @return True if the application is running on a Mac OS X machine, false if not.
	 */
	public static boolean isMacOsX() {
		return getPlatform() == MAC_OS_X;
	}

	/**
	 * Check whether the application is running on a Solaris machine.
	 *
	 * @return True if the application is running on a Solaris machine, false if not.
	 */
	public static boolean isSolaris() {
		return getPlatform() == SOLARIS;
	}
}
