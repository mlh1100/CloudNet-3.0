package de.dytanic.cloudnet.module.util;

import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.IModuleResource;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.module.ModuleConfig;

import java.util.Collection;

public class RuntimeModuleHandlerAdapter implements IRuntimeModuleHandler {

    @Override
    public void handleAddModuleResource(IModuleManager moduleManager, ModuleConfig moduleConfig, IModuleResource module)
    {

    }

    @Override
    public void handlePreLoadModule(IModuleManager moduleManager, ModuleConfig module)
    {

    }

    @Override
    public void handlePreLoadModule(IModuleManager moduleManager, Module module)
    {

    }

    @Override
    public void handlePostLoadModule(IModuleManager moduleManager, Module module)
    {

    }

    @Override
    public void handlePreUnloadModule(IModuleManager moduleManager, Module module)
    {

    }

    @Override
    public void handlePostUnloadModule(IModuleManager moduleManager, ModuleConfig module)
    {

    }

    @Override
    public void handleDependenciesNotFound(IModuleManager moduleManager, ModuleConfig moduleConfig, Collection<String> dependencies)
    {

    }

}