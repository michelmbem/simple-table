package org.addy.simpletable.column.adapter;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

/**
 * A ColumnAdapter that maps column names to bean property names.
 * It uses introspection to get or set column values.
 *
 * @author Michel Mbem
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

    private void loadPropertiesFrom(Class<?> beanType) {
        PropertyDescriptor[] allProperties = getAllProperties(beanType);
        properties = new PropertyDescriptor[columnNames.length];

        for (int i = 0; i < properties.length; ++i) {
            final String propertyName = columnNames[i];
            properties[i] = Stream.of(allProperties)
                    .filter(p -> p.getName().equals(propertyName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Could not find a " +
                            propertyName + " property in class " + beanType.getName()));
        }
    }

    private static PropertyDescriptor[] getAllProperties(Class<?> beanType) {
        if (beanType.isRecord()) {
            return Stream.of(beanType.getRecordComponents())
                    .map(component -> {
                        try {
                            return new PropertyDescriptor(
                                    component.getName(),
                                    component.getAccessor(),
                                    null);
                        } catch (IntrospectionException e) {
                            throw new IllegalStateException(e);
                        }})
                    .toArray(PropertyDescriptor[]::new);
        }

        try {
            return Introspector.getBeanInfo(beanType).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }
}
