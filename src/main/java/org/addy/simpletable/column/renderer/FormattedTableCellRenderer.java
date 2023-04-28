package org.addy.simpletable.column.renderer;

import org.addy.util.TypeConverter;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.Format;

public class FormattedTableCellRenderer extends DefaultTableCellRenderer {
    private final Format format;
    private final Class<?> renderedType;

    public FormattedTableCellRenderer(Format format, Class<?> renderedType) {
        this.format = format;
        this.renderedType = renderedType;
    }

    public FormattedTableCellRenderer(Format format) {
        this(format, null);
    }

    @Override
    protected void setValue(Object value) {
        if (renderedType != null)
            value = TypeConverter.toType(value, renderedType);

        setText(value != null ? format.format(value) : "");
    }
}
