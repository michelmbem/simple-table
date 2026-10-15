package org.addy.simpletable.column.spec;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CellFormat {
    public static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(2, 4, 2, 4);

    public static final CellFormat DEFAULT = new CellFormat();
    public static final CellFormat FIRST_LINE_START = new CellFormat(SwingConstants.LEADING, SwingConstants.LEADING);
    public static final CellFormat PAGE_START = new CellFormat(SwingConstants.CENTER, SwingConstants.LEADING);
    public static final CellFormat FIRST_LINE_END = new CellFormat(SwingConstants.TRAILING, SwingConstants.LEADING);
    public static final CellFormat LINE_START = new CellFormat(SwingConstants.LEADING, SwingConstants.CENTER);
    public static final CellFormat CENTER = new CellFormat(SwingConstants.CENTER, SwingConstants.CENTER);
    public static final CellFormat LINE_END = new CellFormat(SwingConstants.TRAILING, SwingConstants.CENTER);
    public static final CellFormat LAST_LINE_START = new CellFormat(SwingConstants.LEADING, SwingConstants.TRAILING);
    public static final CellFormat PAGE_END = new CellFormat(SwingConstants.CENTER, SwingConstants.TRAILING);
    public static final CellFormat LAST_lINE_END = new CellFormat(SwingConstants.TRAILING, SwingConstants.TRAILING);

    private int horizontalAlignment;
    private int verticalAlignment;
    private Color foreground;
    private Color background;
    private Font font;
    private Border border;

    public CellFormat(int horizontalAlignment, int verticalAlignment,
                      Color foreground, Color background, Font font,
                      Border border) {

        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.foreground = foreground;
        this.background = background;
        this.font = font;
        this.border = border;
    }

    private CellFormat(int horizontalAlignment, int verticalAlignment) {
        this(horizontalAlignment, verticalAlignment, null, null, null, DEFAULT_BORDER);
    }

    private CellFormat() {
        this(-1, -1);
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

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public void applyTo(Component component, boolean isEditor) {
        if (foreground != null) component.setForeground(foreground);
        if (background != null) component.setBackground(background);
        if (font != null) component.setFont(font);

        if (component instanceof JComponent jComponent) {
            if (background != null) jComponent.setOpaque(true);
            if (!(border == null || isEditor)) jComponent.setBorder(border);

            if (component instanceof JLabel label) {
                if (horizontalAlignment >= 0) label.setHorizontalAlignment(horizontalAlignment);
                if (verticalAlignment >= 0) label.setVerticalAlignment(verticalAlignment);
            } else if (component instanceof AbstractButton button) {
                if (background != null) button.setContentAreaFilled(true);
                if (horizontalAlignment >= 0) button.setHorizontalAlignment(horizontalAlignment);
                if (verticalAlignment >= 0) button.setVerticalAlignment(verticalAlignment);
            } else if (component instanceof JTextField textField && horizontalAlignment >= 0) {
                textField.setHorizontalAlignment(horizontalAlignment);
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .horizontalAlignment(horizontalAlignment)
                .verticalAlignment(verticalAlignment)
                .foreground(foreground)
                .background(background)
                .font(font)
                .border(border);
    }

    public CellFormat withColors(Color foreground, Color background) {
        return toBuilder()
                .foreground(foreground)
                .background(background)
                .build();
    }

    public CellFormat withFont(Font font) {
        return toBuilder().font(font).build();
    }

    public CellFormat withBorder(Border border) {
        return toBuilder().border(border).build();
    }

    public static class Builder {
        private final CellFormat cellFormat = new CellFormat();

        public Builder horizontalAlignment(int arg) {
            cellFormat.setHorizontalAlignment(arg);
            return this;
        }

        public Builder verticalAlignment(int arg) {
            cellFormat.setVerticalAlignment(arg);
            return this;
        }

        public Builder foreground(Color arg) {
            cellFormat.setForeground(arg);
            return this;
        }

        public Builder background(Color arg) {
            cellFormat.setBackground(arg);
            return this;
        }

        public Builder font(Font arg) {
            cellFormat.setFont(arg);
            return this;
        }

        public Builder border(Border arg) {
            cellFormat.setBorder(arg);
            return this;
        }

        public CellFormat build() {
            return cellFormat;
        }
    }
}
