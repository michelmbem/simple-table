package org.addy.simpletable.column.converter;

import org.addy.simpletable.model.ListItem;

import java.util.*;

public class ListItemCellConverter implements CellConverter {
    private final List<ListItem<?>> listItems;

    public ListItemCellConverter(Collection<? extends ListItem<?>> listItems) {
        this.listItems = new ArrayList<>(Objects.requireNonNull(listItems));
    }

    public ListItemCellConverter(ListItem<?>... listItems) {
        this.listItems = Arrays.asList(Objects.requireNonNull(listItems));
    }

    @Override
    public Object model2view(Object modelValue, Object rowItem) {
        return listItems.stream()
                .filter(listItem -> listItem.getValue().equals(modelValue))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Object view2model(Object editorValue, Object rowItem) {
        var listItem = (ListItem<?>) editorValue;
        return listItem != null ? listItem.getValue() : null;
    }
}
