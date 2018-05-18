package de.dytanic.cloudnet.webserver.webhandler;

import java.util.Collection;

public interface IWebHandlerProvider {

    void registerHandler(WebHandler webHandler);

    void unregisterHandler(Class<? extends WebHandler> webHandlerClazz);

    Collection<WebHandler> getHandlers();

    /*= -------------------------------------------------------------------- =*/

    default void registerHandlers(WebHandler... webHandlers)
    {
        if(webHandlers == null) return;

        for(WebHandler handler : webHandlers)
            if(handler != null)
                registerHandler(handler);
    }

    default void unregisterHandlers(Class<? extends WebHandler>... webHandlerClazzes)
    {
        if(webHandlerClazzes == null) return;

        for(Class<? extends WebHandler> clazz : webHandlerClazzes)
            if(clazz != null)
                unregisterHandler(clazz);
    }

    default void clearHandlers()
    {
        for(WebHandler webHandler : getHandlers())
            unregisterHandler(webHandler.getClass());
    }

}