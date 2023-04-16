package org.addy.simpletable.column.adapter;

/**
 *
 * @author Mike
 */
public abstract class BasicColumnAdapter implements ColumnAdapter {
    @Override
    public boolean isCellEditable(int columnIndex) {
        return true;
    }
}
