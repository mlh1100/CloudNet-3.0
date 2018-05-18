package de.dytanic.cloudnet.registry;

import java.util.Collection;

public interface IServiceRegistry {

    <T extends IRegistryService, E extends T> void registerService(Class<T> clazz, IServiceProvider serviceProvider, E e);

    <T extends IRegistryService, E extends T> void registerServices(Class<T> clazz, IServiceProvider serviceProvider, E... e);

    <T extends IRegistryService, E extends T> E getService(Class<T> clazz, String serviceName);

    <T extends IRegistryService> Collection<T> getServices(Class<T> clazz);

    <T extends IRegistryService> boolean hasService(Class<T> clazz, String serviceName);

    <T extends IRegistryService> void removeService(Class<T> clazz, String name);

    <T extends IRegistryService> void removeServices(Class<T> clazz);

    <T extends IRegistryService> void removeServices(Class<T> clazz, IServiceProvider serviceProvider);

    void removeServices(IServiceProvider serviceProvider);

}