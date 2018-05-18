package de.dytanic.cloudnet.module.util;

import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.IModuleResource;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.module.ModuleConfig;

import java.util.Collection;

public interface IRuntimeModuleHandler {

    void handleAddModuleResource(IModuleManager moduleManager, ModuleConfig moduleConfig, IModuleResource module);

    void handlePreLoadModule(IModuleManager moduleManager, ModuleConfig module);

    void handlePreLoadModule(IModuleManager moduleManager, Module module);

    void handlePostLoadModule(IModuleManager moduleManager, Module module);

    void handlePreUnloadModule(IModuleManager moduleManager, Module module);

    void handlePostUnloadModule(IModuleManager moduleManager, ModuleConfig module);

    void handleDependenciesNotFound(IModuleManager moduleManager, ModuleConfig moduleConfig, Collection<String> dependencies);

}