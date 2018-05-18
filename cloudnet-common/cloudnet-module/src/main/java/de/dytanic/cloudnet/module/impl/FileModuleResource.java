package de.dytanic.cloudnet.module.impl;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.module.IModuleManager;
import de.dytanic.cloudnet.module.ModuleConfig;
import de.dytanic.cloudnet.module.exception.ModuleConfigNotFoundException;
import de.dytanic.cloudnet.module.IModuleResource;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileModuleResource implements IModuleResource {

    protected final File file;

    public FileModuleResource(File file)
    {
        this.file = file;
    }

    public FileModuleResource(Path path)
    {
        this(path.toFile());
    }

    @Override
    public URL getUrl()
    {
        try
        {
            return file.toURI().toURL();
        } catch (MalformedURLException e)
        {
        }

        return null;
    }

    @Override
    public ModuleConfig loadConfig()
    {
        try (ZipFile zipFile = new ZipFile(file))
        {
            ZipEntry zipEntry = zipFile.getEntry(IModuleManager.DEFAULT_MODULE_CONFIG_NAME);

            if (zipEntry == null) throw new ModuleConfigNotFoundException("ModuleConfig cannot be found");

            try (InputStream inputStream = zipFile.getInputStream(zipEntry); InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
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