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

    public BeanRowAdapter(Class itemClass, String... columnNames) {
        super(columnNames);
        initColumnsLookUp();
        setItemClass(itemClass);
    }

    @Override
    public void setColumnNames(String[] columnNames) {
        super.setColumnNames(columnNames);
        initColumnsLookUp();
    }

    @Override
    public Object getValueAt(Object item, int columnIndex) {
        if (!columnLookedUp[columnIndex]) {
            lookUpColumnAt(columnIndex, item.getClass());
        }

        if (fields[columnIndex] != null) {
            try {
                return fields[columnIndex].get(item);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
        }

        if (getters[columnIndex] != null) {
            try {
                return getters[columnIndex].invoke(item);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return null;
            }
        }

        return null;
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        if (!columnLookedUp[columnIndex]) {
            lookUpColumnAt(columnIndex, item.getClass());
        }

        if (fields[columnIndex] != null) {
            try {
                fields[columnIndex].set(item, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
            }
        } else if (setters[columnIndex] != null) {
            try {
                setters[columnIndex].invoke(item, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            }
        }
    }

    @Override
    public boolean isCellEditable(int columnIndex) {
        return setters == null || setters[columnIndex] != null;
    }

    public Class getColumnClassAt(int columnIndex) {
        if (fields != null && fields[columnIndex] != null) {
            return fields[columnIndex].getType();
        }
        if (getters != null && getters[columnIndex] != null) {
            return getters[columnIndex].getReturnType();
        }
        return Object.class;
    }

    private void initColumnsLookUp() {
        itemClass = null;

        if (columnNames == null) {
            fields = null;
            getters = null;
            setters = null;
            columnLookedUp = null;
        } else {
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

    private void setItemClass(Class itemClass) {
        this.itemClass = itemClass;
        for (int i = 0; i < columnNames.length; ++i) {
            lookUpColumnAt(i, itemClass);
        }
    }

    private void lookUpColumnAt(int columnIndex, Class clazz) {
        if (itemClass == null) {
            itemClass = clazz;
        }

        try {
            fields[columnIndex] = itemClass.getField(columnNames[columnIndex]);
        } catch (NoSuchFieldException | SecurityException ex) {
            String propertyName = columnNames[columnIndex];
            if (propertyName.length() > 1) {
                propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            } else {
                propertyName = propertyName.toUpperCase();
            }

            try {
                getters[columnIndex] = itemClass.getMethod("get" + propertyName);
            } catch (NoSuchMethodException | SecurityException ex1) {
                Method tmpGetter = null;
                try {
                    tmpGetter = itemClass.getMethod("is" + propertyName);
                } catch (NoSuchMethodException | SecurityException ex2) {
                    try {
                        tmpGetter = itemClass.getMethod("has" + propertyName);
                    } catch (NoSuchMethodException | SecurityException ex3) {
                    }
                } finally {
                    if (tmpGetter != null && tmpGetter.getReturnType() == Boolean.TYPE) {
                        getters[columnIndex] = tmpGetter;
                    }
                }
            }

            if (getters[columnIndex] != null) {
                try {
                    setters[columnIndex] = itemClass.getMethod(
                            "set" + propertyName,
                            getters[columnIndex].getReturnType());
                } catch (NoSuchMethodException | SecurityException ex1) {
                }
            }
        }

        columnLookedUp[columnIndex] = true;
    }

}
