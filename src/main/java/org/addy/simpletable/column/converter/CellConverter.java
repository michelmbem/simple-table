package org.addy.simpletable.column.converter;

public interface CellConverter {
    Object model2view(Object modelValue, Object rowItem);
    Object view2model(Object editorValue, Object rowItem);
}
