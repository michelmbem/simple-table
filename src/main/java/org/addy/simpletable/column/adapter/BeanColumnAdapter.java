package org.addy.simpletable.column.adapter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Mike
 */
public class BeanColumnAdapter extends AssociativeColumnAdapter {
    protected PropertyDescriptor[] properties = null;

    public BeanColumnAdapter(String... propertyNames) {
        super(propertyNames);
    }

    public BeanColumnAdapter(Class<?> itemClass, String... propertyNames) {
        super(propertyNames);
        loadPropertiesFrom(itemClass);
    }

    @Override
    public void setColumnNames(String[] columnNames) {
        super.setColumnNames(columnNames);
        this.properties = null;
    }

    @Override
    public boolean isCellEditable(int columnIndex) {
        return properties == null || properties[columnIndex].getWriteMethod() != null;
    }

    @Override
    public Object getValueAt(Object item, int columnIndex) {
        if (properties == null) loadPropertiesFrom(item.getClass());

        try {
            return properties[columnIndex].getReadMethod().invoke(item);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {
            return null;
        }
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        if (properties == null) loadPropertiesFrom(item.getClass());

        try {
            properties[columnIndex].getWriteMethod().invoke(item, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {
        }
    }

    public Class<?> getColumnClassAt(int columnIndex) {
        return properties == null ? Object.class : properties[columnIndex].getPropertyType();
    }

    private void loadPropertiesFrom(Class<?> itemClass) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(itemClass);
            PropertyDescriptor[] allProperties = beanInfo.getPropertyDescriptors();

            properties = new PropertyDescriptor[columnNames.length];

            for (int i = 0; i < properties.length; ++i) {
                properties[i] = null;

                for (PropertyDescriptor property : allProperties) {
                    if (property.getName().equals(columnNames[i])) {
                        properties[i] = property;
                        break;
                    }
                }

                if (properties[i] == null)
                    throw new IllegalArgumentException("Could not find a " + columnNames[i] + " property in class " + itemClass.getName());
            }
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }
}
