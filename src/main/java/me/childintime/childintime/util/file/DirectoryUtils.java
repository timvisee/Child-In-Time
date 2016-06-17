package me.childintime.childintime.util.file;

import me.childintime.childintime.util.Platform;

import java.io.File;

public class DirectoryUtils {

    /**
     * Get the application data directory.
     *
     * @return Application data directory.
     */
	public static File getAppDataDirectory() {
        // Get the systems home directory
        String homeDir = System.getProperty("user.home", ".");
        File workingDir;

        // Determine the directory
        switch (Platform.getPlatform()) {
        case WINDOWS:
            String applicationData = System.getenv("APPDATA");
            if (applicationData != null)
                workingDir = new File(applicationData);
            else
                workingDir = new File(homeDir);
            break;
            
        case LINUX:
        case SOLARIS:
            workingDir = new File(homeDir);
            break;
            
        case MAC_OS_X:
            workingDir = new File(homeDir);
            break;
            
        default:
            workingDir = new File(homeDir);
        }

        // Return the working directory
        return workingDir;
    }
}
