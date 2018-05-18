package de.dytanic.cloudnet.module.impl;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.module.*;
import de.dytanic.cloudnet.module.exception.ModuleLoadException;
import de.dytanic.cloudnet.module.util.IRuntimeModuleHandler;
import de.dytanic.cloudnet.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public final class DefaultModuleManager implements IModuleManager {

    private final Map<String, Pair<IModuleResource, ModuleConfig>> loadableResources = $.newConcurrentHashMap();

    private final Map<String, Module> modules = $.newConcurrentHashMap();

    @Setter
    private IRuntimeModuleHandler runtimeModuleHandler;

    @Setter
    @Getter
    private Collection<String> moduleBlackList;

    @Setter
    @Getter
    private File moduleConfigDirectory = new File(IModuleManager.DEFAULT_MODULE_DIRECTORY);

    @Override
    public void detectModule(File file)
    {
        addModuleResource(new FileModuleResource(file));
    }

    @Override
    public void detectModules(URL... urls)
    {
        if (urls == null) return;

        for (URL url : urls) addModuleResource(new URLModuleResource(url));
    }

    @Override
    public void detectModules(File directory)
    {
        if (directory == null || !directory.exists() || !directory.isDirectory()) return;

        for (File file : directory.listFiles(this::filterFiles)) detectModule(file);
    }

    @Override
    public void addModuleResource(IModuleResource moduleResource)
    {
        if (moduleResource == null) return;

        ModuleConfig moduleConfig = moduleResource.loadConfig();

        if (moduleConfig == null || loadableResources.containsKey(moduleConfig.getName()) ||
                modules.containsKey(moduleConfig.getName()) || (moduleBlackList != null && moduleBlackList.contains(moduleConfig.getName())))
            return;

        this.loadableResources.put(moduleConfig.getName(), new Pair<>(moduleResource, moduleConfig));

        if (runtimeModuleHandler != null)
            runtimeModuleHandler.handleAddModuleResource(this, moduleConfig, moduleResource);

    }

    @Override
    public Module getModule(String name)
    {
        return this.modules.get(name);
    }

    @Override
    public Collection<Module> getEnabledModules()
    {
        return $.filter(this.modules.values(), new Predicate<Module>() {
            @Override
            public boolean test(Module module)
            {
                return module != null && module.isLoaded();
            }
        });
    }

    @Override
    public void loadModules()
    {
        for (Pair<IModuleResource, ModuleConfig> entry : this.loadableResources.values())
            loadModule(entry);
    }

    @Override
    public void removeModule(Module module)
    {
        if (module == null) return;

        try (IModuleLoader moduleLoader = module.getModuleLoader())
        {
            if (runtimeModuleHandler != null)
                runtimeModuleHandler.handlePreUnloadModule(this, module);

            this.modules.remove(moduleLoader.getModuleConfig().getName());

            module.onUnload();
            module.setLoaded(false);

            if (runtimeModuleHandler != null)
                runtimeModuleHandler.handlePostUnloadModule(this, moduleLoader.getModuleConfig());

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void removeModules()
    {
        for (Module module : modules.values()) removeModule(module);
    }

    /*= --------------------------------------------------------------------- =*/

    private void loadModule(Pair<IModuleResource, ModuleConfig> entry)
    {
        Collection<String> notLoadabledDepends = loadModuleDependencies(entry);
        if (notLoadabledDepends.size() > 0)
        {
            if (runtimeModuleHandler != null)
                runtimeModuleHandler.handleDependenciesNotFound(this, entry.getSecond(), notLoadabledDepends);

            return;
        }

        try
        {
            if (runtimeModuleHandler != null)
                runtimeModuleHandler.handlePreLoadModule(this, entry.getSecond());

            IModuleLoader moduleLoader = new DefaultModuleLoader(entry.getFirst(), entry.getSecond());

            Module module = moduleLoader.loadModule();

            if (module == null) throw new ModuleLoadException("Failed to load module");

            if (runtimeModuleHandler != null)
                runtimeModuleHandler.handlePreLoadModule(this, module);

            module.setDataFolder(new File(this.moduleConfigDirectory, module.getName()));

            module.onLoad();
            module.setLoaded(true);

            this.loadableResources.remove(module.getName());
            this.modules.put(module.getName(), module);

            if (runtimeModuleHandler != null)
                runtimeModuleHandler.handlePostLoadModule(this, module);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private Collection<String> loadModuleDependencies(Pair<IModuleResource, ModuleConfig> entry)
    {
        Collection<String> cannotLoadedEntries = $.newArrayList();

        if (entry.getSecond().getModuleDependencies() != null && entry.getSecond().getModuleDependencies().length > 0)
        {
            for (String depend : entry.getSecond().getModuleDependencies())
            {
                if (this.modules.containsKey(depend)) continue;

                if (this.loadableResources.containsKey(depend))
                {
                    loadModule(this.loadableResources.get(depend));
                    continue;
                }

                if (entry.getSecond().isIgnoreDependencies()) continue;
                else cannotLoadedEntries.add(depend);
            }
        }

        return cannotLoadedEntries;
    }

    private boolean filterFiles(File pathname)
    {
        return !pathname.isDirectory() && pathname.exists() && pathname.length() > 0 && (pathname.getName().endsWith(".jar") || pathname.getName().endsWith(".zip") ||
                pathname.getName().endsWith(".war"));
    }
}