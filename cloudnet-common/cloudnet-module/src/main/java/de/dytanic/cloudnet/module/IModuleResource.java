package de.dytanic.cloudnet.module;

import java.net.URL;

public interface IModuleResource {

    URL getUrl();

    ModuleConfig loadConfig();

}