package org.addy.simpletable;

import org.addy.simpletable.column.definition.CellFormat;
import org.addy.simpletable.column.definition.ColumnDefinition;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 * @author Mike
 */
public class SimpleTable extends javax.swing.JTable {
    public static final int DEFAULT_ROW_HEIGHT = 20;
    public static final Color DEFAULT_ALTERNATE_BACKGROUND = new Color(225, 240, 255);
    public static final Color DEFAULT_ROLLOVER_BACKGROUND = new Color(255, 255, 240);
    public static final Insets DEFAULT_CELL_INSETS = new Insets(2, 4, 2, 4);

    private Color alternateBackground;
    private Color rolloverBackground;
    private Insets cellPadding;
    private int rolloverRowIndex = -1;
    private ColumnDefinition[] columnDefinitions;

    public SimpleTable() {
        super();
        initializeTable();
        generateColumnDefinitions();
    }

    public SimpleTable(TableModel model) {
        super(model);
        initializeTable();
        generateColumnDefinitions();
    }

    public SimpleTable(TableModel tm, TableColumnModel cm) {
        super(tm, cm);
        initializeTable();
        generateColumnDefinitions();
    }

    public SimpleTable(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
        super(tm, cm, sm);
        initializeTable();
        generateColumnDefinitions();
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

    public Insets getCellPadding() {
        return cellPadding;
    }

    public void setCellPadding(Insets cellPadding) {
        this.cellPadding = cellPadding;
        repaint();
    }
    
    public ColumnDefinition[] getColumnDefinitions() {
        return columnDefinitions;
    }
    
    public void setColumnDefinitions(ColumnDefinition... columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
        applyColumnDefinitions();
    }
    
    public ColumnDefinition getColumnDefinition(int index) {
        return columnDefinitions[index];
    }
    
    public void setColumnDefinition(int index, ColumnDefinition column) {
        columnDefinitions[index] = column;
        columnDefinitions[index].applyTo(getColumnModel().getColumn(index), this, index);
    }

    public void applyColumnDefinitions() {
        for (int i = 0; i < columnDefinitions.length; ++i) {
            columnDefinitions[i].applyTo(getColumnModel().getColumn(i), this, i);
        }
    }
    
    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        generateColumnDefinitions();
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, row, column);
        component.setBorder(BorderFactory.createEmptyBorder(cellPadding.top, cellPadding.left, cellPadding.bottom, cellPadding.right));

        if (rolloverBackground != null && row == rolloverRowIndex) {
            component.setForeground(getForeground());
            component.setBackground(rolloverBackground);
        } else if (!(alternateBackground == null || component.getBackground().equals(getSelectionBackground()))) {
            CellFormat cellFormat = columnDefinitions[column].getCellFormat();

            if (cellFormat.getBackground() != null) {
                component.setBackground(cellFormat.getBackground());
            } else {
                component.setBackground(row % 2 == 1 ? alternateBackground : getBackground());
            }
        }

        return component;
    }

    protected void initializeTable() {
        setRowHeight(DEFAULT_ROW_HEIGHT);
        setAlternateBackground(DEFAULT_ALTERNATE_BACKGROUND);
        setRolloverBackground(DEFAULT_ROLLOVER_BACKGROUND);
        setCellPadding(DEFAULT_CELL_INSETS);
        setAutoCreateRowSorter(true);
        createRolloverListener();
    }

    protected void generateColumnDefinitions() {
        columnDefinitions = new ColumnDefinition[getColumnCount()];

        for (int i = 0; i < columnDefinitions.length; ++i) {
            columnDefinitions[i] = new ColumnDefinition();
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
