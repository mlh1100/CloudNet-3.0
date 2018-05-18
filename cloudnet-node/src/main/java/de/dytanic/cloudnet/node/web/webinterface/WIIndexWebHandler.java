package de.dytanic.cloudnet.node.web.webinterface;

import de.dytanic.cloudnet.webserver.util.PathProvider;
import de.dytanic.cloudnet.webserver.webhandler.context.IWebContext;
import de.dytanic.cloudnet.webserver.webhandler.defaults.HttpMethodWebHandlerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public final class WIIndexWebHandler extends HttpMethodWebHandlerAdapter {

    public WIIndexWebHandler()
    {
        super("webinterface");
    }

    @Override
    public void get(IWebContext webContext, PathProvider pathProvider) throws Exception
    {
        try (InputStream inputStream = WIIndexWebHandler.class.getClassLoader().getResourceAsStream("web/index.html");
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            if (inputStream == null)
            {
                webContext.setReponseCode(200);
                webContext.setResponseContentType("text/plain");
                webContext.setResponseData("index.html not found!".getBytes());
                return;
            }

            byte[] buffer = new byte[6144];
            int len;

            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
                byteArrayOutputStream.write(buffer, 0, len);

            webContext.setResponseContentType("text/html");
            webContext.setReponseCode(200);
            webContext.setResponseData(buffer);
        }
    }
}