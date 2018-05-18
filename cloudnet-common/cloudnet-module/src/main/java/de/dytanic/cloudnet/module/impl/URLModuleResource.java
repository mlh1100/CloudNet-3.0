package de.dytanic.cloudnet.module.impl;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.ModuleConfig;
import de.dytanic.cloudnet.module.util.FinalizeURLClassLoader;
import de.dytanic.cloudnet.module.IModuleResource;
import de.dytanic.cloudnet.module.exception.ModuleConfigNotFoundException;
import lombok.Getter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;

public class URLModuleResource implements IModuleResource {

    @Getter
    protected URL url;

    public URLModuleResource(URL url)
    {
        this.url = url;
    }

    @Override
    public ModuleConfig loadConfig()
    {
        try (URLClassLoader urlClassLoader = new FinalizeURLClassLoader(url);
             InputStream inputStream = urlClassLoader.getResourceAsStream(IModuleManager.DEFAULT_MODULE_CONFIG_NAME))
        {

            if (inputStream == null) throw new ModuleConfigNotFoundException("ModuleConfig cannot be found");

            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8))
            {
                return $.GSON.fromJson($.JSON_PARSER.parse(inputStreamReader), ModuleConfig.TYPE);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

}