package me.childintime.childintime.ui.component;

import javax.swing.*;
import java.awt.*;

public class GrowingLabel extends JLabel {

    /**
     * Constructor.
     *
     * @param text Text.
     * @param icon Icon.
     * @param horizontalAlignment Horizontal alignment.
     */
    public GrowingLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    /**
     * Constructor.
     *
     * @param text Text.
     * @param horizontalAlignment Horizontal alignment.
     */
    public GrowingLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }

    /**
     * Constructor.
     *
     * @param text Text.
     */
    public GrowingLabel(String text) {
        super(text);
    }

    /**
     * Constructor.
     *
     * @param image Image.
     * @param horizontalAlignment Horizontal alignment.
     */
    public GrowingLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    /**
     * Constructor.
     *
     * @param image Image.
     */
    public GrowingLabel(Icon image) {
        super(image);
    }

    /**
     * Constructor.
     */
    public GrowingLabel() { }

    @Override
    public void paint(Graphics g) {
        // Calculate the font size
        Font labelFont = this.getFont();
        String labelText = this.getText();

        int stringWidth = this.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = this.getWidth() - 2;

        // Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = this.getHeight();

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        this.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));

        // Paint the super
        super.paint(g);
    }
}
