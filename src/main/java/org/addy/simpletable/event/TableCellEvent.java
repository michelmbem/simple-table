package org.addy.simpletable.event;

import javax.swing.*;

public class TableCellEvent {
    private final JTable table;
    private final int row;
    private final int column;
    private final Object value;

    public TableCellEvent(JTable table, int row, int column, Object value) {
        this.table = table;
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public TableCellEvent(JTable table, int row, int column) {
        this(table, row, column, null);
    }

    public JTable getTable() {
        return table;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}
