package org.addy.simpletable;

import org.addy.simpletable.column.adapter.*;
import org.addy.simpletable.row.adapter.ArrayRowAdapter;
import org.addy.simpletable.row.adapter.ListRowAdapter;
import org.addy.simpletable.row.adapter.ResultSetRowAdapter;
import org.addy.simpletable.row.adapter.RowAdapter;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.addy.util.CollectionUtil.requiredFirst;

/**
 *
 * @author Mike
 */
public class SimpleTableModel extends AbstractTableModel {
    private Object itemSource;
    private String[] columnNames;
    private Class<?>[] columnClasses;
    private RowAdapter rowAdapter;
    private ColumnAdapter columnAdapter;
    private boolean editable = true;

    public SimpleTableModel(Object itemSource, String[] columnNames, Class<?>[] columnClasses, RowAdapter rowAdapter, ColumnAdapter columnAdapter) {
        checkSameSize(columnNames, columnClasses);
        this.itemSource = itemSource;
        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
        this.rowAdapter = rowAdapter;
        this.columnAdapter = columnAdapter;
        initColumnAdapter(columnAdapter);
    }

    public SimpleTableModel(Object itemSource, String[] columnNames, RowAdapter rowAdapter, ColumnAdapter columnAdapter) {
        this(itemSource, columnNames, null, rowAdapter, columnAdapter);
    }

    public SimpleTableModel(Object[] items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(items, columnNames, new ArrayRowAdapter(), columnAdapter);
    }

    public SimpleTableModel(Object[] items, String... columnNames) {
        this(items, columnNames, new ArrayRowAdapter(), ColumnAdapters.from(requiredFirst(items).getClass(), columnNames));
    }

    public SimpleTableModel(Object[][] items, String... columnNames) {
        this(items, columnNames, new ArrayRowAdapter(), new ArrayColumnAdapter());
    }

    public SimpleTableModel(Collection<?> items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(new ArrayList<>(items), columnNames, new ListRowAdapter(), columnAdapter);
    }

    public SimpleTableModel(Collection<?> items, String... columnNames) {
        this(new ArrayList<>(items), columnNames, new ListRowAdapter(), ColumnAdapters.from(requiredFirst(items).getClass(), columnNames));
    }

    public SimpleTableModel(Class<?> itemClass, String... columnNames) {
        this(new ArrayList<>(), columnNames, new ListRowAdapter(), ColumnAdapters.from(itemClass, columnNames));
    }

    public SimpleTableModel(ResultSet resultSet, String... columnNames) {
        this(resultSet, columnNames, new ResultSetRowAdapter(), new ResultSetColumnAdapter());
    }

    public SimpleTableModel() {
        this(null, null, null, null, null);
    }

    public Object getItemSource() {
        return itemSource;
    }

    public void setItemSource(Object itemSource) {
        this.itemSource = itemSource;
        fireTableDataChanged();
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        this.columnClasses = null;
        fireTableStructureChanged();
    }

    public Class<?>[] getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(Class<?>[] columnClasses) {
        checkSameSize(this.columnNames, columnClasses);
        this.columnClasses = columnClasses;
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
        return columnNames != null ? columnNames.length : 0;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnClasses == null)
            guessColumnClasses();

        return columnClasses[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object row = rowAdapter.getRowAt(itemSource, rowIndex);

        return !(row == null || columnAdapter == null)
                ? columnAdapter.getValueAt(row, columnIndex)
                : null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Object row = rowAdapter.getRowAt(itemSource, rowIndex);

        if (!(row == null || columnAdapter == null)) {
            columnAdapter.setValueAt(row, columnIndex, value);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable &&
                (rowAdapter == null || rowAdapter.isCellEditable(rowIndex)) &&
                (columnAdapter == null || columnAdapter.isCellEditable(columnIndex));
    }

    private void checkSameSize(String[] columnNames, Class<?>[] columnClasses) {
        if (columnClasses == null) return;

        if (columnNames != null && columnNames.length != columnClasses.length)
            throw new IllegalArgumentException("When columnClasses is not null it must have the exact same size than columnNames");
    }

    private void initColumnAdapter(ColumnAdapter columnAdapter) {
        if (columnAdapter instanceof AssociativeColumnAdapter) {
            AssociativeColumnAdapter aca = (AssociativeColumnAdapter) columnAdapter;
            if (aca.getColumnNames() == null || aca.getColumnNames().length == 0) {
                aca.setColumnNames(columnNames);
            }
        }
    }

    private void guessColumnClasses() {
        if (columnNames != null) {
            columnClasses = new Class[columnNames.length];

            if (getRowCount() > 0) {
                for (int i = 0; i < columnClasses.length; ++i) {
                    columnClasses[i] = getValueAt(0, i).getClass();
                }
            } else if (columnAdapter instanceof BeanColumnAdapter) {
                BeanColumnAdapter bca = (BeanColumnAdapter) columnAdapter;

                for (int i = 0; i < columnClasses.length; ++i) {
                    columnClasses[i] = bca.getColumnClassAt(i);
                }
            } else {
                Arrays.fill(columnClasses, Object.class);
            }
        }
    }
}
