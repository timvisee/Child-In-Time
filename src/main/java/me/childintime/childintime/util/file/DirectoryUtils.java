/******************************************************************************
 * Copyright (c) Child-In-Time 2016. All rights reserved.                     *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @author Nathan Bakhuijzen                                                  *
 * @author Timo van den Boom                                                  *
 * @author Jos van Gent                                                       *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software")  *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

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
