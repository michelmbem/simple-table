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
}
