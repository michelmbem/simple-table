package org.addy.simpletable.column.converter;

import org.addy.swing.ListItem;

import java.util.List;
import java.util.Objects;

public class ListItemCellConverter implements CellConverter {
    private final List<ListItem<?>> listItems;

    public ListItemCellConverter(List<ListItem<?>> listItems) {
        this.listItems = Objects.requireNonNull(listItems);
    }

    public ListItemCellConverter(ListItem<?>... listItems) {
        this.listItems = List.of(listItems);
    }

    @Override
    public Object model2view(Object modelValue, Object rowItem) {
        return listItems.stream()
                .filter(listItem -> listItem.value().equals(modelValue))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Object view2model(Object editorValue, Object rowItem) {
        var listItem = (ListItem<?>) editorValue;
        return listItem != null ? listItem.value() : null;
    }
}
