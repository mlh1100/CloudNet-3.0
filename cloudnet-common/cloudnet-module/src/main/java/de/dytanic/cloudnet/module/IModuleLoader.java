package de.dytanic.cloudnet.module;

import java.net.URLClassLoader;

public interface IModuleLoader extends AutoCloseable {

    URLClassLoader getClassLoader();

    IModuleResource getModuleResource();

    ModuleConfig getModuleConfig();

    boolean isAvailable();

    Module loadModule() throws Exception;

}