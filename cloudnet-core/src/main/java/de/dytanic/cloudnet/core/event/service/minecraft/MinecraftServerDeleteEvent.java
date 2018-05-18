package de.dytanic.cloudnet.core.event.service.minecraft;

import de.dytanic.cloudnet.core.service.minecraft.AbstractMinecraftServer;

public final class MinecraftServerDeleteEvent extends MinecraftServerEvent {

    public MinecraftServerDeleteEvent(AbstractMinecraftServer minecraftServer)
    {
        super(minecraftServer);
    }
}