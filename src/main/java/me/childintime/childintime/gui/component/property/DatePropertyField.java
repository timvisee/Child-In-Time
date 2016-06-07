package me.childintime.childintime.gui.component.property;

import com.toedter.calendar.JCalendar;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePropertyField extends TextPropertyField {

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed.
     */
    public DatePropertyField(boolean allowNull) {
        super(allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Value.
     * @param allowNull True if null is allowed.
     */
    public DatePropertyField(String value, boolean allowNull) {
        super(value, allowNull);
    }

    @Override
    protected JTextField buildUiTextField() {
        try {
            // Create the formatter with the date mask
            MaskFormatter formatter = new MaskFormatter("####-##-##");

            // Create a calendar instance, to fetch the current date from
            Calendar c = new GregorianCalendar();

            // Configure the place holders for the mask formatter
            formatter.setPlaceholderCharacter('?');
            formatter.setPlaceholder(String.format("%02d", c.get(Calendar.YEAR)) + "-" +
                            String.format("%02d", c.get(Calendar.MONTH)) + "-" +
                            String.format("%02d", c.get(Calendar.DAY_OF_MONTH)));


            JPanel a = new JCalendar(new GregorianCalendar());
            JOptionPane.showMessageDialog(this, a, "MyTitle", JOptionPane.PLAIN_MESSAGE);


            // Create and return the formatted text field
            return new JFormattedTextField(formatter);

        } catch(ParseException e) {
            // Throw an exception
            throw new RuntimeException("Failed to configure date mask parser.", e);
        }
    }
}
