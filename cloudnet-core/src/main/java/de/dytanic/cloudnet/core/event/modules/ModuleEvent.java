package de.dytanic.cloudnet.core.event.modules;

import de.dytanic.cloudnet.core.event.CloudNetCoreEvent;
import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.ModuleConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class ModuleEvent extends CloudNetCoreEvent {

    private final IModuleManager moduleManager;

    private final ModuleConfig moduleConfig;

}