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

package me.childintime.childintime.ui.component.property.context;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ContextDynamicAction extends AbstractAction {

    /**
     * Action.
     */
    private me.childintime.childintime.ui.component.property.action.AbstractAction action;

    /**
     * Constructor.
     */
    public ContextDynamicAction(me.childintime.childintime.ui.component.property.action.AbstractAction action) {
        // Construct the super with the name
        super(action.getDescription());

        // Set the property field instance
        this.action = action;
    }

    /**
     * Action listener.
     *
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        this.action.run();
    }
}
