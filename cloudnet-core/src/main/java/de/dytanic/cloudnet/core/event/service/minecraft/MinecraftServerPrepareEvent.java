package de.dytanic.cloudnet.core.event.service.minecraft;

import de.dytanic.cloudnet.core.service.minecraft.AbstractMinecraftServer;

public final class MinecraftServerPrepareEvent extends MinecraftServerEvent {

    public MinecraftServerPrepareEvent(AbstractMinecraftServer minecraftServer)
    {
        super(minecraftServer);
    }
}