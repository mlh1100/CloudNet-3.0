package de.dytanic.cloudnet.event;

import de.dytanic.cloudnet.module.Module;

/**
 * Created by Tareko on 20.12.2017.
 */
public interface IEventManager {

    <E extends Event> E callEvent(E event);

    void clearListeners();

    IEventManager registerListener(Module module, IEventListener listener);

    IEventManager unregisterListener(Class<? extends IEventListener> listener);

    IEventManager unregisterListener(Module identity);

    boolean containsListener(Class<? extends IEventListener> listener);

    default IEventManager unregisterListeners(Class<IEventListener>... listeners)
    {
        for (Class<IEventListener> listener : listeners) unregisterListener(listener);
        return this;
    }

    default IEventManager registerListeners(Module module, IEventListener... listeners)
    {
        for (IEventListener listener : listeners) registerListener(module, listener);
        return this;
    }

}