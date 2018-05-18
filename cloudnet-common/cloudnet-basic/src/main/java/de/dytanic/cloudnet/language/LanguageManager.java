package de.dytanic.cloudnet.language;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.util.Properties;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/*
 * english.lang
 */
public final class LanguageManager {

    private LanguageManager()
    {
    }

    @Getter
    @Setter
    private static String language;

    private static final Map<String, Properties> LANGUAGE_CACHE = $.newHashMap();

    public static String getMessage(String property)
    {
        if (language == null || !LANGUAGE_CACHE.containsKey(language)) return "MESSAGE OR LANGUAGE NOT FOUND!";

        return LANGUAGE_CACHE.get(language).get(property);
    }

    public static void addLanguageFile(String language, Properties properties)
    {
        if (language == null || properties == null) return;

        if (LANGUAGE_CACHE.containsKey(language))
            LANGUAGE_CACHE.get(language).putAll(properties);
        else
            LANGUAGE_CACHE.put(language, properties);
    }

    public static void addLanguageFile(String language, File file)
    {
        addLanguageFile(language, file.toPath());
    }

    public static void addLanguageFile(String language, Path file)
    {
        try (InputStream inputStream = Files.newInputStream(file))
        {
            addLanguageFile(language, inputStream);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void addLanguageFile(String language, InputStream inputStream)
    {
        Properties properties = new Properties();

        try
        {
            properties.load(inputStream);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        addLanguageFile(language, properties);
    }

}