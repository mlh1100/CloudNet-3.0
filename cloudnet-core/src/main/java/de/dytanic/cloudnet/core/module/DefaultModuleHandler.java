package de.dytanic.cloudnet.core.module;

import de.dytanic.cloudnet.core.event.modules.*;
import de.dytanic.cloudnet.event.IEventManager;
import de.dytanic.cloudnet.language.LanguageManager;
import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.logging.LogLevel;
import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.IModuleResource;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.module.ModuleConfig;
import de.dytanic.cloudnet.module.util.IRuntimeModuleHandler;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public final class DefaultModuleHandler implements IRuntimeModuleHandler {

    private final IEventManager eventManager;

    private final ILogProvider logProvider;

    @Override
    public void handleAddModuleResource(IModuleManager moduleManager, ModuleConfig moduleConfig, IModuleResource module)
    {
        eventManager.callEvent(new ModuleResourceAddEvent(moduleManager, moduleConfig, module));
    }

    @Override
    public void handlePreLoadModule(IModuleManager moduleManager, ModuleConfig module)
    {
        eventManager.callEvent(new ModulePreLoadFromConfigEvent(moduleManager, module));
    }

    @Override
    public void handlePreLoadModule(IModuleManager moduleManager, Module module)
    {
        eventManager.callEvent(new ModulePreLoadEvent(moduleManager, module.getModuleConfig(), module));
    }

    @Override
    public void handlePostLoadModule(IModuleManager moduleManager, Module module)
    {
        eventManager.callEvent(new ModulePostLoadEvent(moduleManager, module.getModuleConfig(), module));

        log(
                LanguageManager.getMessage("module-load-successfully")
                        .replace("%module%", module.getName())
                        .replace("%author%", module.getAuthor())
                        .replace("%version%", module.getVersion())
                        .replace("%website%", module.getWebsite())
                        .replace("%description%", module.getDescription())
        );
    }

    @Override
    public void handlePreUnloadModule(IModuleManager moduleManager, Module module)
    {
        eventManager.callEvent(new ModulePreUnloadEvent(moduleManager, module.getModuleConfig(), module));
    }

    @Override
    public void handlePostUnloadModule(IModuleManager moduleManager, ModuleConfig module)
    {
        eventManager.callEvent(new ModulePostUnloadEvent(moduleManager, module));

        log(
                LanguageManager.getMessage("module-unload-successfully")
                        .replace("%module%", module.getName())
                        .replace("%author%", module.getAuthor())
                        .replace("%version%", module.getVersion())
                        .replace("%website%", module.getWebsite())
                        .replace("%description%", module.getDescription())
        );
    }

    @Override
    public void handleDependenciesNotFound(IModuleManager moduleManager, ModuleConfig moduleConfig, Collection<String> dependencies)
    {

    }

    private void log(String message)
    {
        this.logProvider.log(LogLevel.MODULE, message);
    }

}