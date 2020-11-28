package org.addy.simpletable;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Mike
 */
public class SimpleTable extends javax.swing.JTable {

    public static final int DEFAULT_ROW_HEIGHT = 20;
    public static final Color DEFAULT_ALTERNATE_BACKGROUND = new Color(225, 255, 240);
    public static final Color DEFAULT_ROLLOVER_BACKGROUND = new Color(255, 255, 205);
    
    private Color alternateBackground;
    private Color rolloverBackground;
    private int rolloverRowIndex = -1;
    
    public SimpleTable() {
        super();
        initializeTable();
    }

    public SimpleTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        initializeTable();
    }

    public SimpleTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        initializeTable();
    }

    public SimpleTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        initializeTable();
    }

    public SimpleTable(TableModel model) {
        super(model);
        initializeTable();
    }

    public SimpleTable(TableModel tm, TableColumnModel cm) {
        super(tm, cm);
        initializeTable();
    }

    public SimpleTable(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
        super(tm, cm, sm);
        initializeTable();
    }

    public SimpleTable(String[] columnNames, Class[] columnClasses, List items, RowAdapter rowAdapter) {
        super(new SimpleTableModel(columnNames, columnClasses, items, rowAdapter));
        initializeTable();
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
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);
        if (rolloverBackground != null && row == rolloverRowIndex) {
            comp.setForeground(getForeground());
            comp.setBackground(rolloverBackground);
        }
        else if (!(alternateBackground == null || comp.getBackground().equals(getSelectionBackground()))) {
            if (row % 2 == 1) {
                comp.setBackground(alternateBackground);
            }
            else {
                comp.setBackground(getBackground());
            }
        }
        return comp;
    }

    private void initializeTable() {
        setRowHeight(DEFAULT_ROW_HEIGHT);
        setAlternateBackground(DEFAULT_ALTERNATE_BACKGROUND);
        setRolloverBackground(DEFAULT_ROLLOVER_BACKGROUND);
        setAutoCreateRowSorter(true);
        
        RolloverListener rl = new RolloverListener();
        addMouseMotionListener(rl);
        addMouseListener(rl);
    }
    
    private class RolloverListener extends MouseInputAdapter {

        @Override
        public void mouseExited(MouseEvent e) {
            rolloverRowIndex = -1;
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int row = rowAtPoint(e.getPoint());
            if( row != rolloverRowIndex ) {
                rolloverRowIndex = row;
                repaint();
            }
        }
    }

    public static void main(String... args) {
        JFrame frame = new JFrame("SimpleTable demo");
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SimpleTableModel model = new SimpleTableModel(
                new String[]{"Nom", "Prénom", "Sexe", "Age", "Adresse"},
                new Class[]{String.class, String.class, Character.TYPE, Integer.class, String.class},
                getTableData(),
                new ArrayRowAdapter());

        SimpleTable table = new SimpleTable(model);
        frame.getContentPane().add(new JScrollPane(table));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException |
               InstantiationException |
               IllegalAccessException |
               UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(System.err);
        }

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    private static List getTableData() {
        return Arrays.asList(
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
                new Object[]{"PENDA", "Laurence", 'F', 20, "Douala - Bonaberi"});
    }

}
