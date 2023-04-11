package org.addy.simpletable.column.renderer;

import org.addy.simpletable.util.DateFormats;
import org.addy.util.DateUtil;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class DateTimeTableCellRenderer extends DefaultTableCellRenderer {
    private final DateFormat dateFormat;

    public DateTimeTableCellRenderer(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateTimeTableCellRenderer(String pattern) {
        this(DateFormats.of(pattern));
    }

    public DateTimeTableCellRenderer() {
        this(DateFormat.getInstance());
    }

    @Override
    protected void setValue(Object value) {
        if (value == null) {
            setText("");
        } else {
            setText(dateFormat.format(toDate(value)));
        }
    }

    protected static Date toDate(Object value) {
        Class<?> valueType = value.getClass();

        if (Date.class.isAssignableFrom(valueType))
            return (Date) value;

        if (LocalDate.class.isAssignableFrom(valueType))
            return DateUtil.toDate((LocalDate) value);

        if (LocalTime.class.isAssignableFrom(valueType))
            return DateUtil.toDate((LocalTime) value);

        if (LocalDateTime.class.isAssignableFrom(valueType))
            return DateUtil.toDate((LocalDateTime) value);

        try {
            return DateUtil.parseDate(value.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
