package org.addy.simpletable.row.adapter;

public abstract class BasicRowAdapter implements RowAdapter {
    @Override
    public boolean isCellEditable(int rowIndex) {
        return true;
    }
}
