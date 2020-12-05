package org.addy.simpletable.rows;

/**
 *
 * @author Mike
 */
public abstract class BasicRowAdapter implements RowAdapter {

    @Override
    public boolean isCellEditable(int columnIndex) {
        return true;
    }

}
