package org.addy.simpletable;

import org.addy.simpletable.rows.RowAdapter;
import org.addy.simpletable.rows.RowAdapterWithColumnNames;
import org.addy.simpletable.rows.BeanRowAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Mike
 */
public class SimpleTableModel extends javax.swing.table.AbstractTableModel {

    private static final String DIFFERENT_SIZE_MESSAGE = "columnNames and columnClasses must either be both null or have the same length";
    private static final String OUT_OF_BOUNDS_MESSAGE = "index is out of the bounds of the item's indices";

    private String[] columnNames;
    private Class[] columnClasses;
    private List items;
    private RowAdapter rowAdapter;
    private boolean editable = true;

    public SimpleTableModel() {
        this(null, null, null, null);
    }

    public SimpleTableModel(String[] columnNames, Class[] columnClasses, List items, RowAdapter rowAdapter) {
        if ((columnNames == null && columnClasses != null)
                || (columnNames != null && columnClasses == null)
                || (columnNames != null && columnNames.length != columnClasses.length)) {

            throw new IllegalArgumentException(DIFFERENT_SIZE_MESSAGE);
        }

        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
        this.items = items;
        this.rowAdapter = rowAdapter;
        initRowAdapter(rowAdapter);
    }

    public SimpleTableModel(String[] columnNames, List items, RowAdapter rowAdapter) {
        this.columnNames = columnNames;
        this.items = items;
        this.rowAdapter = rowAdapter;
        initRowAdapter(rowAdapter);
        initColumnClasses();
    }

    public SimpleTableModel(Class itemClass, String... propertyNames) {
        this(propertyNames, new ArrayList(), new BeanRowAdapter(itemClass, propertyNames));
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        fireTableStructureChanged();
    }

    public Class[] getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(Class[] columnClasses) {
        this.columnClasses = columnClasses;
        fireTableStructureChanged();
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
        fireTableDataChanged();
    }

    public RowAdapter getRowAdapter() {
        return rowAdapter;
    }

    public void setRowAdapter(RowAdapter rowAdapter) {
        this.rowAdapter = rowAdapter;
        initRowAdapter(rowAdapter);
        fireTableDataChanged();
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
        return items != null ? items.size() : 0;
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
    public Class getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object item = items.get(rowIndex);
        return !(item == null || rowAdapter == null)
                ? rowAdapter.getValueAt(item, columnIndex)
                : null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Object item = items.get(rowIndex);
        if (!(item == null || rowAdapter == null)) {
            rowAdapter.setValueAt(item, columnIndex, value);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable && (rowAdapter == null || rowAdapter.isCellEditable(columnIndex));
    }

    public void addItem(Object item) {
        if (items == null) {
            items = new ArrayList();
        }

        int index = items.size();
        items.add(item);
        fireTableRowsInserted(index, index);
    }

    public void addItem(int index, Object item) {
        if (items == null) {
            if (index == 0) {
                items = new ArrayList();
            } else {
                throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MESSAGE);
            }
        } else if (index > items.size()) {
            throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MESSAGE);
        }

        items.add(index, item);
        fireTableRowsInserted(index, index);
    }

    public Object getItem(int index) {
        if (items == null || index < 0 || items.size() <= index) {
            throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MESSAGE);
        }

        return items.get(index);
    }

    public void setItem(int index, Object item) {
        if (items == null || index < 0 || items.size() <= index) {
            throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MESSAGE);
        }

        items.set(index, item);
        fireTableRowsUpdated(index, index);
    }

    public void removeItem(int index) {
        if (items == null || index < 0 || items.size() <= index) {
            throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MESSAGE);
        }

        items.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void removeItem(Object item) {
        int index = items.indexOf(item);
        if (index >= 0) {
            items.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    public void addItems(Collection items) {
        if (items == null) {
            items = new ArrayList(items);
            fireTableRowsInserted(0, items.size());
        } else {
            int index = this.items.size();
            this.items.addAll(items);
            fireTableRowsInserted(index, index + items.size());
        }
    }

    public void addItems(int index, Collection items) {
        if (this.items == null) {
            if (index == 0) {
                this.items = new ArrayList(items);
                fireTableRowsInserted(0, items.size());
            } else {
                throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MESSAGE);
            }
        } else if (index > this.items.size()) {
            throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MESSAGE);
        }

        this.items.addAll(index, items);
        fireTableRowsInserted(index, index + items.size());
    }

    public void removeItems(int index, int count) {
        if (items == null || index < 0 || items.size() <= index + count) {
            throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MESSAGE);
        }

        for (int i = 0; i < count; ++i) {
            items.remove(index);
        }

        fireTableRowsDeleted(index, index + count);
    }

    public void clearItems() {
        int size = items.size();
        items.clear();
        fireTableRowsDeleted(0, size - 1);
    }

    private void initRowAdapter(RowAdapter ra) {
        if (ra instanceof RowAdapterWithColumnNames) {
            RowAdapterWithColumnNames rawcn = (RowAdapterWithColumnNames) ra;
            if (rawcn.getColumnNames() == null || rawcn.getColumnNames().length == 0) {
                rawcn.setColumnNames(columnNames);
            }
        }
    }

    private void initColumnClasses() {
        if (columnNames != null) {
            this.columnClasses = new Class[columnNames.length];
            if (items != null && items.size() > 0) {
                for (int i = 0; i < columnNames.length; ++i) {
                    this.columnClasses[i] = this.getValueAt(0, i).getClass();
                }
            } else if (rowAdapter instanceof BeanRowAdapter) {
                BeanRowAdapter bra = (BeanRowAdapter) rowAdapter;
                for (int i = 0; i < columnNames.length; ++i) {
                    this.columnClasses[i] = bra.getColumnClassAt(i);
                }
            }
        }
    }

}
