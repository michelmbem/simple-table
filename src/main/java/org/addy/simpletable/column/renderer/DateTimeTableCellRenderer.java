package org.addy.simpletable.column.renderer;

import org.addy.simpletable.util.DateFormats;
import org.addy.util.TypeConverter;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;

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
            setText(dateFormat.format(TypeConverter.toDate(value)));
        }
    }
}
