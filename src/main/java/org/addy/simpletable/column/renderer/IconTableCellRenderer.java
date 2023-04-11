package org.addy.simpletable.column.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class IconTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    protected void setValue(Object value) {
        setIcon((Icon) value);
    }
}
