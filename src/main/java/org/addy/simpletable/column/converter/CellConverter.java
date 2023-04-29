package org.addy.simpletable.column.converter;

public interface CellConverter {
    default Object model2view(Object modelValue, Object rowItem) {
        return modelValue;
    }

    default Object view2model(Object editorValue, Object rowItem) {
        return editorValue;
    }
}
