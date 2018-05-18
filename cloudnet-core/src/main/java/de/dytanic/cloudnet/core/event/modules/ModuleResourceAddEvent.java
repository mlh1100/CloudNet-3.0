package de.dytanic.cloudnet.core.event.modules;

import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.IModuleResource;
import de.dytanic.cloudnet.module.ModuleConfig;
import lombok.Getter;

@Getter
public final class ModuleResourceAddEvent extends ModuleEvent {

    private final IModuleResource moduleResource;

    public ModuleResourceAddEvent(IModuleManager moduleManager, ModuleConfig moduleConfig, IModuleResource moduleResource)
    {
        super(moduleManager, moduleConfig);

        this.moduleResource = moduleResource;
    }
}