package org.addy.simpletable.column.renderer;

import org.addy.simpletable.util.NumberFormats;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.NumberFormat;

public class NumberTableCellRenderer extends DefaultTableCellRenderer {
    private final NumberFormat numberFormat;

    public NumberTableCellRenderer(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    public NumberTableCellRenderer(String pattern) {
        this(NumberFormats.of(pattern));
    }

    public NumberTableCellRenderer() {
        this(NumberFormat.getInstance());
    }

    @Override
    protected void setValue(Object value) {
        if (value == null) {
            setText("");
        } else {
            setText(numberFormat.format(value));
        }
    }
}
