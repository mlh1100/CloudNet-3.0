package de.dytanic.cloudnet.core.event.service.minecraft;

import de.dytanic.cloudnet.core.service.minecraft.AbstractMinecraftServer;
import de.dytanic.cloudnet.event.interfaces.Cancelable;
import lombok.Getter;
import lombok.Setter;

public final class MinecraftServerPreStartEvent extends MinecraftServerEvent implements Cancelable {

    @Getter
    @Setter
    private boolean cancelled;

    public MinecraftServerPreStartEvent(AbstractMinecraftServer minecraftServer)
    {
        super(minecraftServer);
    }
}