package de.dytanic.cloudnet.util.file;

import de.dytanic.cloudnet.$;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.function.Predicate;

public final class FileUtils {

    private FileUtils()
    {
    }

    public static void copyFileToDirectory(File file, File to) throws IOException
    {
        if (to == null || file == null) return;

        if (!to.exists()) to.mkdirs();

        File n = new File(to.getAbsolutePath() + $.SLASH_STRING + file.getName());
        Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFilesInDirectory(File from, File to) throws IOException
    {
        if (to == null || from == null) return;

        if (!to.exists()) to.mkdirs();

        if (!from.isDirectory()) return;

        for (File file : from.listFiles())
        {
            if (file == null) continue;

            if (file.isDirectory())
                copyFilesInDirectory(file, new File(to.getAbsolutePath() + $.SLASH_STRING + file.getName()));
            else
            {
                File n = new File(to.getAbsolutePath() + $.SLASH_STRING + file.getName());
                Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public static void extractResource(String path, String targetPath)
    {
        try (InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(path))
        {
            Files.copy(inputStream, Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteDirectory(File file)
    {
        if (file == null) return;

        if (file.isDirectory()) for (File entry : file.listFiles()) deleteDirectory(entry);
        else file.delete();
    }

    /*= --------------------------------------------------------------------------------------------- =*/

    public static File detectCraftBukkitFile(File directory)
    {
        if (directory == null || !directory.isDirectory()) return null;

        return $.filterFirst(Objects.requireNonNull(directory.listFiles()), new Predicate<File>() {
            @Override
            public boolean test(File file)
            {
                String name = file.getName().toLowerCase();
                return file.exists() && !file.isDirectory() && name.endsWith(".jar") && (name.contains("spigot") || name.contains("craftbukkit"));
            }
        });
    }

    public static File detectGlowstoneFile(File directory)
    {
        if (directory == null || !directory.isDirectory()) return null;

        return $.filterFirst(Objects.requireNonNull(directory.listFiles()), new Predicate<File>() {
            @Override
            public boolean test(File file)
            {
                String name = file.getName().toLowerCase();
                return file.exists() && !file.isDirectory() && name.endsWith(".jar") && name.contains("glowstone");
            }
        });
    }

    public static File detectSpongeFile(File directory)
    {
        if (directory == null || !directory.isDirectory()) return null;

        return $.filterFirst(Objects.requireNonNull(directory.listFiles()), new Predicate<File>() {
            @Override
            public boolean test(File file)
            {
                String name = file.getName().toLowerCase();
                return file.exists() && !file.isDirectory() && name.endsWith(".jar") && name.contains("sponge");
            }
        });
    }

    public static File detectBungeeCordFile(File directory)
    {
        if (directory == null || !directory.isDirectory()) return null;

        return $.filterFirst(Objects.requireNonNull(directory.listFiles()), new Predicate<File>() {
            @Override
            public boolean test(File file)
            {
                String name = file.getName().toLowerCase();
                return file.exists() && !file.isDirectory() && name.endsWith(".jar") && (name.contains("bungee") || name.contains("waterfall") ||
                        name.contains("travertine") || name.contains("flexpipe") || name.contains("proxy"));
            }
        });
    }

}