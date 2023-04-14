package org.addy.simpletable.column.renderer;

import org.addy.swing.JPictureBox;
import org.addy.swing.SizeMode;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.*;
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
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        // Unused!
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        switch (propertyName) {
            case "image":
            case "sizeMode":
                super.firePropertyChange(propertyName, oldValue, newValue);
                break;
            default:
                break;
        }
    }

    private void setValue(Object value) {
        try {
            if (value == null) {
                setImage(null);
            } else if (value instanceof Image) {
                setImage((Image) value);
            } else if (value instanceof ImageIcon) {
                setImage(((ImageIcon) value).getImage());
            } else if (value instanceof InputStream) {
                setImage(ImageIO.read((InputStream) value));
            } else if (value instanceof File) {
                setImage(ImageIO.read((File) value));
            } else if (value instanceof URL) {
                setImage(ImageIO.read((URL) value));
            } else if (value instanceof byte[]) {
                setImage(ImageIO.read(new ByteArrayInputStream((byte[]) value)));
            } else {
                setImage(ImageIO.read(new FileInputStream(value.toString())));
            }
        } catch (IOException e) {
            setImage(ERROR_IMAGE);
        }
    }
}
