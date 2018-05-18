package de.dytanic.cloudnet.core.event.modules;

import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.module.ModuleConfig;
import lombok.Getter;

@Getter
public final class ModulePostLoadEvent extends ModuleEvent {

    private final Module module;

    public ModulePostLoadEvent(IModuleManager moduleManager, ModuleConfig moduleConfig, Module module)
    {
        super(moduleManager, moduleConfig);

        this.module = module;
    }
}