package org.addy.simpletable.row.header;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.Objects;

public class RowHeaderModel extends AbstractTableModel {
    private final TableModel tableModel;

    public RowHeaderModel(TableModel tableModel) {
        this.tableModel = Objects.requireNonNull(tableModel, "RowHeaderModel requires a valid table model");
    }

    @Override
    public int getRowCount() {
        return tableModel.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
