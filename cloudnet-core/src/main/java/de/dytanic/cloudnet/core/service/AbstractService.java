package de.dytanic.cloudnet.core.service;

import de.dytanic.cloudnet.service.IServiceInfo;
import de.dytanic.cloudnet.service.ServiceId;
import de.dytanic.cloudnet.service.util.ServiceRuntimeState;
import de.dytanic.cloudnet.service.util.ServiceType;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
public abstract class AbstractService<T extends IServiceInfo> implements IService<T> {

    protected Process process;

    protected ServiceType type;

    protected ServiceId serviceId;

    protected String directoryPath;

    protected File directory;

    @Setter
    protected T serviceInfo;

    protected ServiceRuntimeState runtimeState = ServiceRuntimeState.CREATED;

    @Override
    public final boolean executeCommand(String commandLine)
    {
        if (!this.executeCommand0(commandLine)) return false;

        if (process != null && process.isAlive())
            try
            {
                process.getOutputStream().write(commandLine.concat(System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
                process.getOutputStream().flush();

                return true;
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

        return false;
    }

    @Override
    public UUID getUniqueId()
    {
        return this.serviceId.getUniqueId();
    }

    @Override
    public final boolean restart()
    {
        if (!this.restart0()) return false;

        this.stop();
        this.start();

        return true;
    }

    @Override
    public final void close() throws Exception
    {
        this.stop();
        this.delete();
        this.close0();

        this.runtimeState = ServiceRuntimeState.DELETED;
    }

    /*= --------------------------------------------------------------------- =*/

    protected final void startProcess(ProcessBuilder processBuilder) throws IOException
    {
        if (processBuilder == null) return;
        if (this.process != null) killProcess();

        this.process = processBuilder.start();
        this.runtimeState = ServiceRuntimeState.RUNNING;
    }

    protected final void killProcess()
    {
        if (this.process != null && this.process.isAlive())
            this.killProcess0();

        this.process = null;
        this.runtimeState = ServiceRuntimeState.STOPPED;
    }

    /*= --------------------------------------------------------------------- =*/

    protected void killProcess0()
    {

    }

    protected boolean restart0()
    {
        return true;
    }

    protected boolean executeCommand0(String commandLine)
    {
        return true;
    }

    protected void close0()
    {

    }
}