package org.addy.simpletable.column.editor;

import org.addy.simpletable.util.UIHelper;
import org.addy.util.TypeConverter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class RangeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel;
    private final JSlider slider;

    public RangeTableCellEditor(int min, int max) {
        this.panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints(
                0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        slider = new JSlider(min, max);
        panel.add(slider, gbc);
    }

    public RangeTableCellEditor() {
        this(0, 100);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        UIHelper.prepareEditor(panel, table, value, isSelected, row, column);
        slider.setValue(TypeConverter.toInt(value));

        return slider;
    }

    @Override
    public Object getCellEditorValue() {
        return slider.getValue();
    }
}
