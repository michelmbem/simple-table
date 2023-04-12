package org.addy.simpletable;

import org.addy.simpletable.column.adapter.ELColumnAdapter;
import org.addy.simpletable.column.definition.ColumnDefinition;
import org.addy.simpletable.column.definition.ColumnType;
import org.addy.simpletable.event.TableCellActionEvent;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.Range;
import org.addy.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Demo {
    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] tableColumns = {"$.lastName", "$.firstName", "$.gender", "$.dateOfBirth", "$.height", "$.weight * 2.20462262", "$.married", "$.photo"};
        SimpleTableModel tableModel = new SimpleTableModel(getTableData(), tableColumns, new ELColumnAdapter());
        tableModel.getColumns()[7].setEditable(false);

        SimpleTable table = new SimpleTable(tableModel);
        table.setRowHeight(28);
        table.setColumnDefinitions(
                new ColumnDefinition(ColumnType.HYPERLINK,"Nom", 100, true, -1, -1, (TableCellActionListener) Demo::buttonClicked),
                new ColumnDefinition(ColumnType.TEXT,"Prénom", 100),
                new ColumnDefinition(ColumnType.COMBOBOX, "Sexe", 70, false, SwingConstants.CENTER, -1, Gender.values()),
                new ColumnDefinition(ColumnType.DATETIME, "Né(e) le", 100, true, SwingConstants.LEADING, -1, "d"),
                new ColumnDefinition(ColumnType.NUMBER,"Taille", 80, false, SwingConstants.TRAILING, -1, "#0.00'm'"),
                new ColumnDefinition(ColumnType.PROGRESS,"Poids (lbs)", 100, true, -1, -1, new Range(0, 220)),
                new ColumnDefinition(ColumnType.CHECKBOX,"Marié(e)?", 80, false, SwingConstants.CENTER, -1, null),
                new ColumnDefinition(ColumnType.IMAGE,"Photo", 70, false, SwingConstants.CENTER, -1, null));

        JFrame frame = new JFrame("SimpleTable demo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setContentPane(new JScrollPane(table));
        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static List<?> getTableData() {
        List<Person> personList = new ArrayList<>();
        ClassLoader classLoader = Demo.class.getClassLoader();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(classLoader.getResourceAsStream("demodata.txt"))))) {

            personList = reader.lines()
                    .map(line -> line.split(","))
                    .map(fields -> new Person(
                            fields[0],
                            fields[1],
                            Gender.valueOf(fields[2]),
                            LocalDate.parse(fields[3]),
                            Float.parseFloat(fields[4]),
                            Short.parseShort(fields[5]),
                            Boolean.parseBoolean(fields[6]),
                            loadImage(classLoader, fields[7])))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return personList;
    }

    private static Image loadImage(ClassLoader classLoader, String filename) {
        URL imageURL = classLoader.getResource("images/portraits/" + filename);
        return new ImageIcon(Objects.requireNonNull(imageURL)).getImage();
    }

    private static void buttonClicked(TableCellActionEvent e) {
        String newName = JOptionPane.showInputDialog(e.getTable().getTopLevelAncestor(), "New name", e.getValue());
        if (!StringUtil.isBlank(newName)) e.getTable().setValueAt(newName, e.getRow(), e.getColumn());
    }


    public static enum Gender {
        M,
        F,
    }

    public static class Person {
        private String firstName;
        private String lastName;
        private Gender gender;
        private LocalDate dateOfBirth;
        private float height; // Height in meters
        private short weight; // Weight in kilograms
        private boolean married;
        private Image photo;

        public Person() {}

        public Person(String firstName, String lastName, Gender gender, LocalDate dateOfBirth,
                      float height, short weight, boolean married, Image photo) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
            this.height = height;
            this.weight = weight;
            this.married = married;
            this.photo = photo;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = height;
        }

        public short getWeight() {
            return weight;
        }

        public void setWeight(short weight) {
            this.weight = weight;
        }

        public boolean isMarried() {
            return married;
        }

        public void setMarried(boolean married) {
            this.married = married;
        }

        public Image getPhoto() {
            return photo;
        }

        public void setPhoto(Image photo) {
            this.photo = photo;
        }
    }
}