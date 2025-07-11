package org.addy.simpletable.column.renderer;

import org.addy.swing.JPictureBox;
import org.addy.swing.SizeMode;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class ImageTableCellRenderer extends JPictureBox implements TableCellRenderer {
    private static final Border NO_FOCUS_BORDER;
    private static final Image ERROR_IMAGE;

    static {
        NO_FOCUS_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);

        URL errorImageURL = ImageTableCellRenderer.class.getClassLoader().getResource("images/icons/Error.png");
        ERROR_IMAGE = new ImageIcon(Objects.requireNonNull(errorImageURL)).getImage();
    }

    public ImageTableCellRenderer() {
        super();
        setSizeMode(SizeMode.CONTAIN);
    }

    public ImageTableCellRenderer(SizeMode sizeMode) {
        super();
        super.setSizeMode(sizeMode);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setBorder(hasFocus ? UIManager.getBorder("Table.focusCellHighlightBorder") : NO_FOCUS_BORDER);
        setValue(value);

        return this;
    }

    @Override
    public void invalidate() {
        // Unused!
    }

    @Override
    public void validate() {
        // Unused!
    }

    @Override
    public void revalidate() {
        // Unused!
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        // Unused!
    }

    @Override
    public void repaint(Rectangle r) {
        // Unused!
    }

    @Override
    public void repaint() {
        // Unused!
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (propertyName.equals("imageSource") || propertyName.equals("image") || propertyName.equals("sizeMode")) {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    private void setValue(Object value) {
        try {
            setImageSource(value);
        } catch (IllegalArgumentException e) {
            setImage(ERROR_IMAGE);
        }
    }
}
