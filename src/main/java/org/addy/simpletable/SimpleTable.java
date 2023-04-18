package org.addy.simpletable;

import org.addy.simpletable.column.config.CellFormat;
import org.addy.simpletable.column.config.ColumnConfig;
import org.addy.swing.KnownColor;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * An easily configurable JTable.<br>
 * Manages alternated row backgrounds as well as row highlighting on mouse hover and automatic sorting.<br>
 * Automatically generates cell renderers and editors for columns based on the supplied given column definitions.<br>
 *
 * @author Mike
 */
public class SimpleTable extends JTable {
    private ColumnConfig[] columnConfigs;
    private Color alternateBackground;
    private Color rolloverBackground;
    private int rolloverRowIndex = -1;

    public SimpleTable() {
        super();
        initializeTable();
        generateColumnConfigs();
    }

    public SimpleTable(TableModel model) {
        super(model);
        initializeTable();
        generateColumnConfigs();
    }

    public SimpleTable(TableModel tm, TableColumnModel cm) {
        super(tm, cm);
        initializeTable();
        generateColumnConfigs();
    }

    public SimpleTable(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
        super(tm, cm, sm);
        initializeTable();
        generateColumnConfigs();
    }

    public ColumnConfig[] getColumnConfigs() {
        return columnConfigs;
    }

    public void setColumnConfigs(ColumnConfig... columnConfigs) {
        this.columnConfigs = Objects.requireNonNull(columnConfigs);
        applyColumnConfigs();
    }

    public ColumnConfig getColumnConfig(int index) {
        return columnConfigs[index];
    }

    public void setColumnConfig(int index, ColumnConfig columnConfig) {
        columnConfigs[index] = Objects.requireNonNull(columnConfig);
        columnConfig.applyTo(getColumnModel().getColumn(index), this, index);
    }

    public void applyColumnConfigs() {
        for (int i = 0; i < columnConfigs.length; ++i) {
            columnConfigs[i].applyTo(getColumnModel().getColumn(i), this, i);
        }
    }

    public Color getAlternateBackground() {
        return alternateBackground;
    }

    public void setAlternateBackground(Color alternateBackground) {
        this.alternateBackground = alternateBackground;
        repaint();
    }

    public Color getRolloverBackground() {
        return rolloverBackground;
    }

    public void setRolloverBackground(Color rolloverBackground) {
        this.rolloverBackground = rolloverBackground;
    }
    
    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        generateColumnConfigs();
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, row, column);
        columnConfigs[column].getCellFormat().applyTo(component, false);

        if (!component.getBackground().equals(getSelectionBackground())) {
            if (rolloverBackground != null && row == rolloverRowIndex) {
                component.setForeground(getForeground());
                component.setBackground(rolloverBackground);
            } else if (alternateBackground != null) {
                CellFormat cellFormat = columnConfigs[column].getCellFormat();

                if (cellFormat.getBackground() != null) {
                    component.setBackground(cellFormat.getBackground());
                } else {
                    component.setBackground(row % 2 == 1 ? alternateBackground : getBackground());
                }
            }
        }

        return component;
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column) {
        JComponent component = (JComponent) super.prepareEditor(editor, row, column);
        columnConfigs[column].getCellFormat().applyTo(component, true);
        return component;
    }

    protected void initializeTable() {
        setRowHeight(20);
        setAlternateBackground(KnownColor.WHITE_BLUE);
        setRolloverBackground(KnownColor.IVORY);
        setAutoCreateRowSorter(true);
        createRolloverListener();
    }

    protected void generateColumnConfigs() {
        columnConfigs = new ColumnConfig[getColumnCount()];

        for (int i = 0; i < columnConfigs.length; ++i) {
            columnConfigs[i] = new ColumnConfig();
        }
    }

    protected void createRolloverListener() {
        RolloverListener rolloverListener = new RolloverListener();
        addMouseMotionListener(rolloverListener);
        addMouseListener(rolloverListener);
    }


    protected class RolloverListener extends MouseInputAdapter {
        @Override
        public void mouseExited(MouseEvent e) {
            rolloverRowIndex = -1;
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int row = rowAtPoint(e.getPoint());
            if (row != rolloverRowIndex) {
                rolloverRowIndex = row;
                repaint();
            }
        }
    }
}
