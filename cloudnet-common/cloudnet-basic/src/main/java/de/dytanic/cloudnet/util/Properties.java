package de.dytanic.cloudnet.util;

import de.dytanic.cloudnet.$;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class Properties extends LinkedHashMap<String, String> {

    public boolean getBoolean(String key)
    {
        if (!containsKey(key)) return false;

        return Boolean.parseBoolean(get(key));
    }

    /*= -------------------------------------------------------------- =*/

    public void load(File file) throws IOException
    {
        load(file.toPath());
    }

    public void load(Path path) throws IOException
    {
        if (Files.exists(path))
            try (InputStream inputStream = Files.newInputStream(path))
            {
                load(inputStream);
            }
    }

    public void load(InputStream inputStream) throws IOException
    {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        {
            load(inputStreamReader);
        }
    }

    public void load(Reader reader) throws IOException
    {
        try (BufferedReader bufferedReader = new BufferedReader(reader))
        {
            String input;

            while ((input = bufferedReader.readLine()) != null)
            {
                if (input.isEmpty() || input.equals($.EMPTY_STRING) || input.equals($.SPACE_STRING) || input.startsWith("#") || !input.contains("="))
                    continue;

                int x = input.indexOf("=");

                put(input.substring(0, x), input.substring(x + 1, input.length()));
            }
        }
    }

    public void save(File file) throws IOException
    {
        save(null, file.toPath());
    }

    public void save(String commit, File file) throws IOException
    {
        save(commit, file.toPath());
    }

    public void save(Path path) throws IOException
    {
        save(null, path);
    }

    public void save(String commit, Path path) throws IOException
    {
        if (!Files.exists(path)) Files.createFile(path);

        try (OutputStream outputStream = Files.newOutputStream(path))
        {
            save(commit, outputStream);
        }
    }

    public void save(OutputStream outputStream) throws IOException
    {
        save(null, outputStream);
    }

    public void save(String commit, OutputStream outputStream) throws IOException
    {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
        {
            save(commit, outputStreamWriter);
        }
    }

    public void save(Writer writer) throws IOException
    {
        save(null, writer);
    }

    public void save(String commit, Writer writer) throws IOException
    {
        try (PrintWriter printWriter = new PrintWriter(writer))
        {
            if (commit != null)
                for (String key : commit.split("\n"))
                    printWriter.write("# " + key.replace("\n", $.EMPTY_STRING) + $.LINE_SEPARATOR);

            for (Map.Entry<String, String> keys : entrySet())
                if (keys.getKey() != null && keys.getValue() != null)
                    printWriter.write(keys.getKey() + "=" + keys.getValue() + $.LINE_SEPARATOR);

            printWriter.flush();
        }
    }

    public void toLower()
    {
        for(Map.Entry<String, String> entry : entrySet())
            replace(entry.getKey(), entry.getValue().toLowerCase());
    }

    public java.util.Properties java()
    {
        java.util.Properties properties = new java.util.Properties();

        for(Map.Entry<String, String> entry : this.entrySet())
            properties.setProperty(entry.getKey(), entry.getValue());

        return properties;
    }

}