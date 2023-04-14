package org.addy.simpletable.model;

import java.io.Serializable;
import java.util.Objects;

public class ListItem<T extends Serializable> implements Serializable, Comparable<ListItem<T>> {
    private static final long serialVersionUID = 1L;

    private final T value;
    private final String label;

    public ListItem(T value, String label) {
        this.value = value;
        this.label = label;
    }

    public ListItem(T value) {
        this(value, value != null ? value.toString() : null);
    }

    public ListItem() {
        this(null, null);
    }

    public T getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem<?> listItem = (ListItem<?>) o;
        return Objects.equals(value, listItem.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return label != null ? label : "";
    }

    @Override
    public int compareTo(ListItem<T> other) {
        return toString().compareTo(other.toString());
    }
}
