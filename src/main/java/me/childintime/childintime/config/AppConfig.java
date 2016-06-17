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

package me.childintime.childintime.config;

import me.childintime.childintime.App;

import java.io.File;

public class AppConfig extends Config {

    /**
     * Configuration file name.
     */
    public static final String CONFIG_FILE_NAME = "config.yml";

    /**
     * Configuration file.
     */
    public static final File CONFIG_FILE = new File(App.getDirectory(), CONFIG_FILE_NAME);

    /**
     * Constructor.
     * Note: this automatically uses the default configuration file of the application.
     */
    public AppConfig() {
        // Create a configuration file instance
        this(CONFIG_FILE);
    }

    /**
     * Constructor.
     *
     * @param configFile Configuration file.
     */
    public AppConfig(File configFile) {
        super(configFile);
    }
}
