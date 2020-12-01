package org.addy.simpletable;

import java.util.List;

/**
 *
 * @author Mike
 */
public class ListRowAdapter extends BasicRowAdapter {

    @Override
    public Object getValueAt(Object item, int columnIndex) {
        return ((List) item).get(columnIndex);
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        ((List) item).set(columnIndex, value);
    }

}
