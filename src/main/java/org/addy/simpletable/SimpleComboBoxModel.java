package org.addy.simpletable;

import org.addy.simpletable.row.adapter.ArrayRowAdapter;
import org.addy.simpletable.row.adapter.ListRowAdapter;
import org.addy.simpletable.row.adapter.RowAdapter;
import org.addy.util.CollectionUtil;

import javax.swing.*;
import java.util.Collection;

/**
 * A generic combobox model that virtually accepts any kind of data source.<br>
 * Uses a row adapter to extract items from the given data source.<br>
 * Can automatically detect which type of row adapter to use based on the constructor used to create an instance.
 *
 * @author Mike
 */
public class SimpleComboBoxModel<E> extends AbstractListModel<E> implements ComboBoxModel<E> {
    private Object itemSource;
    private RowAdapter rowAdapter;
    private E selectedItem = null;

    public SimpleComboBoxModel(Object itemSource, RowAdapter rowAdapter) {
        this.itemSource = itemSource;
        this.rowAdapter = rowAdapter;
    }

    public SimpleComboBoxModel(E[]  items) {
        this(items, new ArrayRowAdapter());
    }

    public SimpleComboBoxModel(Collection<E> items) {
        this(CollectionUtil.toList(items), new ListRowAdapter());
    }

    public SimpleComboBoxModel() {
        this(null, null);
    }

    public Object getItemSource() {
        return itemSource;
    }

    public void setItemSource(Object itemSource) {
        this.itemSource = itemSource;
        fireContentsChanged(this, 0, getSize());
    }

    public RowAdapter getRowAdapter() {
        return rowAdapter;
    }

    public void setRowAdapter(RowAdapter rowAdapter) {
        this.rowAdapter = rowAdapter;
        fireContentsChanged(this, 0, getSize());
    }

    @Override
    public int getSize() {
        return !(itemSource == null || rowAdapter == null) ? rowAdapter.getRowCount(itemSource) : 0;
    }

    @Override
    public E getElementAt(int index) {
        return (E) rowAdapter.getRowAt(itemSource, index);
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = (E) anItem;
    }

    public void addItem(E anItem) {
        int index = getSize();
        rowAdapter.addRow(itemSource, anItem);
        fireIntervalAdded(this, index, index);
    }

    public void insertItemAt(int index, E anItem) {
        rowAdapter.insertRowAt(itemSource, index, anItem);
        fireIntervalAdded(this, index, index);
    }

    public void setItemAt(int index, E anItem) {
        rowAdapter.setRowAt(itemSource, index, anItem);
        fireContentsChanged(this, index, index);
    }

    public void removeItemAt(int index) {
        rowAdapter.removeRowAt(itemSource, index);
        fireIntervalRemoved(this, index, index);
    }

    public void removeAllItems() {
        int size = getSize();
        rowAdapter.removeAllRows(itemSource);
        fireIntervalRemoved(this, 0, size);
    }
}
