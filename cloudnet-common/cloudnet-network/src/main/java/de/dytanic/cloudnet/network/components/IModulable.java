package de.dytanic.cloudnet.network.components;

import java.util.Collection;

public interface IModulable {

    Collection<String> getModules();

    default boolean hasModule(String name)
    {
        return getModules().contains(name);
    }

}