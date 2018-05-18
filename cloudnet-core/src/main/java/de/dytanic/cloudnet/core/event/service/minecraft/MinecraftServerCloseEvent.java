package de.dytanic.cloudnet.core.event.service.minecraft;

import de.dytanic.cloudnet.core.service.minecraft.AbstractMinecraftServer;

public final class MinecraftServerCloseEvent extends MinecraftServerEvent {

    public MinecraftServerCloseEvent(AbstractMinecraftServer minecraftServer)
    {
        super(minecraftServer);
    }
}