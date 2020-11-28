package org.addy.simpletable;

/**
 *
 * @author Mike
 */
public class ArrayRowAdapter extends BasicRowAdapter {

    @Override
    public Object getValueAt(Object item, int columnIndex, Class columnClass) {
        return ((Object[]) item)[columnIndex];
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Class columnClass, Object value) {
        ((Object[]) item)[columnIndex] = value;
    }

}
