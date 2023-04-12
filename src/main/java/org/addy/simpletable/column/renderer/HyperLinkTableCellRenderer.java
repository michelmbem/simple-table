package org.addy.simpletable.column.renderer;

import org.addy.simpletable.util.HyperLink;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class HyperLinkTableCellRenderer extends DefaultTableCellRenderer implements MouseMotionListener {
    private  static final Cursor LINK_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private JTable table = null;
    private int columnIndex = -1;
    private boolean mouseOver = false;
    private Cursor originalTableCursor = null;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (this.table == null) {
            this.table = table;
            columnIndex = column;
            originalTableCursor = table.getCursor();
            table.addMouseMotionListener(this);
        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    @Override
    protected void setValue(Object value) {
        super.setValue(HyperLink.format(value));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Unused!
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (table.columnAtPoint(e.getPoint()) == columnIndex && !mouseOver) {
            mouseOver = true;
            table.setCursor(LINK_CURSOR);
        } else if (table.columnAtPoint(e.getPoint()) != columnIndex && mouseOver) {
            mouseOver = false;
            table.setCursor(originalTableCursor);
        }
    }
}
