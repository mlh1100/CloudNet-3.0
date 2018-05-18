package de.dytanic.cloudnet.module;

import de.dytanic.cloudnet.module.util.IRuntimeModuleHandler;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public interface IModuleManager {

    String DEFAULT_MODULE_CONFIG_NAME = "module.json", DEFAULT_MODULE_DIRECTORY = "modules";

    void setRuntimeModuleHandler(IRuntimeModuleHandler handler);

    void setModuleBlackList(Collection<String> moduleBlackList);

    Collection<String> getModuleBlackList();

    void setModuleConfigDirectory(File directory);

    File getModuleConfigDirectory();

    void detectModule(File file);

    void detectModules(URL... urls);

    void detectModules(File directory);

    void addModuleResource(IModuleResource moduleResource);

    Module getModule(String name);

    Collection<Module> getEnabledModules();

    void loadModules();

    void removeModule(Module module);

    void removeModules();

    /*= ------------------------------------- =*/

    default void detectModule(Path path)
    {
        detectModule(path.toFile());
    }

    default void detectModule(String path)
    {
        detectModule(new File(path));
    }

    default void detectModules(String directoryPath)
    {
        detectModules(Paths.get(directoryPath));
    }

    default void detectModules(Path directory)
    {
        detectModules(directory.toFile());
    }

    default void removeModules(Module... modules)
    {
        for (Module module : modules) removeModule(module);
    }

    default void setDefaultModuleDirectory(String path)
    {
        setModuleConfigDirectory(new File(path));
    }

    default void setDefaultModuleDirectory(Path path)
    {
        if(path == null) return;

        setModuleConfigDirectory(path.toFile());
    }

}