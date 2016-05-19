package me.childintime.childintime.util.file;

import java.io.File;

import me.childintime.childintime.util.Platform;

public class DirectoryUtils {
	
	// TODO: Windows, use global data storage location
	// TODO: Return proper locations for Linux, Mac OS X and other file systems
	
	public static File getAppDataDirectory() {
        String homeDir = System.getProperty("user.home", ".");
        File workingDir;

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
        
        return workingDir;
    }

}
