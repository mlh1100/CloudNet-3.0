package de.dytanic.cloudnet.service.impl.storage;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.service.storage.IStorageService;
import de.dytanic.cloudnet.util.file.FileUtils;
import de.dytanic.cloudnet.util.zip.ZipConverter;
import lombok.Getter;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.Function;

public final class LocalStorageService implements IStorageService {

    @Getter
    private final String serviceName = "local";

    private final File baseDirectory;

    /*
       local/storage/templates/* (region/name)
       local/storage/services/* (name)
    */
    public LocalStorageService(String path)
    {
        this.baseDirectory = new File(path);

        this.baseDirectory.mkdirs();

        new File(this.baseDirectory, "services").mkdirs();
        new File(this.baseDirectory, "templates").mkdirs();
    }

    @Override
    public Collection<String> getTemplates(String region)
    {
        Collection<String> templates = $.newArrayList();

        if (region == null) return templates;

        File dir = new File(this.baseDirectory, "templates/" + region);

        if (dir.isDirectory()) return templates;

        templates.addAll($.transform(dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.exists() && pathname.isDirectory();
            }
        }), new Function<File, String>() {
            @Override
            public String apply(File file)
            {
                return file.getName();
            }
        }));

        return templates;
    }

    @Override
    public Collection<String> getServiceDirectories()
    {
        Collection<String> serviceDirs = $.newArrayList();

        File dir = new File(this.baseDirectory, "services");

        if (dir.isDirectory()) return serviceDirs;

        serviceDirs.addAll($.transform(dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.exists() && pathname.isDirectory();
            }
        }), new Function<File, String>() {
            @Override
            public String apply(File file)
            {
                return file.getName();
            }
        }));

        return serviceDirs;
    }

    @Override
    public void clearCaches()
    {

    }

    @Override
    public boolean hasTemplate(String region, String name)
    {
        File dir = new File(this.baseDirectory, "templates/" + region + $.SLASH_STRING + name);

        return dir.exists() && dir.isDirectory();
    }

    @Override
    public byte[] copyTemplate(String region, String name) throws Exception
    {
        if (region == null || name == null) return new byte[0];
        Path path = Paths.get(this.baseDirectory.getName() + "templates/" + region + $.SLASH_STRING + name);

        if (!Files.exists(path)) return new byte[0];

        return ZipConverter.convert(new Path[]{path});
    }

    @Override
    public boolean copyTemplate(String region, String name, Path destinationDirectory)
    {
        if (region == null || name == null || destinationDirectory == null) return false;

        if (hasTemplate(region, name))
            try
            {
                FileUtils.copyFilesInDirectory(new File(this.baseDirectory, "templates/" + region + $.SLASH_STRING + name), destinationDirectory.toFile());
                return true;
            } catch (Exception ex)
            {

                ex.printStackTrace();
                return false;
            }
        else return false;
    }

    @Override
    public boolean deployTemplate(String region, String name, byte[] data) throws Exception
    {
        if (region == null || name == null || data == null || data.length == 0) return false;

        try
        {
            Path path = Paths.get(this.baseDirectory.getName() + "templates/" + region + $.SLASH_STRING + name);
            if (!Files.exists(path)) Files.createDirectories(path);

            ZipConverter.extract(data, path);
            return true;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deployTemplate(String region, String name, Path inputDirectory)
    {
        if (region == null || name == null || inputDirectory == null) return false;

        if (hasTemplate(region, name))
            try
            {
                FileUtils.copyFilesInDirectory(inputDirectory.toFile(), new File(this.baseDirectory, "templates/" + region + $.SLASH_STRING + name));
                return true;
            } catch (Exception ex)
            {

                ex.printStackTrace();
                return false;
            }
        else return false;
    }

    @Override
    public boolean deleteTemplate(String region, String name)
    {
        if (region == null || name == null) return false;

        FileUtils.deleteDirectory(new File(this.baseDirectory, "templates/" + region + $.SLASH_STRING + name));
        return true;
    }

    @Override
    public boolean hasServiceDirectory(String name)
    {
        File dir = new File(this.baseDirectory, "services/" + name);

        return dir.exists() && dir.isDirectory();
    }

    @Override
    public boolean copyServiceDirectory(String name, Path destinationDirectory) throws Exception
    {
        if (name == null || destinationDirectory == null) return false;

        if (hasServiceDirectory(name))
            try
            {
                FileUtils.copyFilesInDirectory(new File(this.baseDirectory, "services/" + name), destinationDirectory.toFile());
                return true;
            } catch (Exception ex)
            {

                ex.printStackTrace();
                return false;
            }
        else return false;
    }

    @Override
    public byte[] copyServiceDirectory(String name) throws Exception
    {
        if (name == null) return new byte[0];
        Path path = Paths.get(this.baseDirectory.getName() + "services/" + name);

        if (!Files.exists(path)) return new byte[0];

        return ZipConverter.convert(new Path[]{path});
    }

    @Override
    public boolean deployServiceDirectory(String name, byte[] data) throws Exception
    {
        if (name == null || data == null || data.length == 0) return false;

        try
        {
            Path path = Paths.get(this.baseDirectory.getName() + "services/" + name);
            if (!Files.exists(path)) Files.createDirectories(path);

            ZipConverter.extract(data, path);
            return true;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deployServiceDirectory(String name, Path inputDirectory) throws Exception
    {
        if (name == null || inputDirectory == null) return false;

        if (hasServiceDirectory(name))
            try
            {
                FileUtils.copyFilesInDirectory(inputDirectory.toFile(), new File(this.baseDirectory, "services/" + name));
                return true;
            } catch (Exception ex)
            {

                ex.printStackTrace();
                return false;
            }
        else return false;
    }

    @Override
    public boolean deleteServiceDirectory(String name)
    {
        if (name == null) return false;

        FileUtils.deleteDirectory(new File(this.baseDirectory, "services/" + name));
        return true;
    }

    @Override
    public boolean isRemoteService()
    {
        return false;
    }
}