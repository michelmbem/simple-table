package org.addy.simpletable;

import org.addy.simpletable.column.adapter.*;
import org.addy.simpletable.column.converter.CellConverter;
import org.addy.simpletable.column.metadata.ColumnMetaData;
import org.addy.simpletable.row.adapter.ArrayRowAdapter;
import org.addy.simpletable.row.adapter.ListRowAdapter;
import org.addy.simpletable.row.adapter.ResultSetRowAdapter;
import org.addy.simpletable.row.adapter.RowAdapter;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import static org.addy.util.CollectionUtil.requiredFirst;

/**
 * A generic table model that virtually accepts any kind of data source.<br>
 * Uses row and column adapters to extract rows and cells from the given data source.<br>
 * Can automatically detect which row or column adapter based on the constructor used to create an instance.
 *
 * @author Mike
 */
public class SimpleTableModel extends AbstractTableModel {
    private Object itemSource = null;
    private ColumnMetaData[] columns = null;
    private RowAdapter rowAdapter = null;
    private ColumnAdapter columnAdapter = null;
    private boolean editable = true;

    public SimpleTableModel() {}

    public SimpleTableModel(Object itemSource, ColumnMetaData[] columns, RowAdapter rowAdapter, ColumnAdapter columnAdapter) {
        this.itemSource = itemSource;
        this.columns = columns;
        this.rowAdapter = rowAdapter;
        this.columnAdapter = columnAdapter;
        initColumnAdapter(columnAdapter);
    }

    public SimpleTableModel(Object itemSource, String[] columnNames, RowAdapter rowAdapter, ColumnAdapter columnAdapter) {
        this(itemSource, ColumnMetaData.fromNames(columnNames), rowAdapter, columnAdapter);
    }

    public SimpleTableModel(Object[] items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(items, ColumnMetaData.fromNames(columnNames), new ArrayRowAdapter(), columnAdapter);
    }

    public SimpleTableModel(Object[] items, String... columnNames) {
        this(items, ColumnMetaData.fromNames(columnNames), new ArrayRowAdapter(),
                ColumnAdapters.from(requiredFirst(items).getClass(), columnNames));
    }

    public SimpleTableModel(Object[][] items, String... columnNames) {
        this(items, ColumnMetaData.fromNames(columnNames), new ArrayRowAdapter(), new ArrayColumnAdapter());
    }

    public SimpleTableModel(Collection<?> items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(new ArrayList<>(items), ColumnMetaData.fromNames(columnNames), new ListRowAdapter(), columnAdapter);
    }

    public SimpleTableModel(Collection<?> items, String... columnNames) {
        this(new ArrayList<>(items), ColumnMetaData.fromNames(columnNames), new ListRowAdapter(),
                ColumnAdapters.from(requiredFirst(items).getClass(), columnNames));
    }

    public SimpleTableModel(Class<?> itemClass, String... columnNames) {
        this(null, ColumnMetaData.fromNames(columnNames), new ListRowAdapter(),
                ColumnAdapters.from(Objects.requireNonNull(itemClass), columnNames));
    }

    public SimpleTableModel(ResultSet resultSet, String... columnNames) {
        this(resultSet, ColumnMetaData.fromResultSetAndNames(resultSet, columnNames), new ResultSetRowAdapter(), new ResultSetColumnAdapter());
    }

    public Object getItemSource() {
        return itemSource;
    }

    public void setItemSource(Object itemSource) {
        this.itemSource = itemSource;
        fireTableDataChanged();
    }

    public ColumnMetaData[] getColumns() {
        return columns;
    }

    public void setColumns(ColumnMetaData[] columns) {
        this.columns = columns;
        fireTableStructureChanged();
    }

    public RowAdapter getRowAdapter() {
        return rowAdapter;
    }

    public void setRowAdapter(RowAdapter rowAdapter) {
        this.rowAdapter = rowAdapter;
        fireTableDataChanged();
    }

    public ColumnAdapter getColumnAdapter() {
        return columnAdapter;
    }

    public void setColumnAdapter(ColumnAdapter columnAdapter) {
        this.columnAdapter = columnAdapter;
        initColumnAdapter(columnAdapter);
        fireTableStructureChanged();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return !(itemSource == null || rowAdapter == null) ? rowAdapter.getRowCount(itemSource) : 0;
    }

    @Override
    public int getColumnCount() {
        return columns != null ? columns.length : 0;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex].getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        ColumnMetaData column = columns[columnIndex];

        if (column.getType() == null)
            column.setType(getValueAt(0, columnIndex).getClass());

        return column.getType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CellConverter converter = columns[columnIndex].getConverter();
        Object row = rowAdapter.getRowAt(itemSource, rowIndex);
        Object value = !(row == null || columnAdapter == null)
                ? columnAdapter.getValueAt(row, columnIndex)
                : null;

        return converter != null ? converter.model2view(value, row) : value;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        CellConverter converter = columns[columnIndex].getConverter();
        Object row = rowAdapter.getRowAt(itemSource, rowIndex);

        if (!(row == null || columnAdapter == null)) {
            if (converter != null)
                value = converter.view2model(value, row);

            columnAdapter.setValueAt(row, columnIndex, value);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable && columns[columnIndex].isEditable() &&
                (rowAdapter == null || rowAdapter.isCellEditable(rowIndex)) &&
                (columnAdapter == null || columnAdapter.isCellEditable(columnIndex));
    }

    public void addRow(Object row) {
        rowAdapter.addRow(itemSource, row);
        fireTableDataChanged();
    }

    public void insertRowAt(int index, Object row) {
        rowAdapter.insertRowAt(itemSource, index, row);
        fireTableDataChanged();
    }

    public Object getRowAt(int index) {
        return rowAdapter.getRowAt(itemSource, index);
    }

    public void setRowAt(int index, Object row) {
        rowAdapter.setRowAt(itemSource, index, row);
        fireTableDataChanged();
    }

    public void removeRowAt(int index) {
        rowAdapter.removeRowAt(itemSource, index);
        fireTableDataChanged();
    }

    public void removeAllRows() {
        rowAdapter.removeAllRows(itemSource);
        fireTableDataChanged();
    }

    private void initColumnAdapter(ColumnAdapter columnAdapter) {
        if (columnAdapter instanceof AssociativeColumnAdapter) {
            AssociativeColumnAdapter aca = (AssociativeColumnAdapter) columnAdapter;
            if (aca.getColumnNames() == null || aca.getColumnNames().length == 0) {
                aca.setColumnNames(Stream.of(columns).map(ColumnMetaData::getName).toArray(String[]::new));
            }
        }
    }
}
