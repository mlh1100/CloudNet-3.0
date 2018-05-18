package de.dytanic.cloudnet.util.collection;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IFilter<T> extends AutoCloseable {

    T first();

    T remove();

    int size();

    IFilter<T> filter(Predicate<T> predicate);

    void forEach(Consumer<T> consumer);

}