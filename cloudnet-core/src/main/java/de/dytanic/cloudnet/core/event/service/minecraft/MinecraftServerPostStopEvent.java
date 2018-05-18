package de.dytanic.cloudnet.core.event.service.minecraft;

import de.dytanic.cloudnet.core.service.minecraft.AbstractMinecraftServer;

public final class MinecraftServerPostStopEvent extends MinecraftServerEvent {

    public MinecraftServerPostStopEvent(AbstractMinecraftServer minecraftServer)
    {
        super(minecraftServer);
    }
}