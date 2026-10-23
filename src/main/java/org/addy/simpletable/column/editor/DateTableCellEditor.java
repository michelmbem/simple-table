package org.addy.simpletable.column.editor;

import org.addy.swing.JCalendarCombo;
import org.addy.util.DateUtil;
import org.addy.util.ValueConverter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

public class DateTableCellEditor
        extends AbstractCellEditor
        implements TableCellEditor, PropertyChangeListener {

    private final JCalendarCombo calendarCombo = new JCalendarCombo();
    private Class<?> columnClass = null;

    public DateTableCellEditor(String dateFormat) {
        initDatePicker(dateFormat);
    }

    public DateTableCellEditor() {
        initDatePicker(DateUtil.IsoFormat.Extended.DATE);
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        if (columnClass == null)
            columnClass = table.getModel().getColumnClass(column);

        if (value != null)
            calendarCombo.setDate(ValueConverter.toLocalDate(value));
        else
            calendarCombo.setChecked(false);

        return calendarCombo;
    }

    @Override
    public Object getCellEditorValue() {
        return calendarCombo.isChecked()
                ? ValueConverter.toType(calendarCombo.getDate(), columnClass)
                : null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getSource() == calendarCombo.getCalendar()
                && "selectedDate".equals(e.getPropertyName())) {
            stopCellEditing();
        }
    }

    private void initDatePicker(String dateFormat) {
        calendarCombo.setDateFormat(mapFormat(dateFormat));
        calendarCombo.setCheckBoxVisible(true);
        calendarCombo.getCalendar().addPropertyChangeListener(this);
    }

    private static String mapFormat(String dateFormat) {
        return switch (dateFormat.toLowerCase(Locale.ROOT)) {
            case "g" -> DateUtil.IsoFormat.Extended.DATE;
            case "s", "short" -> "dd/MM/yy";
            case "m", "medium" -> "d MMM yyyy";
            case "l", "long" -> "d MMMM yyyy";
            default -> dateFormat;
        };
    }
}
