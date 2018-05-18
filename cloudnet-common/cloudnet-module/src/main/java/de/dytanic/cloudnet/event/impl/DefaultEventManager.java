package de.dytanic.cloudnet.event.impl;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.event.*;
import de.dytanic.cloudnet.event.async.AsyncEvent;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.scheduler.TaskScheduler;
import de.dytanic.cloudnet.util.TrioEntry;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by Tareko on 17.12.2017.
 */
public final class DefaultEventManager implements IEventManager {

    private final Map<Class<? extends Event>, Deque<ListenerEntry>> listeners = $.newHashMap();

    public <E extends Event> E callEvent(E event)
    {
        if (event instanceof AsyncEvent)
        {
            TaskScheduler.runtimeScheduler().schedule(new Runnable() {
                @Override
                public void run()
                {
                    post(event);
                    ((AsyncEvent) event).handle((AsyncEvent) event);
                }
            });
        } else post(event);
        return event;
    }

    @Override
    public void clearListeners()
    {
        this.listeners.clear();
    }

    @Override
    public DefaultEventManager registerListener(Module module, IEventListener listener)
    {
        if (listener == null) throw new NullPointerException();
        if (containsListener((Class<IEventListener>) listener.getClass())) return this;

        for (Method method : listener.getClass().getDeclaredMethods())
            register(checkAccessAndGet(method), module, listener, method);

        return this;
    }

    @Override
    public DefaultEventManager unregisterListener(Class<? extends IEventListener> listener)
    {
        iterator(new BiConsumer<ListenerEntry, Deque<ListenerEntry>>() {
            @Override
            public void accept(ListenerEntry entry, Deque<ListenerEntry> listenerEntries)
            {
                if (entry.listener.getClass().equals(listener))
                    listenerEntries.remove(entry);
            }
        });
        return this;
    }

    @Override
    public DefaultEventManager unregisterListener(Module module)
    {
        iterator(new BiConsumer<ListenerEntry, Deque<ListenerEntry>>() {
            @Override
            public void accept(ListenerEntry entry, Deque<ListenerEntry> listenerEntries)
            {
                if ((entry.module != null && entry.module.equals(module)) || (entry.module == null && module == null))
                    listenerEntries.remove(entry);
            }
        });
        return this;
    }

    @Override
    public boolean containsListener(Class<? extends IEventListener> listener)
    {
        for (Deque<ListenerEntry> entries : this.listeners.values())
            for (ListenerEntry entry : entries)
                if (entry.listener.getClass().equals(listener))
                    return true;

        return false;
    }

    /*= --------------------------------------------------------------------------- =*/

    private void post(Event event)
    {
        iterator(new BiConsumer<ListenerEntry, Deque<ListenerEntry>>() {
            @Override
            public void accept(ListenerEntry entry, Deque<ListenerEntry> listenerEntries)
            {
                entry.invoke(event);
            }
        });
    }

    private void iterator(BiConsumer<ListenerEntry, Deque<ListenerEntry>> runnabled)
    {
        for (Deque<ListenerEntry> entries : this.listeners.values())
            for (ListenerEntry entry : entries) runnabled.accept(entry, entries);
    }

    private TrioEntry<Boolean, Class<? extends Event>, ListenerPriority> checkAccessAndGet(Method method)
    {

        if (method.getParameters().length == 1 && method.isAnnotationPresent(Listener.class) &&
                Event.class.isAssignableFrom(method.getParameters()[0].getType()))
        {
            return new TrioEntry<>(true, (Class<? extends Event>) method.getParameters()[0].getType(),
                    method.getAnnotation(Listener.class).value());
        } else
            return new TrioEntry<>(false, null, null);
    }

    private void register(TrioEntry<Boolean, Class<? extends Event>, ListenerPriority> trio, Module module, IEventListener iEventListener, Method method)
    {
        if(!trio.getFirst()) return;

        if (!this.listeners.containsKey(trio.getSecond()))
            this.listeners.put(trio.getSecond(), $.newConcurrentLinkedDeque());

        method.setAccessible(true);
        ListenerEntry listenerEntry = new ListenerEntry(module, iEventListener, method, trio.getSecond());

        switch (trio.getThird())
        {
            case FIRST:
                this.listeners.get(trio.getSecond()).offerFirst(listenerEntry);
                break;
            case LAST:
                this.listeners.get(trio.getSecond()).offerLast(listenerEntry);
                break;
            case NORMAL:
                this.listeners.get(trio.getSecond()).offer(listenerEntry);
                break;
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private final class ListenerEntry {

        private Module module;

        private IEventListener listener;

        private Method method;

        private Class<? extends Event> eventClazz;

        public void invoke(Event event)
        {
            if (eventClazz.equals(event.getClass()))
                try
                {
                    method.invoke(listener, event);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
    }

}