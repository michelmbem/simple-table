package org.addy.simpletable.column.definition;

import org.addy.simpletable.column.editor.TableCellEditors;
import org.addy.simpletable.column.renderer.TableCellRenderers;

import javax.swing.table.TableColumn;

public class ColumnDefinition {
    private ColumnType columnType = ColumnType.TEXT;
    private String headerText = null;
    private int width = -1;
    private boolean resizable = true;
    private int horizontalAlignment = -1;
    private int verticalAlignment = -1;
    private Object extraData = null;

    public ColumnDefinition() {
    }

    public ColumnDefinition(ColumnType columnType, String headerText) {
        this.columnType = columnType;
        this.headerText = headerText;
    }

    public ColumnDefinition(ColumnType columnType, String headerText, int width) {
        this.columnType = columnType;
        this.headerText = headerText;
        this.width = width;
    }

    public ColumnDefinition(ColumnType columnType, String headerText, int width, boolean resizable,
                            int horizontalAlignment, int verticalAlignment, Object extraData) {
        this.columnType = columnType;
        this.headerText = headerText;
        this.width = width;
        this.resizable = resizable;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.extraData = extraData;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
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

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
    
    public void applyTo(TableColumn column) {
        if (headerText != null)
            column.setHeaderValue(headerText);

        if (width >= 0)
            column.setPreferredWidth(width);

        if (resizable) {
            column.setMinWidth(0);
            column.setMaxWidth(Integer.MAX_VALUE);
        } else {
            column.setMinWidth(column.getPreferredWidth());
            column.setMaxWidth(column.getPreferredWidth());
        }

        column.setCellRenderer(TableCellRenderers.from(columnType, horizontalAlignment, verticalAlignment, extraData));
        column.setCellEditor(TableCellEditors.from(columnType, horizontalAlignment, verticalAlignment, extraData));
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private final ColumnDefinition columnDefinition = new ColumnDefinition();

        public Builder columnType(ColumnType arg) {
            columnDefinition.setColumnType(arg);
            return this;
        }

        public Builder headerText(String arg) {
            columnDefinition.setHeaderText(arg);
            return this;
        }

        public Builder width(int arg) {
            columnDefinition.setWidth(arg);
            return this;
        }

        public Builder resizable(boolean arg) {
            columnDefinition.setResizable(arg);
            return this;
        }

        public Builder horizontalAlignment(int arg) {
            columnDefinition.setHorizontalAlignment(arg);
            return this;
        }

        public Builder verticalAlignment(int arg) {
            columnDefinition.setVerticalAlignment(arg);
            return this;
        }

        public Builder alignment(int arg1, int arg2) {
            columnDefinition.setHorizontalAlignment(arg1);
            columnDefinition.setVerticalAlignment(arg2);
            return this;
        }

        public Builder extraData(Object arg) {
            columnDefinition.setExtraData(arg);
            return this;
        }

        public ColumnDefinition build() {
            return columnDefinition;
        }
    }
}
