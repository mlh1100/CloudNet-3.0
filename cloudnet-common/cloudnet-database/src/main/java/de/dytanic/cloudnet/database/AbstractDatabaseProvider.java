package de.dytanic.cloudnet.database;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.registry.IRemoteableRegistryService;
import de.dytanic.cloudnet.util.AuthConfig;
import lombok.Getter;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
public abstract class AbstractDatabaseProvider implements AutoCloseable, IRemoteableRegistryService {

    protected AbstractDatabaseProvider()
    {
    }

    public abstract IDatabase getDatabase(String name);

    public abstract boolean init(AuthConfig authConfig) throws Exception;

    /*= --------------------------------------------------------------- =*/

    private final Collection<IDatabaseListener> listeners = $.newCopyOnWriteArrayList();

    public final void registerListener(IDatabaseListener listener)
    {
        if (listener == null) return;

        this.listeners.add(listener);
    }

    public final void unregisterListener(Class<? extends IDatabaseListener> clazz)
    {
        IDatabaseListener listener = $.filterFirst(listeners, new Predicate<IDatabaseListener>() {
            @Override
            public boolean test(IDatabaseListener listener)
            {
                return listener.getClass().equals(clazz);
            }
        });

        if (listener != null) listeners.remove(listener);
    }

    public final void call(Consumer<IDatabaseListener> handler)
    {
        if (handler == null) return;

        for (IDatabaseListener listener : listeners)
            handler.accept(listener);
    }

}