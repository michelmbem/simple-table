package org.addy.simpletable.column.adapter;

import java.util.Objects;
import java.util.function.IntPredicate;

/**
 * A ColumnAdapter that uses functional interfaces, mainly in the form of
 * method references or lambda expressions, to read and write cell values.
 *
 * @author Michel Mbem
 */
public class FunctionalColumnAdapter<T> implements ColumnAdapter {
    private final ItemReader<T> itemReader;
    private final ItemWriter<T> itemWriter;
    private final IntPredicate editablePredicate;

    public FunctionalColumnAdapter(ItemReader<T> itemReader,
                                   ItemWriter<T> itemWriter,
                                   IntPredicate editablePredicate) {

        this.itemReader = Objects.requireNonNull(itemReader);
        this.itemWriter = Objects.requireNonNull(itemWriter);
        this.editablePredicate = Objects.requireNonNull(editablePredicate);
    }

    public FunctionalColumnAdapter(ItemReader<T> itemReader, ItemWriter<T> itemWriter) {
        this(itemReader, itemWriter, i -> true);
    }

    public FunctionalColumnAdapter(ItemReader<T> itemReader) {
        this(itemReader, (t, i, o) -> {}, i -> false);
    }

    @Override
    public Object getValueAt(Object item, int columnIndex) {
        return itemReader.read((T) item, columnIndex);
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        itemWriter.write((T) item, columnIndex, value);
    }

    @Override
    public boolean isCellEditable(int columnIndex) {
        return editablePredicate.test(columnIndex);
    }

    @FunctionalInterface
    public interface ItemReader<T> {
        Object read(T item, int index);
    }

    @FunctionalInterface
    public interface ItemWriter<T> {
        void write(T item, int index, Object value);
    }
}
