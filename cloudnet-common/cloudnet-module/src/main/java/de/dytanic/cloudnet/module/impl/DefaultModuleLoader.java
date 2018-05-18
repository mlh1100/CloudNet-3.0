package de.dytanic.cloudnet.module.impl;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.module.Module;
import de.dytanic.cloudnet.module.ModuleConfig;
import de.dytanic.cloudnet.module.exception.ModuleMainClassNotFoundException;
import de.dytanic.cloudnet.module.util.FinalizeURLClassLoader;
import de.dytanic.cloudnet.module.IModuleLoader;
import de.dytanic.cloudnet.module.IModuleResource;
import de.dytanic.cloudnet.module.util.ModuleLibrary;
import lombok.Getter;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

final class DefaultModuleLoader implements IModuleLoader {

    @Getter
    private final URLClassLoader classLoader;

    @Getter
    private final IModuleResource moduleResource;

    @Getter
    private final ModuleConfig moduleConfig;

    public DefaultModuleLoader(IModuleResource moduleResource, ModuleConfig moduleConfig) throws Exception
    {
        this.moduleResource = moduleResource;

        this.moduleConfig = moduleConfig;

        if (moduleConfig.getModuleLibraries() == null || moduleConfig.getModuleLibraries().length == 0)
            this.classLoader = new FinalizeURLClassLoader(this.moduleResource.getUrl());
        else
        {
            Collection<URL> urls = $.newArrayList();

            for (ModuleLibrary moduleLibrary : moduleConfig.getModuleLibraries())
                urls.add(moduleLibrary.getResourceUrl());

            urls.add(moduleResource.getUrl());

            this.classLoader = new FinalizeURLClassLoader(urls.toArray(new URL[0]));
        }
    }

    @Override
    public Module loadModule() throws Exception
    {
        if(!isAvailable()) return null;

        if(moduleConfig.getMain() == null) throw new ModuleMainClassNotFoundException("main class cannot be found");

        Module module = (Module) classLoader.loadClass(this.moduleConfig.getMain()).getDeclaredConstructor().newInstance();

        module.setModuleLoader(this);
        module.setModuleConfig(moduleConfig);

        return module;
    }

    @Override
    public void close() throws Exception
    {
        classLoader.close();
    }

    @Override
    public boolean isAvailable()
    {
        return moduleConfig != null && moduleConfig.getMain() != null && moduleConfig.getName() != null && moduleConfig.getVersion() != null;
    }
}