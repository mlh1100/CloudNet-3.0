package de.dytanic.cloudnet.registry;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.util.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class DefaultServiceRegistry implements IServiceRegistry {

    protected final Map<Class<? extends IRegistryService>, Collection<Pair<IServiceProvider, ? extends IRegistryService>>> registry = $.newConcurrentHashMap();

    public DefaultServiceRegistry()
    {
    }


    @Override
    public <T extends IRegistryService, E extends T> void registerService(Class<T> clazz, IServiceProvider serviceProvider, E e)
    {
        if (clazz == null || serviceProvider == null || e == null) return;

        if (!this.registry.containsKey(clazz))
            this.registry.put(clazz, $.newCopyOnWriteArraySet());

        if (hasService(clazz, e.getServiceName())) removeService(clazz, e.getServiceName());
        registry.get(clazz).add(new Pair<>(serviceProvider, e));
    }

    @Override
    public <T extends IRegistryService, E extends T> void registerServices(Class<T> clazz, IServiceProvider serviceProvider, E... e)
    {
        for (E element : e) registerService(clazz, serviceProvider, element);
    }

    @Override
    public <T extends IRegistryService, E extends T> E getService(Class<T> clazz, String serviceName)
    {
        Collection<? extends IRegistryService> registryServices = getServices(clazz);

        for (IRegistryService iRegistryService : registryServices)
            if (iRegistryService != null && iRegistryService.getServiceName().equalsIgnoreCase(serviceName))
                return (E) iRegistryService;

        return null;
    }

    @Override
    public <T extends IRegistryService> Collection<T> getServices(Class<T> clazz)
    {
        Collection<T> collection = $.newLinkedList();

        if (clazz != null)
            for (Pair<IServiceProvider, ? extends IRegistryService> entry : this.registry.get(clazz))
                collection.add((T) entry.getSecond());

        return collection;
    }

    @Override
    public <T extends IRegistryService> boolean hasService(Class<T> clazz, String serviceName)
    {
        if (!this.registry.containsKey(clazz) || clazz == null || serviceName == null) return false;

        return $.filterFirst(this.registry.get(clazz), new Predicate<Pair<IServiceProvider, ? extends IRegistryService>>() {
            @Override
            public boolean test(Pair<IServiceProvider, ? extends IRegistryService> entry)
            {
                return entry.getSecond().getServiceName() != null && entry.getSecond().getServiceName().equalsIgnoreCase(serviceName);
            }
        }) != null;
    }

    @Override
    public <T extends IRegistryService> void removeService(Class<T> clazz, String name)
    {
        if (clazz == null || name == null || !this.registry.containsKey(clazz)) return;

        Pair<IServiceProvider, ? extends IRegistryService> value = null;

        for (Pair<IServiceProvider, ? extends IRegistryService> entry : this.registry.get(clazz))
            if (entry.getSecond() != null && entry.getSecond().getServiceName().equalsIgnoreCase(name))
                value = entry;

        if (value != null) this.registry.get(clazz).remove(value);
    }

    @Override
    public <T extends IRegistryService> void removeServices(Class<T> clazz)
    {
        if (clazz == null || !this.registry.containsKey(clazz)) return;

        this.registry.get(clazz).clear();
    }

    @Override
    public <T extends IRegistryService> void removeServices(Class<T> clazz, IServiceProvider serviceProvider)
    {
        if (clazz == null || serviceProvider == null || !this.registry.containsKey(clazz)) return;

        Collection<Pair<IServiceProvider, ? extends IRegistryService>> collection = $.newArrayList();

        $.filterFirst(this.registry.get(clazz), new Predicate<Pair<IServiceProvider, ? extends IRegistryService>>() {
            @Override
            public boolean test(Pair<IServiceProvider, ? extends IRegistryService> iServiceProviderPair)
            {
                if (iServiceProviderPair.getFirst().equals(serviceProvider))
                    collection.add(iServiceProviderPair);

                return false;
            }
        });

        for (Object c : collection)
            this.registry.get(clazz).remove(c);

    }

    @Override
    public void removeServices(IServiceProvider serviceProvider)
    {
        if (serviceProvider == null) return;

        for (Collection<Pair<IServiceProvider, ? extends IRegistryService>> collection : this.registry.values())
            for (Pair<IServiceProvider, ? extends IRegistryService> entry : collection)
                if (entry.getFirst().equals(serviceProvider))
                    collection.remove(entry);

    }

}