package de.dytanic.cloudnet.module.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public final class FinalizeURLClassLoader extends URLClassLoader {

    public FinalizeURLClassLoader(URL[] urls)
    {
        super(urls);
    }

    public FinalizeURLClassLoader(URL url)
    {
        this(new URL[]{url});
    }

    @Override
    public void close() throws IOException
    {
        super.close();

        try
        {
            finalize();
        } catch (Throwable throwable)
        {
        }
    }
}