package de.dytanic.cloudnet.network.components;

public interface INetworkComponentSnapshot<T extends INetworkComponent> extends IModulable {

    T getProvider();

    int getCpuCores();

    int getMemoryLimit();

    int getUsedMemory();

    double getCpuUsage();

}