package de.dytanic.cloudnet.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class QueueFilter<T> implements IFilter<T> {

    protected Queue<T> filter;

    public QueueFilter(Collection<T> collection)
    {
        this.filter = new ArrayBlockingQueue<>(collection.size(), true, collection);
    }

    @Override
    public T first()
    {
        return this.filter.peek();
    }

    @Override
    public T remove()
    {
        return this.filter.poll();
    }

    @Override
    public int size()
    {
        return this.filter.size();
    }

    @Override
    public QueueFilter<T> filter(Predicate<T> predicate)
    {
        Collection<T> collection = new ArrayList<>(filter.size());

        for(T entry : this.filter)
            if(predicate.test(entry))
                collection.add(entry);

        for(T entry : collection) this.filter.remove(entry);

        return this;
    }

    @Override
    public void forEach(Consumer<T> consumer)
    {
        this.filter.forEach(consumer);
    }

    @Override
    public void close()
    {
        this.filter.clear();
    }
}