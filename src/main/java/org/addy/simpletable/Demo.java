package org.addy.simpletable;

import org.addy.simpletable.column.adapter.ELColumnAdapter;
import org.addy.simpletable.column.spec.ColumnSpec;
import org.addy.simpletable.column.validator.CellValidators;
import org.addy.simpletable.event.TableCellActionEvent;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.Range;
import org.addy.simpletable.row.adapter.ListRowAdapter;
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

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static org.addy.simpletable.column.spec.CellFormat.CENTER;
import static org.addy.simpletable.column.spec.CellFormat.LINE_END;
import static org.addy.simpletable.column.spec.ColumnType.*;

public final class Demo {
    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        var tableModel = SimpleTableModel.builder()
                .itemSource(getTableData())
                .addColumns("$.lastName", "$.firstName", "$.gender", "$.dateOfBirth", "$.height",
                        "$.weight * 2.20462262", "$.married", "$.photo")
                .column(1).validator(CellValidators.notEmpty()).end()
                .rowAdapter(ListRowAdapter::new)
                .columnAdapter(ELColumnAdapter::new)
                .build();

        var table = new SimpleTable(tableModel);
        table.setRowHeight(48);
        table.setColumnSpecs(ColumnSpec.builder()
                .columnType(HYPERLINK).headerText("Nom").width(100)
                    .extraData((TableCellActionListener) Demo::buttonClicked).next()
                .columnType(TEXT).headerText("Prénom").width(100).next()
                .columnType(COMBOBOX).headerText("Sexe").width(75)
                    .resizable(false).cellFormat(CENTER).extraData(Gender.values()).next()
                .columnType(DATETIME).headerText("Né(e) le").width(125).extraData("d").next()
                .columnType(NUMBER).headerText("Taille").width(75).resizable(false)
                    .headerFormat(LINE_END).cellFormat(LINE_END).extraData("#0.00'm'").next()
                .columnType(PROGRESS).headerText("Poids (lbs)").width(100)
                    .extraData(new Range(0, 220)).next()
                .columnType(CHECKBOX).headerText("Marié(e)?").width(75)
                    .resizable(false).headerFormat(CENTER).cellFormat(CENTER).next()
                .columnType(IMAGE).headerText("Photo").width(75)
                    .resizable(false).sortable(false)
                .collect());

        var scrollPane = new JScrollPane(table);
        table.createRowHeaders(scrollPane, LINENUMBER, "#", 40, LINE_END);

        var frame = new JFrame("SimpleTable Demo");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setContentPane(scrollPane);
        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static List<?> getTableData() {
        List<Person> personList = new ArrayList<>();
        ClassLoader classLoader = Demo.class.getClassLoader();

        try (var reader = new BufferedReader(new InputStreamReader(
                classLoader.getResourceAsStream("demodata.txt")))) {

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
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return personList;
    }

    private static Image loadImage(ClassLoader classLoader, String filename) {
        URL imageURL = classLoader.getResource("images/portraits/" + filename);
        return new ImageIcon(imageURL).getImage();
    }

    private static void buttonClicked(TableCellActionEvent e) {
        String newName = JOptionPane.showInputDialog(
                e.getTable().getTopLevelAncestor(),
                "New name",
                e.getValue());

        if (!StringUtil.isBlank(newName))
            e.getTable().setValueAt(newName, e.getRow(), e.getColumn());
    }


    public enum Gender {
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
            if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now()))
                throw new IllegalArgumentException("The date of birth cannot be after the current date");

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
