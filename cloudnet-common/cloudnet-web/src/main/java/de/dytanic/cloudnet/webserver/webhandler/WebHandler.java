package de.dytanic.cloudnet.webserver.webhandler;

import de.dytanic.cloudnet.webserver.util.PathProvider;
import de.dytanic.cloudnet.webserver.webhandler.context.IWebContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This is the basic WebHandler class for handle Http requests
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class WebHandler {

    /**
     * {name} alias for a path part
     * Client:
     * api/v1/local/templates
     *
     * WebHandler
     * api/{version}/{storage}/local/{storage_region}
     *
     * PathProvider WrappedMap entries: version, storage, storage_region
     *
     * '*' alias for the next path space
     *
     * example '*':
     * Client:
     * api/v1/dir/local/templates
     *
     * WebHandler
     * api/v1/dir/*
     *
     */
    protected String path;

    /**
     * Basic context Handler for a Http Session with the WebServer
     * @param webContext Context Handler for this session. The handler instance is in use for all WebHandlers in a Session
     * @param pathProvider He managed Path aliases an the Path of a Http Request
     * @throws Exception Handles the exception for a easier programming
     */
    public abstract void onHttpRequest(IWebContext webContext, PathProvider pathProvider) throws Exception;

}