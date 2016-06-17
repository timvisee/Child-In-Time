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

package me.childintime.childintime.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * An extension of JLabel which looks like a link and responds appropriately
 * when clicked. Note that this class will only work with Swing 1.1.1 and later.
 * Note that because of the way this class is implemented, getText() will not
 * return correct values, user <code>getNormalText</code> instead.
 */
public class LinkLabel extends JLabel {

    /**
     * The normal text set by the user.
     */
    private String text;

    /**
     * Creates a new LinkLabel with the given text.
     */
    public LinkLabel(String text) {
        // Call the super
        super(text);

        // Set the cursor mode
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Enable events
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
    }

    /**
     * Sets the text of the label.
     */
    public void setText(String text) {
        // Set the text of the actual label with the link color
        super.setText("<html><font color=\"#0000CF\"><u>" + text + "</u></font></html>");

        // Set the text
        this.text = text;
    }

    /**
     * Returns the text set by the user.
     */
    public String getNormalText() {
        return this.text;
    }

    /**
     * Processes mouse events and responds to clicks.
     */
    protected void processMouseEvent(MouseEvent evt) {
        super.processMouseEvent(evt);
        if(evt.getID() == MouseEvent.MOUSE_CLICKED)
            fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getNormalText()));
    }

    /**
     * Adds an ActionListener to the list of listeners receiving notifications
     * when the label is clicked.
     */
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    /**
     * Removes the given ActionListener from the list of listeners receiving
     * notifications when the label is clicked.
     */
    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    /**
     * Fires an ActionEvent to all interested listeners.
     */
    protected void fireActionPerformed(ActionEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for(int i = 0; i < listeners.length; i += 2) {
            if(listeners[i] == ActionListener.class) {
                ActionListener listener = (ActionListener) listeners[i + 1];
                listener.actionPerformed(evt);
            }
        }
    }
}
