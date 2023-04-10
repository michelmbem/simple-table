package org.addy.simpletable;

import org.addy.simpletable.column.adapter.ArrayColumnAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.addy.simpletable.column.config.ColumnConfig;

/**
 *
 * @author Mike
 */
public class SimpleTable extends javax.swing.JTable {

    public static final int DEFAULT_ROW_HEIGHT = 20;
    public static final Color DEFAULT_ALTERNATE_BACKGROUND = new Color(225, 240, 255);
    public static final Color DEFAULT_ROLLOVER_BACKGROUND = new Color(255, 255, 240);
    public static final Insets DEFAULT_CELL_INSETS = new Insets(2, 2, 2, 2);

    private Color alternateBackground;
    private Color rolloverBackground;
    private Insets cellInsets;
    private int rolloverRowIndex = -1;
    private ColumnConfig[] columns;

    public SimpleTable() {
        super();
        initializeTable();
        createComlumns();
    }

    public SimpleTable(TableModel model) {
        super(model);
        initializeTable();
        createComlumns();
    }

    public SimpleTable(TableModel tm, TableColumnModel cm) {
        super(tm, cm);
        initializeTable();
        createComlumns();
    }

    public SimpleTable(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
        super(tm, cm, sm);
        initializeTable();
        createComlumns();
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

    public Insets getCellInsets() {
        return cellInsets;
    }

    public void setCellInsets(Insets cellInsets) {
        this.cellInsets = cellInsets;
        repaint();
    }
    
    public ColumnConfig[] getColumns() {
        return columns;
    }
    
    public void setColumns(ColumnConfig... columns) {
        for (int i = 0; i < columns.length; ++i) {
            if (i > this.columns.length) break;
            this.columns[i].copyFrom(columns[i]);
            this.columns[i].applyTo(getColumnModel().getColumn(i));
        }
    }
    
    public ColumnConfig getColumnAt(int index) {
        return columns[index];
    }
    
    public void setColumnAt(int index, ColumnConfig column) {
        columns[index].copyFrom(column);
        columns[index].applyTo(getColumnModel().getColumn(index));
    }
    
    public void applyColumnConfiguration() {
        for (int i = 0; i < columns.length; ++i) {
            columns[i].applyTo(getColumnModel().getColumn(i));
        }
    }
    
    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        createComlumns();
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, row, column);
        component.setBorder(BorderFactory.createEmptyBorder(getCellInsets().top, getCellInsets().left, getCellInsets().bottom, getCellInsets().right));

        if (rolloverBackground != null && row == rolloverRowIndex) {
            component.setForeground(getForeground());
            component.setBackground(rolloverBackground);
        } else if (!(alternateBackground == null || component.getBackground().equals(getSelectionBackground()))) {
            component.setBackground(row % 2 == 1 ? alternateBackground : getBackground());
        }

        return component;
    }

    protected void initializeTable() {
        setRowHeight(DEFAULT_ROW_HEIGHT);
        setAlternateBackground(DEFAULT_ALTERNATE_BACKGROUND);
        setRolloverBackground(DEFAULT_ROLLOVER_BACKGROUND);
        setCellInsets(DEFAULT_CELL_INSETS);
        setAutoCreateRowSorter(true);
        createRolloverListener();
    }

    protected void createComlumns() {
        columns = new ColumnConfig[getColumnCount()];

        for (int i = 0; i < columns.length; ++i) {
            columns[i] = new ColumnConfig();
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

    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFrame frame = new JFrame("SimpleTable demo");
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        SimpleTableModel model = new SimpleTableModel(getTableData(), "Nom", "Prénom", "Sexe", "Age", "Adresse");
        SimpleTable table = new SimpleTable(model);
        table.setColumns(
                new ColumnConfig(null, 100),
                new ColumnConfig(null, 100),
                new ColumnConfig(null, 75, SwingConstants.CENTER, -1),
                new ColumnConfig(null, 75, SwingConstants.TRAILING, -1),
                new ColumnConfig(null, 150));
        frame.getContentPane().add(new JScrollPane(table));

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    private static Object[][] getTableData() {
        return new Object[][] {
                new Object[]{"MIMB", "Martin Camus", 'M', 32, "Douala - Bepanda"},
                new Object[]{"OBAMA", "Ernest", 'M', 28, "Yaoundé - Soa"},
                new Object[]{"MBEM", "Paul Michel", 'M', 35, "Edea"},
                new Object[]{"NGOCK", "Pierre", 'M', 41, "Douala - Bonapriso"},
                new Object[]{"EDOUDOUA", "Non Glacé", 'M', 36, "Bertoua"},
                new Object[]{"KAMDEM", "Dieunedort", 'M', 33, "Dschang"},
                new Object[]{"ESSOMBA", "Julienne", 'F', 28, "Ebolowa"},
                new Object[]{"AOUDOU", "Ibrahim", 'M', 30, "Maroua"},
                new Object[]{"EDZOA", "Zacharie", 'M', 22, "Yaoundé - Nlongkack"},
                new Object[]{"MOUBITANG", "Françoise", 'F', 21, "Bafia"},
                new Object[]{"FOTSO", "Marie Claire", 'F', 19, "Bafoussam"},
                new Object[]{"TALOM", "Appolinaire", 'M', 37, "Douala - Akwa"},
                new Object[]{"MOHAMADOU", "Ali", 'M', 43, "Ngaoundéré"},
                new Object[]{"BIYIHA", "Jeannot", 'M', 24, "Yaoundé - Essos"},
                new Object[]{"DOH", "Eric Rostand", 'M', 18, "Bertoua"},
                new Object[]{"NKOLO", "Stéphanie", 'F', 25, "Sangmelima"},
                new Object[]{"NJOYAH", "Maïmouna", 'F', 27, "Foumban"},
                new Object[]{"AKEM", "Donaldson", 'M', 38, "Bamenda"},
                new Object[]{"ENOW", "Sunday", 'M', 22, "Mamfe"},
                new Object[]{"PENDA", "Laurence", 'F', 20, "Douala - Bonaberi"}};
    }

}
