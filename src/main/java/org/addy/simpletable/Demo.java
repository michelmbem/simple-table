package org.addy.simpletable;

import org.addy.simpletable.column.definition.ColumnDefinition;
import org.addy.simpletable.column.definition.ColumnType;
import org.addy.simpletable.event.TableCellActionEvent;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.util.Range;
import org.addy.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Demo {

    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SimpleTableModel model = new SimpleTableModel(getTableData(), "lastName", "firstName", "gender", "dateOfBirth", "height", "weight", "married", "photo");
        SimpleTable table = new SimpleTable(model);
        table.setRowHeight(28);
        table.setColumnDefinitions(
                new ColumnDefinition(ColumnType.BUTTON,"Nom", 100, true, -1, -1, (TableCellActionListener) Demo::buttonClicked),
                new ColumnDefinition(ColumnType.TEXT,"Prénom", 100),
                new ColumnDefinition(ColumnType.COMBOBOX, "Sexe", 70, false, SwingConstants.CENTER, -1, Gender.values()),
                new ColumnDefinition(ColumnType.DATETIME, "Né(e) le", 100, true, SwingConstants.LEADING, -1, "d"),
                new ColumnDefinition(ColumnType.NUMBER,"Taille", 80, false, SwingConstants.TRAILING, -1, "#0.00'm'"),
                new ColumnDefinition(ColumnType.PROGRESS,"Poids", 100, true, -1, -1, new Range(0, 150)),
                new ColumnDefinition(ColumnType.CHECKBOX,"Marié(e)?", 80, false, SwingConstants.CENTER, -1, null),
                new ColumnDefinition(ColumnType.ICON,"Photo", 70, false, SwingConstants.CENTER, -1, null));

        JFrame frame = new JFrame("SimpleTable demo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(table));
        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    private static List<?> getTableData() {
        List<Person> personList = new ArrayList<>();
        ClassLoader classLoader = Demo.class.getClassLoader();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(classLoader.getResourceAsStream("demodata.txt"))))) {

            String line;
            String[] fields;
            Person p;

            while ((line = br.readLine()) != null) {
                fields = line.split(",");
                p = new Person(
                        fields[0],
                        fields[1],
                        Gender.valueOf(fields[2]),
                        LocalDate.parse(fields[3]),
                        Float.parseFloat(fields[4]),
                        Short.parseShort(fields[5]),
                        Boolean.parseBoolean(fields[6]),
                        loadIcon(classLoader, fields[7]));
                personList.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return personList;
    }

    private static Icon loadIcon(ClassLoader classLoader, String filename) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/portraits/" + filename)));
        return new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
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
        private Icon photo;

        public Person() {}

        public Person(String firstName, String lastName, Gender gender, LocalDate dateOfBirth,
                      float height, short weight, boolean married, Icon photo) {
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

        public Icon getPhoto() {
            return photo;
        }

        public void setPhoto(Icon photo) {
            this.photo = photo;
        }
    }
}
