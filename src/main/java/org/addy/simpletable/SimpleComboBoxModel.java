package org.addy.simpletable;

import org.addy.simpletable.row.adapter.ArrayRowAdapter;
import org.addy.simpletable.row.adapter.ListRowAdapter;
import org.addy.simpletable.row.adapter.RowAdapter;
import org.addy.util.CollectionUtil;

import javax.swing.*;
import java.util.Collection;
import java.util.Objects;

/**
 * A generic combobox model that virtually accepts any kind of data source.<br>
 * Uses a row adapter to extract items from the given data source.<br>
 * Can automatically detect which type of row adapter to use based on the constructor used to create an instance.
 *
 * @author Mike
 */
public class SimpleComboBoxModel<E> extends AbstractListModel<E> implements ComboBoxModel<E> {
    private final Object itemSource;
    private final RowAdapter rowAdapter;
    private E selectedItem = null;

    public SimpleComboBoxModel(Object itemSource, RowAdapter rowAdapter) {
        this.itemSource = Objects.requireNonNull(itemSource);
        this.rowAdapter = Objects.requireNonNull(rowAdapter);
    }

    public SimpleComboBoxModel(E[]  items) {
        this(items, new ArrayRowAdapter());
    }

    public SimpleComboBoxModel(Collection<E> items) {
        this(CollectionUtil.toList(items), new ListRowAdapter());
    }

    public Object getItemSource() {
        return itemSource;
    }

    public RowAdapter getRowAdapter() {
        return rowAdapter;
    }

    @Override
    public int getSize() {
        return rowAdapter.getRowCount(itemSource);
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
        fireIntervalAdded(itemSource, index, index);
    }

    public void insertItemAt(int index, E anItem) {
        rowAdapter.insertRowAt(itemSource, index, anItem);
        fireIntervalAdded(itemSource, index, index);
    }

    public void setItemAt(int index, E anItem) {
        rowAdapter.setRowAt(itemSource, index, anItem);
        fireContentsChanged(itemSource, index, index);
    }

    public void removeItemAt(int index) {
        rowAdapter.removeRowAt(itemSource, index);
        fireIntervalRemoved(itemSource, index, index);
    }

    public void removeAllItems() {
        int size = getSize();
        rowAdapter.removeAllRows(itemSource);
        fireIntervalRemoved(itemSource, 0, size);
    }
}
