package org.addy.simpletable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Mike
 */
public class BeanRowAdapter extends RowAdapterWithColumnNames {
    
    private Class itemClass;
    private Field[] fields;
    private Method[] getters;
    private Method[] setters;
    private boolean[] columnLookedUp;
    
    public BeanRowAdapter(String... columnNames) {
        super(columnNames);
        initColumnsLookUp();
    }

    @Override
    public void setColumnNames(String[] columnNames) {
        super.setColumnNames(columnNames);
        initColumnsLookUp();
    }

    @Override
    public Object getValueAt(Object item, int columnIndex, Class columnClass) {
        if (!columnLookedUp[columnIndex]) {
            lookUpColumnAt(item, columnIndex, columnClass);
        }
        
        if (fields[columnIndex] != null) {
            try {
                return fields[columnIndex].get(item);
            }
            catch (IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
        }
        
        if (getters[columnIndex] != null) {
            try {
                return getters[columnIndex].invoke(item);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return null;
            }
        }
        
        return null;
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Class columnClass, Object value) {
        if (!columnLookedUp[columnIndex]) {
            lookUpColumnAt(item, columnIndex, columnClass);
        }
        
        if (fields[columnIndex] != null) {
            try {
                fields[columnIndex].set(item, value);
            }
            catch (IllegalArgumentException | IllegalAccessException ex) {
            }
        }
        else if (setters[columnIndex] != null) {
            try {
                setters[columnIndex].invoke(item, value);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            }
        }
    }
    
    private void initColumnsLookUp() {
        itemClass = null;
        
        if (columnNames == null) {
            fields = null;
            getters = null;
            setters = null;
            columnLookedUp = null;
        }
        else {
            fields = new Field[columnNames.length];
            getters = new Method[columnNames.length];
            setters = new Method[columnNames.length];
            columnLookedUp = new boolean[columnNames.length];
            
            for (int i = 0; i < columnNames.length; ++i) {
                fields[i] = null;
                getters[i] = null;
                setters[i] = null;
                columnLookedUp[i] = false;
            }
        }
    }
    
    private void lookUpColumnAt(Object item, int columnIndex, Class columnClass) {
        if (itemClass == null) {
            itemClass = item.getClass();
        }
        
        try {
            fields[columnIndex] = itemClass.getField(columnNames[columnIndex]);
        }
        catch (NoSuchFieldException | SecurityException ex) {
            String getterName = columnNames[columnIndex];
            if (getterName.length() > 1) {
                getterName = Character.toUpperCase(getterName.charAt(0)) + getterName.substring(1);
            }
            else {
                getterName = getterName.toUpperCase();
            }
            
            String setterName = "set" + getterName;
            getterName = "get" + getterName;
            
            try {
                getters[columnIndex] = itemClass.getMethod(getterName);
            }
            catch (NoSuchMethodException | SecurityException ex1) {
                if (columnClass == Boolean.TYPE) {
                    getterName = "is" + getterName.substring(3);
                    try {
                        getters[columnIndex] = itemClass.getMethod(getterName);
                    }
                    catch (NoSuchMethodException | SecurityException ex2) {
                        getterName = "has" + getterName.substring(2);
                        try {
                            getters[columnIndex] = itemClass.getMethod(getterName);
                        }
                        catch (NoSuchMethodException | SecurityException ex3) {
                        }
                    }
                }
            }
            
            try {
                setters[columnIndex] = itemClass.getMethod(setterName);
            }
            catch (NoSuchMethodException | SecurityException ex1) {
            }
        }
        
        columnLookedUp[columnIndex] = true;
    }

}
