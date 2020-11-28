package org.addy.simpletable;

/**
 *
 * @author Mike
 */
public abstract class BasicRowAdapter implements RowAdapter {

    @Override
    public boolean isCellEditable(int columnIndex) {
        return true;
    }

    @Override
    public abstract Object getValueAt(Object item, int columnIndex, Class columnClass);

    @Override
    public abstract void setValueAt(Object item, int columnIndex, Class columnClass, Object value);

}
