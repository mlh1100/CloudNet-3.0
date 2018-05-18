package de.dytanic.cloudnet.core.service.minecraft;

import de.dytanic.cloudnet.core.event.service.minecraft.*;
import de.dytanic.cloudnet.core.service.AbstractService;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.event.IEventManager;
import de.dytanic.cloudnet.service.ServiceId;
import de.dytanic.cloudnet.service.impl.info.MinecraftServerInfo;
import de.dytanic.cloudnet.service.process.MinecraftServiceProcessMeta;
import lombok.Getter;

import java.io.File;

public abstract class AbstractMinecraftServer extends AbstractService<MinecraftServerInfo> {

    @Getter
    protected String baseTempDir;

    @Getter
    protected MinecraftServiceProcessMeta processMeta;

    public AbstractMinecraftServer(String baseTempDir, ServiceId serviceId, MinecraftServiceProcessMeta minecraftServiceProcessMeta)
    {
        this.baseTempDir = baseTempDir;
        this.processMeta = minecraftServiceProcessMeta;
        this.serviceId = serviceId;

        this.directoryPath = baseTempDir + "/minecraft/" + serviceId.getUniqueId().toString();
        this.directory = new File(this.directoryPath);

        this.init();
    }

    private void init()
    {
        this.directory.mkdirs();
    }

    //Override

    /*= ------------------------------------------------------------------------- =*/

    @Override
    protected void close0()
    {
        this.eventManager().callEvent(new MinecraftServerCloseEvent(this));
    }

    /*= ------------------------------------------------------------------------- =*/

    protected final IEventManager eventManager()
    {
        return CloudNetDriver.getInstance().getEventManager();
    }
}