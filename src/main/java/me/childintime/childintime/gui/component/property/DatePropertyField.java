package me.childintime.childintime.gui.component.property;

import com.toedter.calendar.JCalendar;
import me.childintime.childintime.util.Platform;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePropertyField extends TextPropertyField implements ActionListener, PropertyChangeListener {

    /**
     * Swing serial version UID.
     */
    private static final long serialVersionUID = -2303712745720670722L;

    /**
     * Date format to use.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Calendar button icon.
     */
    public static final String RESOURCE_CALENDAR_BUTTON_ICON = "/com/toedter/calendar/images/JDateChooserColor16.gif";

    /**
     * Calendar button.
     */
    private JButton calendarButton;

    /**
     * Date formatter instance.
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    /**
     * The date that was selected last.
     */
    private Date lastSelectedDate;

    /**
     * Date chooser.
     */
    private JCalendar dateChooser;

    /**
     * Date chooser popup.
     */
    private JPopupMenu dateChooserPopup;

    /**
     * Constructor.
     *
     * @param allowNull True if null is allowed.
     */
    public DatePropertyField(boolean allowNull) {
        this((Object) null, allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Date value as a string.
     * @param allowNull True if null is allowed.
     */
    public DatePropertyField(String value, boolean allowNull) {
        this((Object) value, allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Date value.
     * @param allowNull True if null is allowed.
     */
    public DatePropertyField(Date value, boolean allowNull) {
        this((Object) value, allowNull);
    }

    /**
     * Constructor.
     *
     * @param value Date value, as date object, date string or null.
     * @param allowNull True to allow null, false if not.
     */
    private DatePropertyField(Object value, boolean allowNull) {
        // Construct the super
        super(allowNull);

        // Set the component name
        this.setName("JDateChooser");

        // Create a variable for the date value
        Date valueDate = null;

        // Parse date object instances
        if(value instanceof Date)
            valueDate = (Date) value;

        // Parse date object from string
        if(value instanceof String)
            try {
                valueDate = this.dateFormat.parse((String) value);
            } catch(ParseException ignored) { }

        // Set the date, and the last selected date
        setDate(valueDate);
        this.lastSelectedDate = valueDate;

        // Create the calendar chooser
        this.dateChooser = new JCalendar(valueDate);

        this.dateChooser.getDayChooser().addPropertyChangeListener("day", this);
        this.dateChooser.getDayChooser().setAlwaysFireDayProperty(true);

        // Get and set the date chooser icon
        this.calendarButton.setText("");
        this.calendarButton.setIcon(new ImageIcon(this.getClass().getResource(RESOURCE_CALENDAR_BUTTON_ICON)));

        // Link the calendar button listeners
        this.calendarButton.addActionListener(this);

        // Store the current instance
        final DatePropertyField instance = this;

        // Create a popup menu with the date chooser chooser
        this.dateChooserPopup = new JPopupMenu() {
            /**
             * Swing serial version UID.
             */
            private static final long serialVersionUID = -6078272560337577761L;

            @Override
            public void setVisible(boolean visible) {

                Boolean fire = (Boolean) this.getClientProperty("JPopupMenu.firePopupMenuCanceled");

                if(visible || instance.dateSelected || fire != null && fire)
                    super.setVisible(visible);
            }
        };
        this.dateChooserPopup.setLightWeightPopupEnabled(true);
        this.dateChooserPopup.add(this.dateChooser);

        // TODO: What is this change listener used for?
        ChangeListener changeListener = new ChangeListener() {
            boolean hasListened = false;

            public void stateChanged(ChangeEvent var1) {
                if(this.hasListened) {
                    this.hasListened = false;
                } else {
                    if(DatePropertyField.this.dateChooserPopup.isVisible() && DatePropertyField.this.dateChooser.getMonthChooser().getComboBox().hasFocus()) {
                        MenuElement[] var2 = MenuSelectionManager.defaultManager().getSelectedPath();
                        MenuElement[] var3 = new MenuElement[var2.length + 1];
                        var3[0] = DatePropertyField.this.dateChooserPopup;

                        System.arraycopy(var2, 0, var3, 1, var2.length);

                        this.hasListened = true;
                        MenuSelectionManager.defaultManager().setSelectedPath(var3);
                    }

                }
            }
        };
        MenuSelectionManager.defaultManager().addChangeListener(changeListener);
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

            // Create and return the formatted text field
            return new JFormattedTextField(formatter);

        } catch(ParseException e) {
            // Throw an exception
            throw new RuntimeException("Failed to configure date mask parser.", e);
        }
    }

    @Override
    public JPanel getActionButtonPanel() {
        // Create the clear button
        this.calendarButton = new JButton("…");
        this.calendarButton.addActionListener(e -> {
            // Set the value to null
            setNull(true);
        });

        // Define the size of the clear button
        final int buttonSize = this.textField.getPreferredSize().height - 4;
        final Dimension buttonDimensions = new Dimension(buttonSize, buttonSize);
        this.calendarButton.setPreferredSize(buttonDimensions);
        this.calendarButton.setMinimumSize(buttonDimensions);
        this.calendarButton.setMaximumSize(buttonDimensions);
        this.calendarButton.setSize(buttonDimensions);
        this.calendarButton.setBorder(null);
        this.calendarButton.setFocusable(false);

        // Fix button styling on Mac OS X
        if(Platform.isMacOsX()) {
            this.calendarButton.putClientProperty("JButton.sizeVariant", "mini");
            this.calendarButton.putClientProperty("JButton.buttonType", "square");
            this.calendarButton.setMargin(new Insets(0, 0, 0, 0));
            this.calendarButton.setFont(new Font(this.clearButton.getFont().getFontName(), Font.PLAIN, this.clearButton.getFont().getSize() - 2));
        }

        // Call the super
        JPanel basePanel = super.getActionButtonPanel();

        // Add the button to the panel
        basePanel.add(this.calendarButton, 0);

        // Return the button panel
        return basePanel;
    }

    /**
     * Get the parsed date from the property field.
     * Null is returned if the date was invalid and/or couldn't be parsed.
     *
     * @return Date or null.
     */
    public Date getDate() {
        // Return null if the property field value is null
        if(isNull())
            return null;

        try {
            // Parse the date
            return this.dateFormat.parse(getText());

        } catch(ParseException e) {
            // Return null if date parsing failed
            return null;
        }
    }

    /**
     * Set the date in the property field.
     *
     * @param date Date, or null.
     */
    public void setDate(Date date) {
        // Set the field to the date, set the field to null if the given date is null
        if(date != null)
            setText(this.dateFormat.format(date));
        else
            setText(null);
    }









    private boolean dateSelected;

    @Override
    public void actionPerformed(ActionEvent action) {
        // Determine the position of the date chooser
        final int x = this.textField.getSize().width - this.dateChooserPopup.getPreferredSize().width;
        final int y = this.textField.getSize().height;

        // Create a calendar instance to use
        Calendar calendar = Calendar.getInstance();

        // Set the date based on the value in the text field
        Date date = getDate();
        if(date != null)
            calendar.setTime(date);

        // Set the date chooser calendar
        this.dateChooser.setCalendar(calendar);

        // Show the date chooser popup
        this.dateChooserPopup.show(this.textField, x, y);
        this.dateSelected = false;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // Handle date changes
        if(event.getPropertyName().equals("day")) {
            if(this.dateChooserPopup.isVisible()) {
                // Set the selected state
                this.dateSelected = true;

                // Hide the popup
                this.dateChooserPopup.setVisible(false);

                // Set the date
                if((Integer) event.getNewValue() > 0)
                    this.setDate(this.dateChooser.getCalendar().getTime());
                else
                    this.setDate(null);
            }
        }
    }

    @Override
    public void updateUI() {
        // Update the super ID
        super.updateUI();

        // Update the calendar chooser if available
        if(this.dateChooser != null)
            SwingUtilities.updateComponentTreeUI(this.dateChooserPopup);
    }

    @Override
    public boolean requestFocusInWindow() {
        return this.textField != null ? this.textField.requestFocusInWindow() : super.requestFocusInWindow();
    }
}
