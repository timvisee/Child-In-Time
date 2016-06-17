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

package me.childintime.childintime.ui.component.property.action;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

public class TextPasteAction extends AbstractAction {

    /**
     * Constructor.
     */
    public TextPasteAction() {
        // Construct the super
        super(() -> {}, "Paste", "Paste");

        // Hide the button
        setShowButton(false);

        // Create and configure the action
        Action copyAction = new DefaultEditorKit.PasteAction();
        copyAction.putValue(Action.NAME, "Paste");
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        setContextMenuAction(copyAction);
    }

    @Override
    public void onNullChange(boolean _null) { }
}
