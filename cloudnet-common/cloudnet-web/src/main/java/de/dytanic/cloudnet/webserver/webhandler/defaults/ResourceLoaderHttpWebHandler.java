package de.dytanic.cloudnet.webserver.webhandler.defaults;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.webserver.util.PathProvider;
import de.dytanic.cloudnet.webserver.webhandler.context.IWebContext;
import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public final class ResourceLoaderHttpWebHandler extends HttpMethodWebHandlerAdapter {

    private IHttpWebHandlerResourceLoader resourceLoader;

    public ResourceLoaderHttpWebHandler(String path, IHttpWebHandlerResourceLoader resourceLoader)
    {
        super(path + "/*");

        this.resourceLoader = resourceLoader;
    }

    @Override
    public void get(IWebContext webContext, PathProvider pathProvider) throws Exception
    {
        String path = $.build(pathProvider.getPathAliases(), $.SLASH_STRING);

        try (InputStream inputStream = resourceLoader.loadResource(path);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            if (inputStream == null || pathProvider.getPathAliases().length == 0) return;

            String part = pathProvider.getPathAliases().length == 1 ?
                    pathProvider.getPathAliases()[0]
                    :
                    pathProvider.getPathAliases()[pathProvider.getPathAliases().length - 1];

            webContext.setResponseContentType(parseContentType(part));

            if (webContext.getResponseContentType().equalsIgnoreCase("application/octet-stream"))
                webContext.setResponseHeader("content-disposition", "attachment; filename = " + part + "");

            webContext.setReponseCode(200);

            byte[] buffer = new byte[8192];
            int len;

            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
                byteArrayOutputStream.write(buffer, 0, len);

            buffer = byteArrayOutputStream.toByteArray();

            inputStream.close();

            webContext.setResponseContentLength(buffer.length);
            webContext.setResponseData(buffer);
        }
    }

    /*= ----------------------------------------------------------- =*/

    private String parseContentType(String part)
    {
        if (!part.isEmpty() && part.contains(".") && part.split($.DOT_STRING).length == 2)
        {
            switch (part.split($.DOT_STRING)[1].toLowerCase())
            {
                case "html":
                    return "text/html";
                case "htm":
                    return "text/html";
                case "shtml":
                    return "text/html";
                case "js":
                    return "text/javascript";
                case "json":
                    return "application/json";
                case "css":
                    return "text/css";
                default:
                    return "application/octet-stream";
            }

        } else return "text/plain";
    }

    public interface IHttpWebHandlerResourceLoader {

        InputStream loadResource(String path) throws Exception;

    }

    @AllArgsConstructor
    public static final class ClassLoaderHttpWebHandlerResourceLoader implements IHttpWebHandlerResourceLoader {

        private final String base;

        private final ClassLoader classLoader;

        @Override
        public InputStream loadResource(String path) throws Exception
        {
            return classLoader.getResourceAsStream(base + $.SLASH_STRING + path);
        }
    }

    public static final class FileHttpWebHandlerResourceLoader implements IHttpWebHandlerResourceLoader {

        private final File directory;

        public FileHttpWebHandlerResourceLoader(File directory)
        {
            this.directory = directory;
        }

        @Override
        public InputStream loadResource(String path) throws Exception
        {
            File file = new File(directory, path);
            return file.exists() && !file.isDirectory() ? new FileInputStream(file) : null;
        }
    }

}