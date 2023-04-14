package org.addy.simpletable.column.definition;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CellFormat {
    public static CellFormat DEFAULT = new CellFormat();
    public static CellFormat FIRST_LINE_START = new CellFormat(SwingConstants.LEADING, SwingConstants.LEADING, null, null, null, null);
    public static CellFormat PAGE_START = new CellFormat(SwingConstants.CENTER, SwingConstants.LEADING, null, null, null, null);
    public static CellFormat FIRST_LINE_END = new CellFormat(SwingConstants.TRAILING, SwingConstants.LEADING, null, null, null, null);
    public static CellFormat LINE_START = new CellFormat(SwingConstants.LEADING, SwingConstants.CENTER, null, null, null, null);
    public static CellFormat CENTER = new CellFormat(SwingConstants.CENTER, SwingConstants.CENTER, null, null, null, null);
    public static CellFormat LINE_END = new CellFormat(SwingConstants.TRAILING, SwingConstants.CENTER, null, null, null, null);
    public static CellFormat LAST_LINE_START = new CellFormat(SwingConstants.LEADING, SwingConstants.TRAILING, null, null, null, null);
    public static CellFormat PAGE_END = new CellFormat(SwingConstants.CENTER, SwingConstants.TRAILING, null, null, null, null);
    public static CellFormat LAST_lINE_END = new CellFormat(SwingConstants.TRAILING, SwingConstants.TRAILING, null, null, null, null);

    private int horizontalAlignment;
    private int verticalAlignment;
    private Color foreground;
    private Color background;
    private Font font;
    private Border border;

    public CellFormat(int horizontalAlignment, int verticalAlignment, Color foreground, Color background,
                      Font font, Border border) {

        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.foreground = foreground;
        this.background = background;
        this.font = font;
        this.border = border;
    }

    public CellFormat() {
        this(-1, -1, null, null, null, null);
    }

    public CellFormat withColors(Color foreground, Color background) {
        return new CellFormat(this.horizontalAlignment, this.verticalAlignment, foreground, background, this.font, this.border);
    }

    public CellFormat withFont(Font font) {
        return new CellFormat(this.horizontalAlignment, this.verticalAlignment, this.foreground, this.background, font, this.border);
    }

    public CellFormat withBorder(Border padding) {
        return new CellFormat(this.horizontalAlignment, this.verticalAlignment, this.foreground, this.background, this.font, padding);
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public void applyTo(Component component) {
        if (foreground != null) component.setForeground(foreground);
        if (background != null) component.setBackground(background);
        if (font != null) component.setFont(font);

        if (component instanceof JComponent) {
            JComponent jComponent = (JComponent) component;
            if (background != null) jComponent.setOpaque(true);
            if (border != null) jComponent.setBorder(border);

            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (horizontalAlignment >= 0) label.setHorizontalAlignment(horizontalAlignment);
                if (verticalAlignment >= 0) label.setVerticalAlignment(verticalAlignment);
            } else if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                if (background != null) button.setContentAreaFilled(true);
                if (horizontalAlignment >= 0) button.setHorizontalAlignment(horizontalAlignment);
                if (verticalAlignment >= 0) button.setVerticalAlignment(verticalAlignment);
            } else if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                if (horizontalAlignment >= 0) textField.setHorizontalAlignment(horizontalAlignment);
            }
        }
    }
}
