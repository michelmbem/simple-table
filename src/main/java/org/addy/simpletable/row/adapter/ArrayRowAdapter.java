package org.addy.simpletable.row.adapter;

public class ArrayRowAdapter extends BasicRowAdapter {
    @Override
    public int getRowCount(Object itemSource) {
        return ((Object[]) itemSource).length;
    }

    @Override
    public Object getRowAt(Object itemSource, int rowIndex) {
        return ((Object[]) itemSource)[rowIndex];
    }
}
