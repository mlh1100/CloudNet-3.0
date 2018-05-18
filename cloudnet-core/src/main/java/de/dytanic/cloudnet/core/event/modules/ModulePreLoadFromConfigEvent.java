package de.dytanic.cloudnet.core.event.modules;

import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.ModuleConfig;

public final class ModulePreLoadFromConfigEvent extends ModuleEvent {

    public ModulePreLoadFromConfigEvent(IModuleManager moduleManager, ModuleConfig moduleConfig)
    {
        super(moduleManager, moduleConfig);
    }
}