package org.addy.simpletable.column.renderer;

import org.addy.simpletable.util.HyperLink;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class HyperLinkTableCellRenderer extends DefaultTableCellRenderer {
    private  static final Cursor LINK_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    @Override
    protected void setValue(Object value) {
        super.setValue(HyperLink.format(value));
    }
}
