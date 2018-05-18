package de.dytanic.cloudnet.core.event.modules;

import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.ModuleConfig;

public final class ModulePostUnloadEvent extends ModuleEvent {

    public ModulePostUnloadEvent(IModuleManager moduleManager, ModuleConfig moduleConfig)
    {
        super(moduleManager, moduleConfig);
    }
}