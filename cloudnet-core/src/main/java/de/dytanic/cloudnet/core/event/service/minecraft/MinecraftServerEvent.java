package de.dytanic.cloudnet.core.event.service.minecraft;

import de.dytanic.cloudnet.core.event.CloudNetCoreEvent;
import de.dytanic.cloudnet.core.service.minecraft.AbstractMinecraftServer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class MinecraftServerEvent extends CloudNetCoreEvent {

    private AbstractMinecraftServer minecraftServer;

}