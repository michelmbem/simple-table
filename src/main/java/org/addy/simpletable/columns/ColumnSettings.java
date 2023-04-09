package org.addy.simpletable.columns;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class ColumnSettings {
    
    private String headerText = null;
    private int width = -1;
    private int horizontalAlignment = -1;
    private int verticalAlignment = -1;

    public ColumnSettings() {
    }

    public ColumnSettings(String headerText) {
        this.headerText = headerText;
    }

    public ColumnSettings(String headerText, int width) {
        this.headerText = headerText;
        this.width = width;
    }

    public ColumnSettings(String headerText, int width, int horizontalAlignment, int verticalAlignment) {
        this.headerText = headerText;
        this.width = width;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
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

    public void copyFrom(ColumnSettings other) {
        this.headerText = other.headerText;
        this.width = other.width;
        this.horizontalAlignment = other.horizontalAlignment;
        this.verticalAlignment = other.verticalAlignment;
    }
    
    public void applyTo(TableColumn column) {
        if (headerText != null) {   // if headerText is null let the table model supply the value
            column.setHeaderValue(headerText);
        }

        if (width >= 0) {   // if width is negative let the table model supply the value
            column.setPreferredWidth(width);
        }

        customizeCellRenderer(column);
        customizeCellEditor(column);
    }

    private void customizeCellRenderer(TableColumn column) {
        TableCellRenderer cellRenderer = column.getCellRenderer();

        if (cellRenderer == null) {
            cellRenderer = new DefaultTableCellRenderer();
            column.setCellRenderer(cellRenderer);
        }

        if (cellRenderer instanceof JLabel) {
            JLabel label = (JLabel) cellRenderer;

            if (horizontalAlignment >= 0) {   // if horizontalAlignment is negative let the table guess the value
                label.setHorizontalAlignment(horizontalAlignment);
            }

            if (verticalAlignment >= 0) {   // if verticalAlignment is negative let the table guess the value
                label.setVerticalAlignment(verticalAlignment);
            }
        }
    }

    private void customizeCellEditor(TableColumn column) {
        TableCellEditor cellEditor = column.getCellEditor();

        if (cellEditor == null) {
            cellEditor = new DefaultCellEditor(new JTextField());
            column.setCellEditor(cellEditor);
        }

        if (cellEditor instanceof DefaultCellEditor) {
            DefaultCellEditor defaultCellEditor = (DefaultCellEditor) cellEditor;

            if (defaultCellEditor.getComponent() instanceof JTextField) {
                JTextField textField = (JTextField) defaultCellEditor.getComponent();

                if (horizontalAlignment >= 0) {   // if horizontalAlignment is negative let the table guess the value
                    textField.setHorizontalAlignment(horizontalAlignment);
                }
            }
        }
    }
    
}
