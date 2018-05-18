package de.dytanic.cloudnet.webserver.webhandler;

import de.dytanic.cloudnet.webserver.util.PathProvider;
import de.dytanic.cloudnet.webserver.webhandler.context.IWebContext;

public abstract class HttpMethodWebHandler extends WebHandler {

    protected HttpMethodWebHandler(String path)
    {
        super(path);
    }

    @Override
    public final void onHttpRequest(IWebContext webContext, PathProvider pathProvider) throws Exception
    {
        switch (webContext.getContextMethod().toLowerCase())
        {
            case "put":
                put(webContext, pathProvider);
                break;
            case "post":
                post(webContext, pathProvider);
                break;
            case "trace":
                trace(webContext, pathProvider);
                break;
            case "delete":
                delete(webContext, pathProvider);
                break;
            case "head":
                head(webContext, pathProvider);
                break;
            case "options":
                options(webContext, pathProvider);
                break;
            case "connect":
                connect(webContext, pathProvider);
                break;
            default:
                get(webContext, pathProvider);
                break;
        }
    }

    public abstract void get(IWebContext webContext, PathProvider pathProvider) throws Exception;

    public abstract void put(IWebContext webContext, PathProvider pathProvider) throws Exception;

    public abstract void post(IWebContext webContext, PathProvider pathProvider) throws Exception;

    public abstract void trace(IWebContext webContext, PathProvider pathProvider) throws Exception;

    public abstract void delete(IWebContext webContext, PathProvider pathProvider) throws Exception;

    public abstract void head(IWebContext webContext, PathProvider pathProvider) throws Exception;

    public abstract void options(IWebContext webContext, PathProvider pathProvider) throws Exception;

    public abstract void connect(IWebContext webContext, PathProvider pathProvider) throws Exception;

}