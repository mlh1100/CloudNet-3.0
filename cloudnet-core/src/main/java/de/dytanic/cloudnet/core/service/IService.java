package de.dytanic.cloudnet.core.service;

import de.dytanic.cloudnet.service.IServiceInfo;
import de.dytanic.cloudnet.service.util.ServiceRuntimeState;
import de.dytanic.cloudnet.service.util.ServiceType;

import java.util.UUID;

public interface IService<T extends IServiceInfo> extends AutoCloseable {

    UUID getUniqueId();

    ServiceType getType();

    ServiceRuntimeState getRuntimeState();

    boolean executeCommand(String commandLine);

    boolean prepare();

    boolean start();

    boolean stop();

    boolean delete();

    boolean restart();

    Process getProcess();

    T getServiceInfo();

}