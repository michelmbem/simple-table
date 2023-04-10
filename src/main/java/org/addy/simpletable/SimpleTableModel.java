package org.addy.simpletable;

import org.addy.simpletable.column.adapter.*;
import org.addy.simpletable.row.adapter.ArrayRowAdapter;
import org.addy.simpletable.row.adapter.ListRowAdapter;
import org.addy.simpletable.row.adapter.RowAdapter;

import java.util.*;
import java.util.stream.Stream;

/**
 *
 * @author Mike
 */
public class SimpleTableModel extends javax.swing.table.AbstractTableModel {

    private static final String OUT_OF_BOUNDS_MESSAGE = "index is out of the bounds of the item's indices";

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
        this(items, columnNames, new ArrayRowAdapter(), inferColumnAdapterFrom(first(items).getClass(), columnNames));
    }

    public SimpleTableModel(Object[][] items, String... columnNames) {
        this(items, columnNames, new ArrayRowAdapter(), new ArrayColumnAdapter());
    }

    public SimpleTableModel(Collection<?> items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(new ArrayList<>(items), columnNames, new ListRowAdapter(), columnAdapter);
    }

    public SimpleTableModel(Collection<?> items, String... columnNames) {
        this(new ArrayList<>(items), columnNames, new ListRowAdapter(), inferColumnAdapterFrom(first(items).getClass(), columnNames));
    }

    public SimpleTableModel(Class<?> itemClass, String... columnNames) {
        this(new ArrayList<>(), columnNames, new ListRowAdapter(), inferColumnAdapterFrom(itemClass, columnNames));
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
        fireTableStructureChanged();
    }

    public Class<?>[] getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(Class<?>[] columnClasses) {
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

    public ColumnAdapter getColumndapter() {
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
        Object item = rowAdapter.getRowAt(itemSource, rowIndex);

        if (!(item == null || columnAdapter == null)) {
            columnAdapter.setValueAt(item, columnIndex, value);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable &&
                (rowAdapter == null || rowAdapter.isCellEditable(rowIndex)) &&
                (columnAdapter == null || columnAdapter.isCellEditable(columnIndex));
    }

    private static void checkSameSize(String[] columnNames, Class<?>[] columnClasses) {
        if (columnClasses == null) return;

        if (columnNames != null && columnNames.length != columnClasses.length)
            throw new IllegalArgumentException("columnNames and columnClasses must either be both null or have the same length");
    }

    private static Object first(Object[] array) {
        return Stream.of(array).findFirst().orElseThrow();
    }

    private static Object first(Collection<?> collection) {
        return collection.stream().findFirst().orElseThrow();
    }

    private static ColumnAdapter inferColumnAdapterFrom(Class<?> itemClass, String[] columnNames) {
        if (itemClass.isArray())
            return new ArrayColumnAdapter();

        if (List.class.isAssignableFrom(itemClass))
            return new ListColumnAdapter();

        if (Map.class.isAssignableFrom(itemClass))
            return new MapColumnAdapter(columnNames);

        return new BeanColumnAdapter(itemClass, columnNames);
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
