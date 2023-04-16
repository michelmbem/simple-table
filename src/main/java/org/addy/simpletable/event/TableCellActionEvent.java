package org.addy.simpletable.event;

import javax.swing.*;

public class TableCellActionEvent extends TableCellEvent {
    private final String command;

    public TableCellActionEvent(JTable table, int row, int column, Object value, String command) {
        super(table, row, column, value);
        this.command = command;
    }

    public TableCellActionEvent(JTable table, int row, int column, String command) {
        super(table, row, column);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
