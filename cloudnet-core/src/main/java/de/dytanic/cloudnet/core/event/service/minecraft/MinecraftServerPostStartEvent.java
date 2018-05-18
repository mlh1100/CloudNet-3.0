package de.dytanic.cloudnet.core.event.service.minecraft;

import de.dytanic.cloudnet.core.service.minecraft.AbstractMinecraftServer;

public final class MinecraftServerPostStartEvent extends MinecraftServerEvent {

    public MinecraftServerPostStartEvent(AbstractMinecraftServer minecraftServer)
    {
        super(minecraftServer);
    }
}