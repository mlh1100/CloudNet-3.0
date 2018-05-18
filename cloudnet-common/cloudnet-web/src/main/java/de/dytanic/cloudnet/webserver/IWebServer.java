package de.dytanic.cloudnet.webserver;

import de.dytanic.cloudnet.webserver.webhandler.IWebHandlerProvider;

public interface IWebServer extends IWebHandlerProvider, AutoCloseable {

    IWebServer bind();

    WebConfig getConfig();

}