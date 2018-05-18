package de.dytanic.cloudnet.module;

public interface IModuleExecutor {

    default void onLoad()
    {

    }

    default void onEnable()
    {

    }

    default void onUnload()
    {

    }

    void setLoaded(boolean value);

    boolean isLoaded();

}