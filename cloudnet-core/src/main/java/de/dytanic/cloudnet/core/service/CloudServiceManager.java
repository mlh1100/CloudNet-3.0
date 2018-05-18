package de.dytanic.cloudnet.core.service;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.core.service.bungee.AbstractProxyServer;
import de.dytanic.cloudnet.core.service.minecraft.AbstractMinecraftServer;

import java.util.Map;
import java.util.UUID;

public final class CloudServiceManager {

    private final Map<UUID, AbstractMinecraftServer> minecraftServices = $.newConcurrentHashMap();

    private final Map<UUID, AbstractProxyServer> proxyServices = $.newConcurrentHashMap();

    public CloudServiceManager()
    {

    }

}