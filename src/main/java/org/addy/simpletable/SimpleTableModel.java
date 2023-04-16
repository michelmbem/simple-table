package org.addy.simpletable;

import org.addy.simpletable.column.adapter.*;
import org.addy.simpletable.column.converter.CellConverter;
import org.addy.simpletable.column.definition.ColumnDefinition;
import org.addy.simpletable.row.adapter.ArrayRowAdapter;
import org.addy.simpletable.row.adapter.ListRowAdapter;
import org.addy.simpletable.row.adapter.ResultSetRowAdapter;
import org.addy.simpletable.row.adapter.RowAdapter;
import org.addy.util.CollectionUtil;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
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
    private Object itemSource;
    private ColumnDefinition[] columns;
    private RowAdapter rowAdapter;
    private ColumnAdapter columnAdapter;
    private boolean editable = true;

    public SimpleTableModel(Object itemSource, ColumnDefinition[] columns, RowAdapter rowAdapter, ColumnAdapter columnAdapter) {
        this.itemSource = itemSource;
        this.columns = columns;
        this.rowAdapter = rowAdapter;
        this.columnAdapter = columnAdapter;
        initColumnAdapter(columnAdapter);
    }

    public SimpleTableModel(Object itemSource, String[] columnNames, RowAdapter rowAdapter, ColumnAdapter columnAdapter) {
        this(itemSource, ColumnDefinition.fromNames(columnNames), rowAdapter, columnAdapter);
    }

    public SimpleTableModel(Object[] items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(items, ColumnDefinition.fromNames(columnNames), new ArrayRowAdapter(), columnAdapter);
    }

    public SimpleTableModel(Object[] items, String... columnNames) {
        this(items, ColumnDefinition.fromNames(columnNames), new ArrayRowAdapter(),
                ColumnAdapters.from(requiredFirst(items).getClass(), columnNames));
    }

    public SimpleTableModel(Object[][] items, String... columnNames) {
        this(items, ColumnDefinition.fromNames(columnNames), new ArrayRowAdapter(), new ArrayColumnAdapter());
    }

    public SimpleTableModel(Collection<?> items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(CollectionUtil.toList(items), ColumnDefinition.fromNames(columnNames), new ListRowAdapter(), columnAdapter);
    }

    public SimpleTableModel(Collection<?> items, String... columnNames) {
        this(CollectionUtil.toList(items), ColumnDefinition.fromNames(columnNames), new ListRowAdapter(),
                ColumnAdapters.from(requiredFirst(items).getClass(), columnNames));
    }

    public SimpleTableModel(Class<?> itemClass, String... columnNames) {
        this(null, ColumnDefinition.fromNames(columnNames), new ListRowAdapter(),
                ColumnAdapters.from(Objects.requireNonNull(itemClass), columnNames));
    }

    public SimpleTableModel(ResultSet resultSet, String... columnNames) {
        this(resultSet, ColumnDefinition.fromResultSetAndNames(resultSet, columnNames),
                new ResultSetRowAdapter(), new ResultSetColumnAdapter());
    }

    public SimpleTableModel() {
        this(null,(ColumnDefinition[]) null, null, null);
    }

    public Object getItemSource() {
        return itemSource;
    }

    public void setItemSource(Object itemSource) {
        this.itemSource = itemSource;
        fireTableDataChanged();
    }

    public ColumnDefinition[] getColumns() {
        return columns;
    }

    public void setColumns(ColumnDefinition[] columns) {
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
        ColumnDefinition column = columns[columnIndex];

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
                aca.setColumnNames(Stream.of(columns).map(ColumnDefinition::getName).toArray(String[]::new));
            }
        }
    }
}
