package de.dytanic.cloudnet.module;

import de.dytanic.cloudnet.module.util.ModuleLibrary;
import de.dytanic.cloudnet.registry.IServiceProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public abstract class Module implements IModuleExecutor, IServiceProvider {

    protected ModuleConfig moduleConfig;

    protected IModuleLoader moduleLoader;

    protected File dataFolder;

    private boolean loaded;

    public final String getName()
    {
        return moduleConfig.getName();
    }

    public final String getVersion()
    {
        return moduleConfig.getVersion();
    }

    public final String getAuthor()
    {
        return moduleConfig.getAuthor();
    }

    public final String getDescription()
    {
        return moduleConfig.getDescription();
    }

    public final String getWebsite()
    {
        return moduleConfig.getWebsite();
    }

    public final ModuleLibrary[] getLibraries()
    {
        return moduleConfig.getModuleLibraries();
    }

    public final String[] getDependencies()
    {
        return moduleConfig.getModuleDependencies();
    }

}