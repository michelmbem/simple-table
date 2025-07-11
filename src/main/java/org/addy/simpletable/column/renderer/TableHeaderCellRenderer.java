package org.addy.simpletable.column.renderer;

import org.addy.simpletable.column.spec.CellFormat;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Objects;

public class TableHeaderCellRenderer implements TableCellRenderer {
    private final TableCellRenderer wrappedCellRenderer;
    private CellFormat cellFormat;

    public TableHeaderCellRenderer(TableCellRenderer wrappedCellRenderer) {
        this.wrappedCellRenderer = Objects.requireNonNull(wrappedCellRenderer,
                "TableHeaderCellRenderer requires a valid TableCellRenderer");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        Component component = wrappedCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        /* isEditor is set to true to prevent the border to be set */
        if (cellFormat != null) cellFormat.applyTo(component, true);
        return component;
    }

    public CellFormat getCellFormat() {
        return cellFormat;
    }

    public void setCellFormat(CellFormat cellFormat) {
        this.cellFormat = cellFormat.withBorder(null);
        if (this.cellFormat.getHorizontalAlignment() < 0)
            this.cellFormat.setHorizontalAlignment(SwingConstants.LEADING);
        if (this.cellFormat.getVerticalAlignment() < 0)
            this.cellFormat.setVerticalAlignment(SwingConstants.CENTER);
    }
}
