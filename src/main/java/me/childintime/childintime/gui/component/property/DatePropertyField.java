package me.childintime.childintime.gui.component.property;

import com.toedter.calendar.JCalendar;
import me.childintime.childintime.gui.component.property.context.ContextDateAction;
import me.childintime.childintime.util.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePropertyField extends TextPropertyField implements ActionListener, PropertyChangeListener {

    // TODO: Make sure the user doesn't manually enter a date that is out of the specified minimum/maximum range.

    /**
     * Swing serial version UID.
     */
    private static final long serialVersionUID = -2303712745720670722L;

    /**
     * Calendar button text.
     */
    public static final String BUTTON_CALENDAR_TEXT = "…";

    /**
     * Calendar button tooltip.
     */
    public static final String BUTTON_CALENDAR_TOOLTIP = "Choose date...";

    /**
     * Null placeholder text for the date property field.
     */
    public static final String NULL_PLACEHOLDER_TEXT = "YYYY-MM-DD";

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
     * Date chooser.
     */
    private JCalendar dateChooser;

    /**
     * Date chooser popup.
     */
    private JPopupMenu dateChooserPopup;

    /**
     * Defines whether a date is selected.
     */
    private boolean dateSelected;

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

    @Override
    protected JPopupMenu buildUiMenu() {
        // Build the super context menu
        JPopupMenu contextMenu = super.buildUiMenu();

        // Return null if the super was null
        if(contextMenu == null)
            return null;

        // Add a separator
        contextMenu.addSeparator();

        // Add the date selection context menu item
        contextMenu.add(new ContextDateAction(this));

        // Return the context menu
        return contextMenu;
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

        // Set the null placeholder text
        setNullPlaceholderText(NULL_PLACEHOLDER_TEXT);

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
                // Determine whether to fire a cancelled popup menu event
                Boolean fireCancelled = (Boolean) this.getClientProperty("JPopupMenu.firePopupMenuCanceled");

                // Determine whether to make the popup visible
                if(visible || instance.dateSelected || fireCancelled != null && fireCancelled)
                    super.setVisible(visible);
            }
        };
        this.dateChooserPopup.setLightWeightPopupEnabled(true);
        this.dateChooserPopup.add(this.dateChooser);
    }

    @Override
    public JPanel getActionButtonPanel() {
        // Create the clear button
        this.calendarButton = new JButton(BUTTON_CALENDAR_TEXT);
        this.calendarButton.setToolTipText(BUTTON_CALENDAR_TOOLTIP);

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

        // Create the button panel through the super
        final JPanel button = super.getActionButtonPanel();

        // Add the calendar button to the left of the panel
        button.add(this.calendarButton, 0);

        // Return the button panel
        return button;
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

    /**
     * Check whether a valid date is entered in the date property field.
     *
     * @return True if a valid date is entered, false if not.
     */
    public boolean isValidDate() {
        return getDate() != null;
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        // Show the date chooser
        showDateChooser();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // Make sure the day property changed
        if(!event.getPropertyName().equals("day"))
            return;

        // Make sure the data chooser popup is visible
        if(!this.dateChooserPopup.isVisible())
            return;

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

    /**
     * Show the date chooser.
     */
    public void showDateChooser() {
        // Determine the position of the date chooser
        final int x = this.textField.getSize().width - this.dateChooserPopup.getPreferredSize().width;
        final int y = this.textField.getSize().height;

        // Create a calendar instance to use
        Calendar calendar = Calendar.getInstance();

        // Set the date based on the value in the text field
        final Date date = getDate();
        if(date != null)
            calendar.setTime(date);

        // Set the date chooser calendar
        this.dateChooser.setCalendar(calendar);

        // Show the date chooser popup
        this.dateChooserPopup.show(this.textField, x, y);
        this.dateSelected = false;
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

    @Override
    public void disableIfEmpty() {
        // Clear the field if it's empty, and empty is allowed
        if(isNullAllowed() && !isNull() && !isValidDate())
            setNull(true);
    }

    /**
     * Get the minimum selectable date.
     *
     * @return Minimum selectable date.
     */
    public Date getMinimumDate() {
        return this.dateChooser.getMinSelectableDate();
    }

    /**
     * Set the minimum selectable date.
     *
     * @param date Minimum selectable date.
     */
    public void setMinimumDate(Date date) {
        this.dateChooser.setMinSelectableDate(date);
    }

    /**
     * Get the maximum selectable date.
     *
     * @return Maximum selectable date.
     */
    public Date getMaximumDate() {
        return this.dateChooser.getMinSelectableDate();
    }

    /**
     * Set the maximum selectable date.
     *
     * @param date Maximum selectable date.
     */
    public void setMaximumDate(Date date) {
        this.dateChooser.setMaxSelectableDate(date);
    }
}
