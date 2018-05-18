package de.dytanic.cloudnet.webserver.webhandler.defaults;

import de.dytanic.cloudnet.webserver.util.PathProvider;
import de.dytanic.cloudnet.webserver.webhandler.HttpMethodWebHandler;
import de.dytanic.cloudnet.webserver.webhandler.context.IWebContext;

public class HttpMethodWebHandlerAdapter extends HttpMethodWebHandler {

    protected HttpMethodWebHandlerAdapter(String path)
    {
        super(path);
    }

    @Override
    public void get(IWebContext webContext, PathProvider pathProvider) throws Exception
    {

    }

    @Override
    public void put(IWebContext webContext, PathProvider pathProvider) throws Exception
    {

    }

    @Override
    public void post(IWebContext webContext, PathProvider pathProvider) throws Exception
    {

    }

    @Override
    public void trace(IWebContext webContext, PathProvider pathProvider) throws Exception
    {

    }

    @Override
    public void delete(IWebContext webContext, PathProvider pathProvider) throws Exception
    {

    }

    @Override
    public void head(IWebContext webContext, PathProvider pathProvider) throws Exception
    {

    }

    @Override
    public void options(IWebContext webContext, PathProvider pathProvider) throws Exception
    {

    }

    @Override
    public void connect(IWebContext webContext, PathProvider pathProvider) throws Exception
    {

    }
}