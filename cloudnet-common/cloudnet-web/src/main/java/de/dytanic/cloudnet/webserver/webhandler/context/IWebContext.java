package de.dytanic.cloudnet.webserver.webhandler.context;

import de.dytanic.cloudnet.util.HostAndPort;
import de.dytanic.cloudnet.webserver.util.QueryDecoder;

import java.net.URI;
import java.util.Map;

public interface IWebContext extends AutoCloseable {

    String getUri();

    String getPath();

    URI getURIInstance();

    String getContextMethod();

    String getProtcolVersion();

    HostAndPort getClientAddress();

    QueryDecoder getQueryParameters();

    /*= ============= Request ================ =*/

    String getRequestHeaderProperty(String key);

    boolean hasRequestHeaderProperty(String key);

    /*= ============= Response ================ =*/

    IWebContext setResponseHeader(String key, String value);

    String getResponseHeaderProperty(String key);

    boolean hasResponseHeaderProperty(String key);

    Map<String, String> getResponseHeaders(String key);

    IWebContext setReponseCode(int code);

    IWebContext setResponseData(byte[] data);

    boolean hasResponse();

    byte[] getResponseData();

    int getResponseCode();

    /*= ----------------------------------------------------- =*/

    default String getRequestContentType()
    {
        return getRequestHeaderProperty("Content-Type");
    }

    default IWebContext setResponseContentLength(int length)
    {
        return setResponseHeader("Content-Length", length);
    }

    default IWebContext setResponseContentType(String type)
    {
        return setResponseHeader("Content-Type", type);
    }

    default String getResponseContentType()
    {
        return getResponseHeaderProperty("Content-Type");
    }

    default String getParameter(String name)
    {
        return getQueryParameters().getQueryParams().get(name);
    }

    default boolean containsParameter(String name)
    {
        return getQueryParameters().getQueryParams().containsKey(name);
    }

    default IWebContext setResponseHeader(String key, Object value)
    {
        if(key == null || value == null) return this;

        return setResponseHeader(key, value.toString());
    }

}