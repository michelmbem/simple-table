package org.addy.simpletable.column.renderer;

import org.addy.util.TypeConverter;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;

public class ProgressTableCellRenderer extends JPanel implements TableCellRenderer {
    private final JProgressBar progressBar ;
    private final NumberFormat numberFormat;

    public ProgressTableCellRenderer(int min, int max, boolean stringPainted, NumberFormat numberFormat) {
        super(new GridBagLayout());

        var gbc = new GridBagConstraints(
                0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        progressBar = new JProgressBar(min, max);
        progressBar.setStringPainted(stringPainted);
        add(progressBar, gbc);

        this.numberFormat = numberFormat;
    }

    public ProgressTableCellRenderer(int min, int max) {
        this(min, max, false, null);
    }

    public ProgressTableCellRenderer() {
        this(0, 100, false, null);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        progressBar.setValue(TypeConverter.toInt(value));

        if (progressBar.isStringPainted() && numberFormat != null)
            progressBar.setString(numberFormat.format(value));

        return this;
    }
}
