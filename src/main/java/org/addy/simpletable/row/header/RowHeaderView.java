package org.addy.simpletable.row.header;

import org.addy.simpletable.column.spec.CellFormat;
import org.addy.simpletable.column.spec.ColumnType;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class RowHeaderView extends JTable {
    private final CellFormat cellFormat;

    public RowHeaderView(JTable body, ColumnType columnType, String headerText, int width, CellFormat cellFormat) {
        super();
        this.cellFormat = cellFormat;

        setModel(new RowHeaderModel(body.getModel()));
        setSelectionModel(body.getSelectionModel());
        setRowHeight(body.getRowHeight());
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);
        setForeground(UIManager.getColor("TableHeader.foreground"));
        setBackground(UIManager.getColor("TableHeader.background"));

        TableColumn singleColumn = getColumnModel().getColumn(0);
        singleColumn.setPreferredWidth(width);
        singleColumn.setMinWidth(width);
        singleColumn.setMaxWidth(width);
        singleColumn.setHeaderValue(headerText);
        singleColumn.setCellRenderer(columnType.getRenderer(null));
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, row, column);
        cellFormat.applyTo(component, false);
        return component;
    }
}
