package org.addy.simpletable;

import org.addy.simpletable.column.adapter.BeanColumnAdapter;
import org.addy.simpletable.row.adapter.ListRowAdapter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleTableModelTest {

    @Test
    void builderWorks() {
        var tableModel = SimpleTableModel.builder()
                .itemSource(TestItem.all())
                .editable(false)
                .addColumn("id").type(int.class).end()
                .addColumn("name").type(String.class).end()
                .addColumn("checked", boolean.class).editable(false).end()
                .generateAdapters()
                .build();

        // Validating the structure
        assertThat(tableModel).isInstanceOf(SimpleTableModel.class);
        assertThat(tableModel.getItemSource()).isInstanceOf(List.class);
        assertThat((List<?>) tableModel.getItemSource()).hasSize(5);
        assertThat(tableModel.getRowAdapter()).isInstanceOf(ListRowAdapter.class);
        assertThat(tableModel.getColumnAdapter()).isInstanceOf(BeanColumnAdapter.class);
        assertThat(tableModel.isEditable()).isFalse();
        assertThat(tableModel.getColumns()).hasSize(3);

        assertThat(tableModel.getColumns()[0].getName()).isEqualTo("id");
        assertThat(tableModel.getColumns()[0].getType()).isEqualTo(int.class);
        assertThat(tableModel.getColumns()[0].isEditable()).isTrue();
        assertThat(tableModel.getColumns()[0].getConverter()).isNull();
        assertThat(tableModel.getColumns()[0].getValidator()).isNull();

        assertThat(tableModel.getColumns()[1].getName()).isEqualTo("name");
        assertThat(tableModel.getColumns()[1].getType()).isEqualTo(String.class);
        assertThat(tableModel.getColumns()[1].isEditable()).isTrue();
        assertThat(tableModel.getColumns()[1].getConverter()).isNull();
        assertThat(tableModel.getColumns()[1].getValidator()).isNull();

        assertThat(tableModel.getColumns()[2].getName()).isEqualTo("checked");
        assertThat(tableModel.getColumns()[2].getType()).isEqualTo(boolean.class);
        assertThat(tableModel.getColumns()[2].isEditable()).isFalse();
        assertThat(tableModel.getColumns()[2].getConverter()).isNull();
        assertThat(tableModel.getColumns()[2].getValidator()).isNull();

        // Validating the interface
        assertThat(tableModel.getRowCount()).isEqualTo(5);
        assertThat(tableModel.getColumnCount()).isEqualTo(3);
        assertThat(tableModel.getColumnName(0)).isEqualTo("id");
        assertThat(tableModel.getColumnClass(0)).isEqualTo(int.class);
        assertThat(tableModel.getColumnName(1)).isEqualTo("name");
        assertThat(tableModel.getColumnClass(1)).isEqualTo(String.class);
        assertThat(tableModel.getColumnName(2)).isEqualTo("checked");
        assertThat(tableModel.getColumnClass(2)).isEqualTo(boolean.class);
        assertThat(tableModel.isCellEditable(0, 0)).isFalse();
        assertThat(tableModel.isCellEditable(4, 2)).isFalse();
        assertThat(tableModel.getValueAt(0, 0)).isEqualTo(1);
        assertThat(tableModel.getValueAt(2, 1)).isEqualTo("Three");
        assertThat(tableModel.getValueAt(4, 2)).isEqualTo(true);
    }

    public record TestItem(int id, String name, boolean checked) {
        public static List<TestItem> all() {
            return List.of(
                    new TestItem(1, "One", true),
                    new TestItem(2, "Two", false),
                    new TestItem(3, "Three", true),
                    new TestItem(4, "Four", false),
                    new TestItem(5, "Five", true));
        }
    }
}