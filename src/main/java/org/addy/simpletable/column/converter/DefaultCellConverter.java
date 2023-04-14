package org.addy.simpletable.column.converter;

public class DefaultCellConverter implements CellConverter {
    @Override
    public Object model2view(Object modelValue, Object rowItem) {
        return modelValue;
    }

    @Override
    public Object view2model(Object editorValue, Object rowItem) {
        return editorValue;
    }
}
