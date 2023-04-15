package org.addy.simpletable.row.adapter;

import java.util.List;

public class ListRowAdapter extends BasicRowAdapter {
    @Override
    public int getRowCount(Object itemSource) {
        return ((List) itemSource).size();
    }

    @Override
    public Object getRowAt(Object itemSource, int rowIndex) {
        return ((List) itemSource).get(rowIndex);
    }

    @Override
    public void setRowAt(Object itemSource, int rowIndex, Object item) {
        ((List) itemSource).set(rowIndex, item);
    }

    @Override
    public void addRow(Object itemSource, Object item) {
        ((List) itemSource).add(item);
    }

    @Override
    public void insertRowAt(Object itemSource, int rowIndex, Object item) {
        ((List) itemSource).add(rowIndex, item);
    }

    @Override
    public void removeRowAt(Object itemSource, int rowIndex) {
        ((List) itemSource).remove(rowIndex);
    }

    @Override
    public void removeAllRows(Object itemSource) {
        ((List) itemSource).clear();
    }
}
