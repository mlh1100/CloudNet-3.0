package de.dytanic.cloudnet.core.event.modules;

import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.module.ModuleConfig;
import lombok.Getter;

@Getter
public final class ModulePreUnloadEvent extends ModuleEvent {

    private final Module module;

    public ModulePreUnloadEvent(IModuleManager moduleManager, ModuleConfig moduleConfig, Module module)
    {
        super(moduleManager, moduleConfig);

        this.module = module;
    }
}