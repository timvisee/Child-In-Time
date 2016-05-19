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
		String osName = System.getProperty("os.name").toLowerCase();

		if (osName.contains("win"))
			return me.childintime.childintime.util.Platform.WINDOWS;

		if (osName.contains("mac"))
			return me.childintime.childintime.util.Platform.MAC_OS_X;

		if (osName.contains("solaris") || osName.contains("sunos"))
			return me.childintime.childintime.util.Platform.SOLARIS;

		if (osName.contains("linux") || osName.contains("unix"))
			return me.childintime.childintime.util.Platform.LINUX;

		return me.childintime.childintime.util.Platform.UNKNOWN;
	}
}
